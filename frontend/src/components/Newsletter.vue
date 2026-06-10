<template>
  <section class="newsletter">
    <div class="newsletter-content">
      <h2>Suscríbete a Nuestro Newsletter</h2>
      <p>Recibe ofertas exclusivas y las últimas novedades directamente en tu correo</p>
      
      <form @submit.prevent="handleSubscribe" class="newsletter-form">
        <input 
          v-model="email"
          type="email" 
          placeholder="tu@correo.com" 
          required
          aria-label="Correo electrónico"
        >
        <button type="submit" class="subscribe-btn">Suscribirse</button>
      </form>
      
      <p v-if="message" :class="['message', messageType]">{{ message }}</p>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')
const message = ref('')
const messageType = ref('')

const handleSubscribe = () => {
  if (!email.value) {
    message.value = 'Por favor ingresa tu correo'
    messageType.value = 'error'
    return
  }
  
  message.value = '¡Gracias por suscribirte!'
  messageType.value = 'success'
  email.value = ''
  
  setTimeout(() => {
    message.value = ''
  }, 3000)
}
</script>

<style scoped>
.newsletter {
  background: linear-gradient(135deg, #f0d4e6 0%, #e6d4f7 50%, #d4f0f7 100%);
  padding: 4rem 2rem;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.newsletter::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 107, 157, 0.08) 0%, transparent 70%);
  animation: rotateBg 20s linear infinite;
  pointer-events: none;
}

@keyframes rotateBg {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.newsletter-content {
  position: relative;
  z-index: 1;
}

.newsletter-content {
  max-width: 600px;
  margin: 0 auto;
}

.newsletter-content h2 {
  font-size: 2rem;
  color: var(--text-primary);
  margin-bottom: 1rem;
  transition: color 0.3s ease;
}

.newsletter-content > p {
  color: var(--text-secondary);
  margin-bottom: 2rem;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.newsletter-form {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

input[type="email"] {
  flex: 1;
  padding: 1rem;
  border: 2px solid #ff6b9d;
  border-radius: 25px;
  font-size: 0.95rem;
  outline: none;
  transition: all 0.3s ease;
  background: var(--card-bg);
  color: var(--text-primary);
  box-shadow: 0 2px 8px rgba(255, 107, 157, 0.1);
}

input[type="email"]:focus {
  box-shadow: 0 0 0 4px rgba(255, 107, 157, 0.3), inset 0 0 0 1px rgba(255, 107, 157, 0.1);
  border-color: #e85a8c;
  transform: translateY(-2px);
}

.subscribe-btn {
  background: linear-gradient(135deg, #ff6b9d 0%, #e85a8c 100%);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  box-shadow: 0 4px 15px rgba(255, 107, 157, 0.3);
  position: relative;
  overflow: hidden;
}

.subscribe-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.subscribe-btn:hover::after {
  width: 300px;
  height: 300px;
}

.subscribe-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(255, 107, 157, 0.4);
}

.message {
  font-size: 0.95rem;
  margin-top: 1rem;
  padding: 1rem;
  border-radius: 10px;
  font-weight: 500;
}

.message.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.message.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

@media (max-width: 768px) {
  .newsletter-form {
    flex-direction: column;
  }

  .subscribe-btn {
    width: 100%;
  }

  .newsletter-content h2 {
    font-size: 1.5rem;
  }
}
</style>
