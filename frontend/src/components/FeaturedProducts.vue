<template>
  <section class="featured">
    <div class="featured-container">
      <h2>Productos Destacados</h2>
      
      <div class="products-grid">
        <article v-for="product in products" :key="product.id" class="product-card">
          <div class="product-image">{{ product.icon }}</div>
          <h3>{{ product.name }}</h3>
          <p class="product-description">{{ product.description }}</p>
          <div class="product-footer">
            <span class="price">${{ product.price }}</span>
            <button class="add-btn" @click="addToCart(product)">Agregar</button>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '../services/api'
import { useCart } from '../composables/useCart'

const { addToCart } = useCart()

const icons = ['🌸', '👜', '💎', '👠', '☂️', '⏱️', '✨', '👗']
const products = ref([])
const loading = ref(true)

const loadProducts = async () => {
  try {
    loading.value = true
    let data = await api.getProductos()
    
    // Si la base de datos está vacía, poblamos datos iniciales automáticamente
    if (!data || data.length === 0) {
      await api.poblarDatosIniciales()
      data = await api.getProductos()
    }

    products.value = data.map((p, idx) => ({
      id: p.id,
      name: p.nombre,
      description: p.descripcion,
      price: p.precio,
      icon: icons[idx % icons.length]
    }))
  } catch (error) {
    console.warn('Backend no disponible o error de conexión, usando datos locales:', error)
    products.value = [
      { id: 1, name: 'Perfume Elegancia', description: 'Aroma exclusivo y duradero', price: 89.99, icon: '🌸' },
      { id: 2, name: 'Bolsa Premium', description: 'Diseño minimalista y funcional', price: 129.99, icon: '👜' },
      { id: 3, name: 'Joyería Artesanal', description: 'Piezas únicas de plata', price: 149.99, icon: '💎' },
      { id: 4, name: 'Zapatos Confort', description: 'Comodidad para todo el día', price: 99.99, icon: '👠' },
      { id: 5, name: 'Sombrilla Elegante', description: 'Protección con estilo', price: 59.99, icon: '☂️' },
      { id: 6, name: 'Cinturón Sofisticado', description: 'Detalle que marca diferencia', price: 74.99, icon: '⏱️' },
    ]
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.featured {
  background: linear-gradient(180deg, var(--bg-primary) 0%, var(--bg-secondary) 50%, var(--bg-tertiary) 100%);
  padding: 5rem 2rem;
  position: relative;
  transition: background 0.3s ease;
}

.featured::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 200px;
  background: linear-gradient(180deg, transparent 0%, rgba(0, 188, 212, 0.05) 100%);
  pointer-events: none;
}

.featured-container {
  max-width: 1200px;
  margin: 0 auto;
}

.featured-container h2 {
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 3rem;
  color: var(--text-primary);
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  transition: color 0.3s ease;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
}

.product-card {
  background: var(--card-bg);
  border-radius: 15px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 2px 10px rgba(240, 212, 230, 0.2);
  border: 2px solid var(--border-color);
  display: flex;
  flex-direction: column;
  gap: 1rem;
  position: relative;
  overflow: hidden;
}

.product-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #ff6b9d, #00bcd4);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 16px 40px rgba(255, 107, 157, 0.3);
  border-color: #ff6b9d;
}

.product-card:hover::before {
  opacity: 1;
}

.product-image {
  font-size: 3rem;
  background: linear-gradient(135deg, #f0d4e6 0%, #e6d4f7 50%, #d4f0f7 100%);
  padding: 2rem;
  border-radius: 10px;
  animation: pulseGradient 3s ease-in-out infinite;
}

@keyframes pulseGradient {
  0%, 100% { background: linear-gradient(135deg, #f0d4e6 0%, #e6d4f7 50%, #d4f0f7 100%); }
  50% { background: linear-gradient(135deg, #f0d4e6 0%, #e6d4f7 50%, #d4f0f7 100%); filter: brightness(1.05); }
}

.product-card h3 {
  font-size: 1.2rem;
  color: var(--text-primary);
  margin: 0.5rem 0;
  transition: color 0.3s ease;
}

.product-description {
  color: var(--text-secondary);
  font-size: 0.9rem;
  flex-grow: 1;
  transition: color 0.3s ease;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  border-top: 1px solid #f0f0f0;
  padding-top: 1rem;
}

.price {
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.add-btn {
  background: linear-gradient(135deg, #00bcd4 0%, #00a8cc 100%);
  color: white;
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  font-size: 0.9rem;
  box-shadow: 0 2px 8px rgba(0, 188, 212, 0.2);
}

.add-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 188, 212, 0.4);
  background: linear-gradient(135deg, #00a8cc 0%, #0095b0 100%);
}

@media (max-width: 768px) {
  .featured-container h2 {
    font-size: 2rem;
  }

  .products-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
  }

  .product-card {
    padding: 1.5rem;
  }
}
</style>
