<template>
  <div v-if="isCartOpen" class="cart-backdrop" @click.self="isCartOpen = false">
    <div class="cart-modal">
      <div class="cart-header">
        <h2>🛒 Tu Carrito</h2>
        <button class="close-btn" @click="isCartOpen = false">✕</button>
      </div>

      <!-- Pantalla de Éxito al Comprar -->
      <div v-if="orderSuccess" class="success-view">
        <div class="success-icon">🎉</div>
        <h3>¡Compra Realizada con Éxito!</h3>
        <p>Tu pedido <strong>#{{ orderSuccess.id }}</strong> ha sido registrado en la base de datos MySQL.</p>
        <p class="order-total">Total pagado: ${{ Number(orderSuccess.total).toFixed(2) }}</p>
        <button class="btn-primary" @click="orderSuccess = null; isCartOpen = false">Seguir Comprando</button>
      </div>

      <!-- Lista de Productos en el Carrito -->
      <div v-else-if="cartItems.length > 0" class="cart-content">
        <div class="cart-items">
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <span class="item-icon">{{ item.icon }}</span>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="item-price">${{ item.price.toFixed(2) }} c/u</p>
              <p v-if="item.stock !== null" class="item-stock">Stock: {{ item.stock }}</p>
            </div>
            <div class="item-qty">
              <button @click="updateQuantity(item.id, -1)">-</button>
              <span>{{ item.quantity }}</span>
              <button :disabled="item.stock !== null && item.quantity >= item.stock" @click="updateQuantity(item.id, 1)">+</button>
            </div>
            <button class="remove-btn" @click="removeFromCart(item.id)">🗑️</button>
          </div>
        </div>

        <div class="cart-summary">
          <span>Total:</span>
          <span class="total-amount">${{ totalPrice.toFixed(2) }}</span>
        </div>

        <!-- Formulario de Checkout -->
        <form @submit.prevent="handleCheckout" class="checkout-form">
          <h4>Datos para la entrega</h4>
          <div class="form-row">
            <input v-model="form.nombre" required placeholder="Tu Nombre completo" class="input-field" />
            <input v-model="form.email" type="email" required placeholder="tu@email.com" class="input-field" />
          </div>
          <div class="form-row">
            <input v-model="form.telefono" placeholder="Teléfono" class="input-field" />
            <input v-model="form.domicilio" placeholder="Dirección de Entrega" class="input-field" />
          </div>
          <div class="form-row">
            <select v-model="form.metodoPago" class="input-field">
              <option value="Tarjeta de Crédito">Tarjeta de Crédito</option>
              <option value="Tarjeta de Débito">Tarjeta de Débito</option>
              <option value="Efectivo / Transferencia">Efectivo / Transferencia</option>
            </select>
          </div>
          <button type="submit" :disabled="isSubmitting" class="btn-checkout">
            {{ isSubmitting ? 'Procesando en BD...' : `Comprar Ahora ($${totalPrice.toFixed(2)})` }}
          </button>
        </form>
      </div>

      <!-- Carrito Vacío -->
      <div v-else class="empty-cart">
        <div class="empty-icon">🛍️</div>
        <p>Tu carrito está vacío</p>
        <button class="btn-primary" @click="isCartOpen = false">Ver Productos</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useCart } from '../composables/useCart'

const {
  cartItems,
  isCartOpen,
  isSubmitting,
  orderSuccess,
  updateQuantity,
  removeFromCart,
  totalPrice,
  checkout
} = useCart()

const form = ref({
  nombre: '',
  email: '',
  telefono: '',
  domicilio: '',
  metodoPago: 'Tarjeta de Crédito'
})

const handleCheckout = async () => {
  try {
    await checkout(form.value)
  } catch (error) {
    alert('Error al procesar la compra: ' + error.message)
  }
}
</script>

<style scoped>
.cart-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: flex-end;
  z-index: 1000;
  backdrop-filter: blur(3px);
}

.cart-modal {
  width: 100%;
  max-width: 460px;
  height: 100%;
  background: var(--card-bg, #ffffff);
  color: var(--text-primary, #333333);
  display: flex;
  flex-direction: column;
  box-shadow: -5px 0 25px rgba(0, 0, 0, 0.25);
  animation: slideLeft 0.3s ease-out;
  overflow-y: auto;
}

@keyframes slideLeft {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}

.cart-header {
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--border-color, #eee);
}

.cart-header h2 {
  margin: 0;
  font-size: 1.4rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.4rem;
  cursor: pointer;
  color: var(--text-primary);
}

.cart-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  flex: 1;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  max-height: 220px;
  overflow-y: auto;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 0.8rem;
  background: var(--bg-secondary, #f8f9fa);
  border-radius: 10px;
}

.item-icon {
  font-size: 1.8rem;
}

.item-info {
  flex: 1;
}

.item-info h4 {
  margin: 0 0 0.2rem 0;
  font-size: 0.95rem;
}

.item-price {
  margin: 0;
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.item-stock {
  margin: 0.15rem 0 0;
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.item-qty {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.item-qty button {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  border: 1px solid #ccc;
  background: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.item-qty button:disabled {
  cursor: not-allowed;
  opacity: 0.45;
}

.remove-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
}

.cart-summary {
  display: flex;
  justify-content: space-between;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 1rem 0;
  border-top: 2px solid var(--border-color, #eee);
  border-bottom: 2px solid var(--border-color, #eee);
}

.total-amount {
  color: #ff6b9d;
}

.checkout-form {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.checkout-form h4 {
  margin: 0 0 0.2rem 0;
}

.form-row {
  display: flex;
  gap: 0.6rem;
}

.input-field {
  flex: 1;
  padding: 0.65rem 0.8rem;
  border-radius: 8px;
  border: 1px solid var(--border-color, #ccc);
  background: var(--bg-primary, #fff);
  color: var(--text-primary);
  font-size: 0.85rem;
}

.btn-checkout {
  margin-top: 0.5rem;
  padding: 0.9rem;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, #00bcd4 0%, #00a8cc 100%);
  color: white;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.btn-checkout:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 188, 212, 0.4);
}

.empty-cart, .success-view {
  padding: 3rem 1.5rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.empty-icon, .success-icon {
  font-size: 3.5rem;
}

.order-total {
  font-size: 1.2rem;
  font-weight: bold;
  color: #28a745;
}

.btn-primary {
  padding: 0.8rem 1.5rem;
  border-radius: 25px;
  border: none;
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  color: white;
  font-weight: bold;
  cursor: pointer;
}
</style>
