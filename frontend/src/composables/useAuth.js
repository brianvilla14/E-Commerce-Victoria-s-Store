import { ref } from 'vue'

const isLoggedIn = ref(false)
const user = ref(null)
const authError = ref('')

export function useAuth() {
  const login = async (email, password) => {
    authError.value = ''
    
    // Validación básica
    if (!email || !password) {
      authError.value = 'Email y contraseña son requeridos'
      return false
    }
    
    if (!email.includes('@')) {
      authError.value = 'Email inválido'
      return false
    }
    
    if (password.length < 6) {
      authError.value = 'La contraseña debe tener al menos 6 caracteres'
      return false
    }
    
    // Simular autenticación (en producción se conectaría a un backend)
    try {
      // Simular delay de red
      await new Promise(resolve => setTimeout(resolve, 800))
      
      isLoggedIn.value = true
      user.value = {
        email: email,
        role: email.includes('admin') ? 'admin' : 'user',
        loginTime: new Date()
      }
      
      // Guardar en localStorage
      localStorage.setItem('user', JSON.stringify(user.value))
      localStorage.setItem('isLoggedIn', 'true')
      
      return true
    } catch (error) {
      authError.value = 'Error al iniciar sesión'
      return false
    }
  }

  const logout = () => {
    isLoggedIn.value = false
    user.value = null
    authError.value = ''
    localStorage.removeItem('user')
    localStorage.removeItem('isLoggedIn')
  }

  const checkAuth = () => {
    const saved = localStorage.getItem('isLoggedIn')
    const savedUser = localStorage.getItem('user')
    
    if (saved === 'true' && savedUser) {
      isLoggedIn.value = true
      user.value = JSON.parse(savedUser)
    }
  }

  return {
    isLoggedIn,
    user,
    authError,
    login,
    logout,
    checkAuth
  }
}
