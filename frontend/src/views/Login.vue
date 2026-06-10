<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <h1 class="login-title">Victoria's Store</h1>
          <p class="login-subtitle">Acceso Administrativo</p>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="email" class="form-label">Correo Electrónico</label>
            <input
              v-model="email"
              id="email"
              type="email"
              placeholder="admin@victorias-store.com"
              class="form-input"
              required
              aria-label="Correo electrónico"
            />
          </div>

          <div class="form-group">
            <label for="password" class="form-label">Contraseña</label>
            <input
              v-model="password"
              id="password"
              type="password"
              placeholder="••••••••"
              class="form-input"
              required
              aria-label="Contraseña"
            />
          </div>

          <div v-if="error" class="error-message">
            <span>⚠️ {{ error }}</span>
          </div>

          <button
            type="submit"
            class="login-btn"
            :disabled="isLoading"
            :aria-busy="isLoading"
          >
            <span v-if="!isLoading">Iniciar Sesión</span>
            <span v-else>Cargando...</span>
          </button>
        </form>

        <div class="login-footer">
          <p class="demo-hint">
            💡 Demo: usa <strong>admin@test.com</strong> con cualquier contraseña
          </p>
        </div>
      </div>

      <div class="login-background">
        <div class="gradient-circle"></div>
        <div class="gradient-circle-2"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'

const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')
const isLoading = ref(false)
const { login } = useAuth()

const handleLogin = async () => {
  isLoading.value = true
  error.value = ''

  const success = await login(email.value, password.value)

  if (success) {
    router.push('/admin')
  } else {
    const { authError } = useAuth()
    error.value = authError.value || 'Error al iniciar sesión'
  }

  isLoading.value = false
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 50%, var(--bg-tertiary) 100%);
  overflow: hidden;
  transition: background 0.3s ease;
}

.login-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 100%;
  max-width: 420px;
  padding: 3rem 2rem;
  background: var(--card-bg);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(255, 107, 157, 0.15);
  position: relative;
  z-index: 10;
  border: 1px solid var(--border-color);
  transition: background 0.3s ease, border-color 0.3s ease;
}

.login-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.login-title {
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
}

.login-subtitle {
  color: var(--text-tertiary);
  font-size: 0.95rem;
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: color 0.3s ease;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: color 0.3s ease;
}

.form-input {
  padding: 1rem;
  border: 2px solid var(--border-color);
  border-radius: 10px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.form-input:focus {
  background: var(--card-bg);
  border-color: #ff6b9d;
  box-shadow: 0 0 0 4px rgba(255, 107, 157, 0.1);
  outline: none;
}

.form-input::placeholder {
  color: var(--text-tertiary);
}

.error-message {
  padding: 1rem;
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
  color: #721c24;
  font-size: 0.9rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-btn {
  background: linear-gradient(135deg, #ff6b9d 0%, #e85a8c 100%);
  color: white;
  border: none;
  padding: 1.1rem;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 1px;
  box-shadow: 0 4px 15px rgba(255, 107, 157, 0.3);
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.2);
  transition: left 0.4s ease;
}

.login-btn:hover:not(:disabled)::before {
  left: 100%;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 107, 157, 0.4);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-footer {
  margin-top: 2rem;
  text-align: center;
  padding-top: 1.5rem;
  border-top: 1px solid #f0f0f0;
}

.demo-hint {
  font-size: 0.85rem;
  color: #666;
  margin: 0;
}

.demo-hint strong {
  color: #ff6b9d;
  font-weight: 600;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.gradient-circle {
  position: absolute;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255, 107, 157, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  top: -100px;
  right: -100px;
  animation: float 6s ease-in-out infinite;
}

.gradient-circle-2 {
  position: absolute;
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(0, 188, 212, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  bottom: -80px;
  left: -80px;
  animation: float 8s ease-in-out infinite reverse;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(20px);
  }
}

@media (max-width: 768px) {
  .login-card {
    padding: 2rem 1.5rem;
  }

  .login-title {
    font-size: 1.5rem;
  }

  .login-subtitle {
    font-size: 0.85rem;
  }
}
</style>
