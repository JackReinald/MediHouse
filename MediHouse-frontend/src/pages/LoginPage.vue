<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form class="q-gutter-xs q-pa-md row" @submit="onSubmit" @reset="onReset" style="width: 100%; max-width: 400px;">
      <q-input
        class="col-12"
        v-model="email"
        filled
        label="Ingrese su correo electrónico"
        placeholder="mary@domain.com"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Por favor ingrese su correo']"
      />
      <q-input
        class="col-12"
        v-model="password"
        filled
        :type="isPwd ? 'password' : 'text'"
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
            :name="isPwd ? 'visibility_off' : 'visibility'"
            class="cursor-pointer"
            @click="isPwd = !isPwd"
          />
        </template>
      </q-input>

      <div class="q-mt-md  col-12 row justify-evenly">
        <q-btn type="submit" icon="mail">Enviar</q-btn>
        <q-btn type="reset" icon="clear_all" hint="something123">Limpiar todo</q-btn>
      </div>
      <div class="col-12 row justify-center">
        <q-toggle v-model="accept" label="¿El servidor respondió bien?" />
      </div>
    </q-form>
  </q-page>
</template>

<script setup>
import { useQuasar } from 'quasar'
import { ref } from 'vue'
const $q = useQuasar()

const email = ref(null)
const password = ref(null)
const isPwd = ref(true)
const accept = ref(false)

const onSubmit = () => {
  if (accept.value) {
    console.log('Entró al condicional de aceptado')
    $q.notify({
      color: 'green-4',
      textColor: 'white',
      icon: 'cloud_done',
      message: 'Submitted',
    })
  } else {
    console.log('Entró al condicional de rechazado')

    $q.notify({
      color: 'red-4',
      textColor: 'white',
      icon: 'error',
      message: 'Not Submitted',
    })
  }
}
const onReset = () => {
  email.value = null
  password.value = null
  isPwd.value = true
  accept.value = false
}
</script>
