<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form class="q-gutter-xs q-pa-md row" @submit="handleLogin" @reset="onReset" style="width: 100%; max-width: 400px;">
      <q-input
        class="col-12"
        v-model="loginData.email"
        filled
        label="Ingrese su correo electrónico"
        placeholder="mary@domain.com"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Por favor ingrese su correo']"
      />
      <q-input
        class="col-12"
        v-model="loginData.password"
        filled
        :type="isVisible ? 'password' : 'text'"
        placeholder="Aguacate1234"
        label="Ingrese su contraseña"
        lazy-rules
        :rules="[
          (val) => (val && val.length >= 10) || '1. La clave debe tener al menos 10 caracteres.',
          (val) => /[A-Z]/.test(val) || '2. La clave debe contener al menos 1 mayúscula.',
          (val) => /\d/.test(val) || '3. La clave debe contener al menos 1 número.',
        ]"
      >
        <template v-slot:append>
          <q-icon
            :name="isVisible ? 'visibility_off' : 'visibility'"
            class="cursor-pointer"
            @click="isVisible = !isVisible"
          />
        </template>
      </q-input>

      <div class="q-mt-md  col-12 row justify-evenly">
        <q-btn type="submit" icon="mail">Enviar</q-btn>
        <q-btn type="reset" icon="clear_all" hint="something123">Limpiar todo</q-btn>
      </div>
    </q-form>
  </q-page>
</template>

<script setup>
import { useQuasar } from 'quasar'
import { api } from 'src/boot/axios';
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const $q = useQuasar()
const router = useRouter();

const loginData = ref({
  email: '',
  password: '',
})

const isVisible = ref(true)

const onReset = () => {
  loginData.value = {}
  isVisible.value = true
}

const handleLogin = async () => {
  try {
    const response = api.post('/api/auth/login', {
      email: loginData.value.email,
      password: loginData.value.password,
    });

    console.log("Response:", await response);

    if ((await response).data.success) {
      $q.localStorage.set('isLoggedIn', true)
      $q.notify({
        color: 'green-4',
        textColor: 'white',
        icon: 'cloud_done',
        message: (await response).data.message,
      });

      router.push('/')
    }
  } catch (error) {
    $q.notify({
      color: 'red-4',
      textColor: 'white',
      icon: 'error',
      message: error.response.data.message || 'Error during login',
    });
  }
}
</script>
