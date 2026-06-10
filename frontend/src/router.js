import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from './composables/useAuth'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Admin from './views/Admin.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const { isLoggedIn, checkAuth } = useAuth()
  
  // Verificar autenticación guardada
  checkAuth()
  
  if (to.meta.requiresAuth && !isLoggedIn.value) {
    next('/login')
  } else if (to.path === '/login' && isLoggedIn.value) {
    next('/admin')
  } else {
    next()
  }
})

export default router
