import { ref, watch } from 'vue'

const isDarkMode = ref(false)

export function useDarkMode() {
  // Cargar preferencia guardada
  const loadTheme = () => {
    const saved = localStorage.getItem('darkMode')
    if (saved !== null) {
      isDarkMode.value = JSON.parse(saved)
    } else {
      // Detectar preferencia del sistema
      isDarkMode.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    applyTheme()
  }

  // Aplicar el tema al documento
  const applyTheme = () => {
    if (isDarkMode.value) {
      document.documentElement.setAttribute('data-theme', 'dark')
    } else {
      document.documentElement.setAttribute('data-theme', 'light')
    }
    localStorage.setItem('darkMode', JSON.stringify(isDarkMode.value))
  }

  // Toggle entre modos
  const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value
    applyTheme()
  }

  // Cargar tema al inicializar
  loadTheme()

  // Observar cambios
  watch(isDarkMode, applyTheme)

  return {
    isDarkMode,
    toggleDarkMode,
    loadTheme
  }
}
