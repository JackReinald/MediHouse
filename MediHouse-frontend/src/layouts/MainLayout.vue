<template>
  <q-layout view="hHh lpR lff">
    <q-header reveal bordered class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg" />
          </q-avatar>
          Title
        </q-toolbar-title>

        <q-btn dense flat round icon="menu" @click="toggleRightDrawer" />
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" elevated>
      <q-scroll-area class="fit">
        <div class="q-pa-lg bg-secondary text-white text-center">
          <q-avatar size="72px" class="q-mb-sm">
            <img src="https://cdn.quasar.dev/img/avatar.png" />
          </q-avatar>
          <div class="text-weight-bold">Juan Pérez</div>
          <div>Admin del Hogar</div>
        </div>

        <q-list padding>
          <q-item clickable v-ripple>
            <q-item-section avatar><q-icon name="person" /></q-item-section>
            <q-item-section>Mi Perfil</q-item-section>
          </q-item>

          <q-item clickable v-ripple>
            <q-item-section avatar><q-icon name="notifications" /></q-item-section>
            <q-item-section>Configurar Alertas</q-item-section>
          </q-item>

          <q-separator inset spaced size="3px"/>

          <q-item-label header>Configuración</q-item-label>

          <q-item tag="label" v-ripple>
            <q-item-section>
              <q-item-label v-if="isDarkMode == false">
                Modo Claro
              </q-item-label>
              <q-item-label v-else>
                Modo Oscuro
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-toggle color="blue" v-model="isDarkMode" @update:model-value="setDarkMode" :icon="isDarkMode? 'dark_mode': 'light_mode'"/>
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple class="text-red" @click="logout">
            <q-item-section avatar><q-icon name="logout" color="red" /></q-item-section>
            <q-item-section>Cerrar Sesión</q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-drawer show-if-above v-model="rightDrawerOpen" side="right" elevated>
      <q-scroll-area class="fit">
        <div class="q-pa-md">
          <div class="text-h6 q-mb-md">Panel de Control</div>

          <q-list bordered separator class="rounded-borders">
            <q-item-label header class="text-negative text-weight-bold">
              <q-icon name="warning" color="negative" /> Vencimientos Próximos
            </q-item-label>

            <q-item v-for="n in 3" :key="n" clickable v-ripple>
              <q-item-section>
                <q-item-label class="text-weight-bold">Paracetamol</q-item-label>
                <q-item-label caption>Vence en 2 días</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-badge color="red" label="Urgente" />
              </q-item-section>
            </q-item>
          </q-list>

          <div class="text-subtitle2 q-mt-lg q-mb-sm">Filtrar Inventario</div>
          <q-select
            dense
            outlined
            v-model="filter"
            :options="categories"
            label="Categoría"
            class="q-mb-sm"
          />
          <q-toggle v-model="onlyLowStock" label="Solo stock bajo" color="primary" />
        </div>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { useQuasar } from 'quasar'
import { ref } from 'vue'

const $q = useQuasar();

const leftDrawerOpen = ref(false)
const rightDrawerOpen = ref(false)
const isDarkMode = ref(false)

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

function toggleRightDrawer() {
  rightDrawerOpen.value = !rightDrawerOpen.value
}

function setDarkMode(value) {
  $q.dark.set(value);
}

function logout() {
  $q.localStorage.set('isLoggedIn', false);
  window.location.reload();
}
</script>
