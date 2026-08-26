<template>
  <header class="header">
    <div class="header-container">
      <div class="logo">
        <RouterLink to="/" class="logo-link">
          <h1>Victoria's Store</h1>
        </RouterLink>
      </div>
      
      <nav class="nav">
        <RouterLink to="/" class="nav-link">Inicio</RouterLink>
        <a href="#" class="nav-link">Catálogo</a>
        <a href="#" class="nav-link">Ofertas</a>
        <a href="#" class="nav-link">Contacto</a>
      </nav>

      <div class="header-actions">
        <button class="search-btn" aria-label="Buscar productos">
          <span>🔍</span>
        </button>
        <button class="cart-btn" @click="isCartOpen = true" aria-label="Ver carrito">
          <span>🛒</span>
          <span v-if="totalCount > 0" class="cart-count">{{ totalCount }}</span>
        </button>
        <button class="theme-btn" @click="toggleDarkMode" :aria-label="isDarkMode ? 'Modo claro' : 'Modo oscuro'">
          <span>{{ isDarkMode ? '☀️' : '🌙' }}</span>
        </button>
        <RouterLink to="/login" class="user-btn" aria-label="Mi cuenta">
          <span>👤</span>
        </RouterLink>
      </div>
    </div>
  </header>
</template>

<script setup>
import { RouterLink } from 'vue-router'
import { useDarkMode } from '../composables/useDarkMode'
import { useCart } from '../composables/useCart'

const { isDarkMode, toggleDarkMode } = useDarkMode()
const { totalCount, isCartOpen } = useCart()
</script>

<style scoped>
.header {
  background: var(--header-bg);
  border-bottom: 3px solid;
  border-image: linear-gradient(90deg, #ff6b9d, #00bcd4) 1;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 4px 20px rgba(255, 107, 157, 0.12);
  transition: background 0.3s ease;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
}

.logo h1 {
  font-size: 1.8rem;
  background: linear-gradient(135deg, #ff6b9d 0%, #e85a8c 50%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 700;
  letter-spacing: -0.5px;
  animation: logoGlow 3s ease-in-out infinite;
}

@keyframes logoGlow {
  0%, 100% { filter: drop-shadow(0 0 0 rgba(255, 107, 157, 0.3)); }
  50% { filter: drop-shadow(0 0 8px rgba(255, 107, 157, 0.5)); }
}

.nav {
  display: flex;
  gap: 2rem;
  flex: 1;
  justify-content: center;
}

.nav-link {
  text-decoration: none;
  color: #555;
  font-weight: 500;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 3px;
  background: linear-gradient(90deg, #ff6b9d, #00bcd4);
  transition: width 0.3s ease;
  border-radius: 2px;
  box-shadow: 0 0 10px rgba(255, 107, 157, 0.5);
}

.nav-link:hover::after {
  width: 100%;
}

.logo-link {
  text-decoration: none;
  cursor: pointer;
}

.logo-link h1 {
  font-size: 1.8rem;
  background: linear-gradient(135deg, #ff6b9d 0%, #e85a8c 50%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 700;
  letter-spacing: -0.5px;
  animation: logoGlow 3s ease-in-out infinite;
}

.user-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  transition: all 0.3s ease;
  position: relative;
  padding: 0.5rem;
  border-radius: 50%;
  text-decoration: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-btn:hover {
  background: linear-gradient(135deg, rgba(255, 107, 157, 0.15), rgba(0, 188, 212, 0.1));
  transform: scale(1.15);
  box-shadow: 0 4px 12px rgba(255, 107, 157, 0.25);
}

.nav-link:hover::after {
  width: 100%;
}

.header-actions {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  transition: all 0.3s ease;
  position: relative;
  padding: 0.5rem;
  border-radius: 50%;
}

button:hover {
  background: linear-gradient(135deg, rgba(255, 107, 157, 0.15), rgba(0, 188, 212, 0.1));
  transform: scale(1.15);
  box-shadow: 0 4px 12px rgba(255, 107, 157, 0.25);
}

.cart-btn {
  position: relative;
}

.cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ff6b9d;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: bold;
}

.theme-btn {
  transition: all 0.3s ease, transform 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.theme-btn:hover {
  transform: scale(1.15) rotate(20deg);
}

@media (max-width: 768px) {
  .header-container {
    padding: 0 1rem;
    gap: 1rem;
  }

  .nav {
    display: none;
  }

  .logo h1 {
    font-size: 1.3rem;
  }
}
</style>
