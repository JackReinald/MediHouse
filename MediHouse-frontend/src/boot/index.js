import { boot } from 'quasar/wrappers'
import { LocalStorage } from 'quasar'


export default boot(async function ({ router }) {


  // --- GUARDIA ACTUALIZADO AL ESTILO MODERNO (SIN NEXT) ---
  router.beforeEach(async (to) => {
    const isAuthenticated = LocalStorage.getItem('isLoggedIn') === true
    
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

    console.log({
    path: to.path,
    requiresAuth: requiresAuth,
    isAuthenticated: isAuthenticated,
  })

    // 1. Si la ruta requiere autenticación y el usuario NO está logueado
    if (requiresAuth && !isAuthenticated) {
      // Redirigimos al login
      return { path: '/login' }
    }

    // 2. Evitamos que un usuario ya logueado vuelva a la página de Login
    if (to.path === '/login' && isAuthenticated) {
      // Redirigimos al inicio
      return { path: '/' }
    }

    // Si no se retorna nada (undefined), la navegación continúa normalmente
  })
})