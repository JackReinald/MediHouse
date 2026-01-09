<template>
  <q-page class="flex flex-center column">
    <h4 class="text-center" style="border: 2px blue solid">Registro de usuarios</h4>

    <q-form
      class="q-pa-lg q-px-xl shadow-5 rounded-borders"
      @submit="handleRegister"
      @reset="onReset"
    >
      <q-input
        v-model="registerData.name"
        outlined
        label="Ingrese su nombre(s)"
        placeholder="Pepito"
        lazy-rules
        :rules="nameRules"
        color="secondary"
      />
      <q-input
        v-model="registerData.lastname"
        outlined
        label="Ingrese su apellido(s)"
        placeholder="Pérez"
        lazy-rules
        :rules="lastnameRules"
      />
      <q-input
        v-model="registerData.age"
        outlined=""
        label="Ingrese su edad"
        lazy-rules
        :rules="ageRules"
        type="number"
      />
      <q-input
        v-model="registerData.email"
        outlined=""
        label="Ingrese su correo electrónico"
        placeholder="mary@domain.com"
        lazy-rules
        :rules="emailRules"
      />
      <q-input
        v-model="registerData.password"
        outlined=""
        :type="isVisible ? 'password' : 'text'"
        placeholder="Aguacate1234"
        label="Ingrese su contraseña"
        lazy-rules
        :rules="passwordRules"
      >
        <template v-slot:append>
          <q-icon
            :name="isVisible ? 'visibility_off' : 'visibility'"
            class="cursor-pointer"
            @click="isVisible = !isVisible"
          />
        </template>
      </q-input>

      <div class="row q-gutter-x-md justify-evenly">
        <q-btn type="submit" icon="mail">Registrarse</q-btn>
        <q-btn type="reset" icon="clear_all" hint="something123">Limpiar todo</q-btn>
      </div>
    </q-form>
  </q-page>
</template>

<script setup>
import { useQuasar } from 'quasar'
import { api } from 'src/boot/axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const $q = useQuasar()

const router = useRouter()
const isVisible = ref(true)
const registerData = ref({})

const nameRules = [
  (val) => (val && val.length > 0) || 'Por favor ingrese su nombre',
  (val) =>
    /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(val) || 'El nombre solo debe contener letras y espacios',
]
const lastnameRules = [
  (val) => (val && val.length > 0) || 'Por favor ingrese su apellido',
  (val) =>
    /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(val) || 'El apellido solo debe contener letras y espacios',
]
const emailRules = [
  (val) => (val && val.length > 0) || 'Por favor ingrese su correo',
  (val) => /.+@.+\..+/.test(val) || 'Por favor ingrese un correo válido',
]
const passwordRules = [
  (val) => (val && val.length >= 10) || '1. La clave debe tener al menos 10 caracteres.',
  (val) => /[A-Z]/.test(val) || '2. La clave debe contener al menos 1 mayúscula.',
  (val) => /\d/.test(val) || '3. La clave debe contener al menos 1 número.',
]
const ageRules = [
  (val) => (val && val > 0) || 'Por favor ingrese una edad válida',
  (val) => (val && val < 120) || 'Por favor ingrese una edad válida',
]
const onReset = () => {
  registerData.value = {}
  isVisible.value = true
}

const handleRegister = async () => {
  try {
    console.log('Entró a handleRegister')
    const response = api.post('/api/users', registerData.value)

    console.log('Response:', await response)

    if ((await response).data.success) {
      $q.localStorage.set('isLoggedIn', false)
      console.log('Registro existoso')
      $q.notify({
        color: 'green-4',
        textColor: 'white',
        icon: 'cloud_done',
        message: (await response).data.message,
      })
        // Optionally, redirect to login page after successful registration
        router.push('/login')

    }
  } catch (error) {
    console.log('Registro fallido')
    $q.notify({
      color: 'red-4',
      textColor: 'white',
      icon: 'error',
      message: error.response.data.message || 'Error during login',
    })
  }
}
</script>

<style lang="scss" scoped></style>
