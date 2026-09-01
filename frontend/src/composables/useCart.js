import { ref, computed } from 'vue'
import { api } from '../services/api'

const cartItems = ref([])
const isCartOpen = ref(false)
const isSubmitting = ref(false)
const orderSuccess = ref(null)

const hasStockLimit = (item) => Number.isFinite(item.stock)

export function useCart() {
  const addToCart = (product) => {
    const existing = cartItems.value.find(item => item.id === product.id)
    const stock = Number(product.stock)

    if (Number.isFinite(stock) && stock <= 0) {
      return
    }

    if (existing) {
      if (hasStockLimit(existing) && existing.quantity >= existing.stock) {
        return
      }
      existing.quantity += 1
    } else {
      cartItems.value.push({
        id: product.id,
        name: product.name,
        price: Number(product.price),
        icon: product.icon || '🛍️',
        stock: Number.isFinite(stock) ? stock : null,
        quantity: 1
      })
    }
    isCartOpen.value = true
  }

  const updateQuantity = (productId, delta) => {
    const item = cartItems.value.find(i => i.id === productId)
    if (item) {
      const nextQuantity = item.quantity + delta
      item.quantity = hasStockLimit(item) ? Math.min(nextQuantity, item.stock) : nextQuantity
      if (item.quantity <= 0) {
        removeFromCart(productId)
      }
    }
  }

  const removeFromCart = (productId) => {
    cartItems.value = cartItems.value.filter(i => i.id !== productId)
  }

  const clearCart = () => {
    cartItems.value = []
  }

  const totalCount = computed(() => {
    return cartItems.value.reduce((acc, item) => acc + item.quantity, 0)
  })

  const totalPrice = computed(() => {
    return cartItems.value.reduce((acc, item) => acc + (item.price * item.quantity), 0)
  })

  const checkout = async (customerData) => {
    if (cartItems.value.length === 0) return

    isSubmitting.value = true
    try {
      const ventaReq = {
        clienteNombre: customerData.nombre,
        clienteEmail: customerData.email,
        clienteTelefono: customerData.telefono,
        clienteDomicilio: customerData.domicilio,
        metodoPago: customerData.metodoPago || 'Tarjeta de Crédito',
        items: cartItems.value.map(item => ({
          productoId: item.id,
          cantidad: item.quantity
        }))
      }

      const order = await api.crearVenta(ventaReq)
      orderSuccess.value = order
      clearCart()
      return order
    } finally {
      isSubmitting.value = false
    }
  }

  return {
    cartItems,
    isCartOpen,
    isSubmitting,
    orderSuccess,
    addToCart,
    updateQuantity,
    removeFromCart,
    clearCart,
    totalCount,
    totalPrice,
    checkout
  }
}
