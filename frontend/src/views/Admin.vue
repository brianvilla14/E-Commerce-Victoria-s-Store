<template>
  <div class="admin-layout">
    <div class="admin-sidebar">
      <div class="sidebar-header">
        <h2>Victoria's Store</h2>
        <p class="admin-badge">Admin Panel</p>
      </div>

      <nav class="sidebar-nav">
        <button
          v-for="item in navItems"
          :key="item.id"
          @click="currentTab = item.id"
          :class="['nav-item', { active: currentTab === item.id }]"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span>{{ item.label }}</span>
        </button>
      </nav>

      <div class="sidebar-footer">
        <div class="user-info">
          <div class="user-avatar">👤</div>
          <div>
            <p class="user-name">{{ user?.email }}</p>
            <p class="user-role">{{ user?.role }}</p>
          </div>
        </div>
        <button @click="handleLogout" class="logout-btn">Cerrar Sesión</button>
      </div>
    </div>

    <div class="admin-main">
      <div class="admin-header">
        <h1>{{ getTabTitle }}</h1>
        <div class="header-actions">
          <input v-model.trim="searchQuery" type="search" placeholder="Buscar..." class="search-box" />
          <button class="action-btn">⚙️ Configuración</button>
        </div>
      </div>

      <!-- Dashboard Tab -->
      <div v-if="currentTab === 'dashboard'" class="tab-content">
        <div class="stats-grid">
          <div v-for="stat in stats" :key="stat.id" class="stat-card">
            <div class="stat-icon">{{ stat.icon }}</div>
            <div class="stat-info">
              <p class="stat-label">{{ stat.label }}</p>
              <p class="stat-value">{{ stat.value }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Tab -->
      <div v-if="currentTab === 'products'" class="tab-content">
        <div class="products-header">
          <h2>Gestión de Productos</h2>
          <button class="add-btn" @click="openModal()">+ Nuevo Producto</button>
        </div>

        <table class="products-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Marca</th>
              <th>Precio</th>
              <th>Stock</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="filteredProducts.length === 0">
              <td colspan="7" style="text-align: center; padding: 2rem;">No hay productos registrados</td>
            </tr>
            <tr v-for="product in filteredProducts" :key="product.id">
              <td>{{ product.id }}</td>
              <td>{{ product.name }}</td>
              <td>{{ product.brand || '-' }}</td>
              <td>${{ product.price }}</td>
              <td>{{ product.stock }}</td>
              <td>
                <span :class="['status-badge', product.stock > 0 ? 'active' : 'inactive']">
                  {{ product.stock > 0 ? 'En Stock' : 'Sin Stock' }}
                </span>
              </td>
              <td>
                <button class="action-link" @click="openModal(product)">Editar</button>
                <button class="action-link delete" @click="deleteProduct(product.id)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Orders Tab -->
      <div v-if="currentTab === 'orders'" class="tab-content">
        <div class="orders-header">
          <h2>Pedidos Recientes</h2>
          <select v-model="orderFilter" class="filter-select">
            <option value="Todos">Todos</option>
            <option value="Pendiente">Pendiente</option>
            <option value="Enviado">Enviado</option>
            <option value="Entregado">Entregado</option>
            <option value="Cancelado">Cancelado</option>
          </select>
        </div>

        <div class="orders-list">
          <div v-if="filteredOrders.length === 0" style="padding: 2rem; text-align: center;">
            No hay pedidos registrados.
          </div>
          <div v-for="order in filteredOrders" :key="order.id" class="order-card">
            <div class="order-header">
              <span class="order-id">Orden #{{ order.id }}</span>
              <span :class="['order-status', statusClass(order.status)]">{{ order.status }}</span>
            </div>
            <div class="order-details">
              <p><strong>Cliente:</strong> {{ order.customer }}</p>
              <p><strong>Total:</strong> ${{ order.total }}</p>
              <p><strong>Método de Pago:</strong> {{ order.paymentMethod || 'No especificado' }}</p>
              <p><strong>Fecha:</strong> {{ formatDate(order.date) }}</p>
            </div>
            <div class="order-actions" style="margin-top: 1rem; display: flex; gap: 0.5rem; align-items: center;">
              <label style="font-size: 0.85rem;">Cambiar Estado:</label>
              <select :value="order.status" @change="changeOrderStatus(order.id, $event.target.value)" class="filter-select" style="padding: 0.3rem 0.6rem;">
                <option value="Pendiente">Pendiente</option>
                <option value="Enviado">Enviado</option>
                <option value="Entregado">Entregado</option>
                <option value="Cancelado">Cancelado</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <!-- Analytics Tab -->
      <div v-if="currentTab === 'analytics'" class="tab-content">
        <div class="analytics-grid">
          <div class="analytics-card">
            <h3>Ingresos Registrados</h3>
            <p class="big-number">${{ statsValues.ingresosTotales.toFixed(2) }}</p>
            <p class="trend positive">📈 En base de datos MySQL</p>
          </div>
          <div class="analytics-card">
            <h3>Total Pedidos</h3>
            <p class="big-number">{{ statsValues.totalPedidos }}</p>
            <p class="trend positive">📦 Registrados en sistema</p>
          </div>
          <div class="analytics-card">
            <h3>Total Productos</h3>
            <p class="big-number">{{ statsValues.totalProductos }}</p>
            <p class="trend neutral">🏷️ En catálogo</p>
          </div>
          <div class="analytics-card">
            <h3>Total Clientes</h3>
            <p class="big-number">{{ statsValues.totalClientes }}</p>
            <p class="trend positive">👥 Usuarios en BD</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Nuevo / Editar Producto -->
    <div v-if="showProductModal" class="modal-backdrop">
      <div class="modal-card">
        <h3>{{ editingProduct ? 'Editar Producto' : 'Nuevo Producto' }}</h3>
        <form @submit.prevent="saveProduct">
          <div class="form-group">
            <label>Nombre:</label>
            <input v-model="productForm.nombre" required class="form-input" placeholder="Nombre del producto" />
          </div>
          <div class="form-group">
            <label>Marca:</label>
            <input v-model="productForm.marca" class="form-input" placeholder="Victoria's Store" />
          </div>
          <div class="form-group">
            <label>Precio ($):</label>
            <input v-model.number="productForm.precio" type="number" step="0.01" required class="form-input" placeholder="99.99" />
          </div>
          <div class="form-group">
            <label>Stock disponible:</label>
            <input v-model.number="productForm.stock" type="number" required class="form-input" placeholder="10" />
          </div>
          <div class="form-group">
            <label>Descripción:</label>
            <textarea v-model="productForm.descripcion" class="form-input" rows="3" placeholder="Detalles del producto..."></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" @click="closeModal" class="btn-cancel">Cancelar</button>
            <button type="submit" class="btn-save">{{ editingProduct ? 'Actualizar' : 'Guardar' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import { api } from '../services/api'

const router = useRouter()
const { user, logout } = useAuth()
const currentTab = ref('dashboard')
const orderFilter = ref('Todos')
const searchQuery = ref('')

const navItems = [
  { id: 'dashboard', label: 'Dashboard', icon: '📊' },
  { id: 'products', label: 'Productos', icon: '📦' },
  { id: 'orders', label: 'Pedidos', icon: '🛍️' },
  { id: 'analytics', label: 'Analítica', icon: '📈' },
]

const statsValues = ref({
  ingresosTotales: 0,
  totalPedidos: 0,
  totalProductos: 0,
  totalClientes: 0,
})

const stats = computed(() => [
  { id: 1, label: 'Ingresos Totales', value: `$${statsValues.value.ingresosTotales.toFixed(2)}`, icon: '💰' },
  { id: 2, label: 'Pedidos Registrados', value: statsValues.value.totalPedidos.toString(), icon: '📦' },
  { id: 3, label: 'Total Productos', value: statsValues.value.totalProductos.toString(), icon: '🏷️' },
  { id: 4, label: 'Total Clientes', value: statsValues.value.totalClientes.toString(), icon: '👥' },
])

const products = ref([])
const orders = ref([])

// Modal state
const showProductModal = ref(false)
const editingProduct = ref(null)
const productForm = ref({
  nombre: '',
  marca: "Victoria's Store",
  precio: 0,
  stock: 10,
  descripcion: ''
})

const loadData = async () => {
  try {
    const [prodsData, ordersData, statsData] = await Promise.allSettled([
      api.getProductos(),
      api.getVentas(),
      api.getStats()
    ])

    if (prodsData.status === 'fulfilled' && prodsData.value) {
      if (prodsData.value.length === 0) {
        await api.poblarDatosIniciales()
        const refreshed = await api.getProductos()
        products.value = mapProducts(refreshed)
      } else {
        products.value = mapProducts(prodsData.value)
      }
    }

    if (ordersData.status === 'fulfilled' && ordersData.value) {
      orders.value = ordersData.value.map(o => ({
        id: o.id,
        customer: o.cliente ? `${o.cliente.nombre || ''} ${o.cliente.apellido || ''}`.trim() || o.cliente.correo : 'Cliente',
        total: o.total,
        status: o.estado || 'Pendiente',
        paymentMethod: o.metodoPago,
        date: o.fechaPedido || new Date().toISOString()
      }))
    }

    if (statsData.status === 'fulfilled' && statsData.value) {
      statsValues.value = {
        ingresosTotales: Number(statsData.value.ingresosTotales || 0),
        totalPedidos: Number(statsData.value.totalPedidos || 0),
        totalProductos: Number(statsData.value.totalProductos || products.value.length),
        totalClientes: Number(statsData.value.totalClientes || 0),
      }
    }
  } catch (e) {
    console.warn('Error al cargar datos desde API:', e)
  }
}

const mapProducts = (list) => {
  return list.map(p => ({
    id: p.id,
    name: p.nombre,
    brand: p.marca,
    price: p.precio,
    stock: p.stock ? p.stock.cantidad : 0,
    status: p.stock && p.stock.cantidad > 0 ? 'active' : 'inactive',
    description: p.descripcion
  }))
}

onMounted(() => {
  loadData()
})

const filteredOrders = computed(() => {
  if (orderFilter.value === 'Todos') return orders.value
  return orders.value.filter(o => o.status.toLowerCase() === orderFilter.value.toLowerCase())
})

const filteredProducts = computed(() => {
  const term = searchQuery.value.toLowerCase()
  if (!term) return products.value

  return products.value.filter(product => {
    return [product.name, product.brand, product.description]
      .filter(Boolean)
      .some(value => value.toLowerCase().includes(term))
  })
})

const openModal = (product = null) => {
  if (product) {
    editingProduct.value = product
    productForm.value = {
      nombre: product.name,
      marca: product.brand || "Victoria's Store",
      precio: product.price,
      stock: product.stock,
      descripcion: product.description || ''
    }
  } else {
    editingProduct.value = null
    productForm.value = {
      nombre: '',
      marca: "Victoria's Store",
      precio: 0,
      stock: 10,
      descripcion: ''
    }
  }
  showProductModal.value = true
}

const closeModal = () => {
  showProductModal.value = false
  editingProduct.value = null
}

const saveProduct = async () => {
  try {
    if (productForm.value.precio <= 0) {
      alert('El precio debe ser mayor a 0')
      return
    }

    if (productForm.value.stock < 0) {
      alert('El stock no puede ser negativo')
      return
    }

    if (editingProduct.value) {
      await api.actualizarProducto(editingProduct.value.id, productForm.value)
    } else {
      await api.crearProducto(productForm.value)
    }
    closeModal()
    await loadData()
  } catch (error) {
    alert('Error al guardar producto: ' + error.message)
  }
}

const deleteProduct = async (id) => {
  if (confirm('¿Estás seguro de eliminar este producto?')) {
    try {
      await api.eliminarProducto(id)
      await loadData()
    } catch (error) {
      alert('Error al eliminar producto: ' + error.message)
    }
  }
}

const changeOrderStatus = async (id, nuevoEstado) => {
  try {
    await api.actualizarEstadoVenta(id, nuevoEstado)
    await loadData()
  } catch (error) {
    alert('Error al actualizar estado: ' + error.message)
  }
}

const getTabTitle = computed(() => {
  const item = navItems.find(i => i.id === currentTab.value)
  return item?.label || 'Dashboard'
})

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('es-ES')
}

const statusClass = (status) => {
  return String(status || 'Pendiente').toLowerCase()
}

const handleLogout = () => {
  logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: var(--bg-secondary);
  transition: background 0.3s ease;
}

.admin-sidebar {
  width: 280px;
  background: linear-gradient(180deg, var(--bg-secondary) 0%, var(--bg-tertiary) 100%);
  color: var(--text-secondary);
  display: flex;
  flex-direction: column;
  border-right: 2px solid;
  border-image: linear-gradient(180deg, #ff6b9d, #00bcd4) 1;
  overflow-y: auto;
  transition: background 0.3s ease, color 0.3s ease;
}

.sidebar-header {
  padding: 2rem 1.5rem;
  border-bottom: 1px solid rgba(255, 107, 157, 0.2);
}

.sidebar-header h2 {
  background: linear-gradient(135deg, #ff6b9d 0%, #00bcd4 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
}

.admin-badge {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

.sidebar-nav {
  flex: 1;
  padding: 1.5rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nav-item {
  background: none;
  border: none;
  color: #aaa;
  padding: 1rem 1.5rem;
  text-align: left;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.95rem;
  font-weight: 500;
}

.nav-item:hover {
  background: rgba(255, 107, 157, 0.1);
  color: #ff6b9d;
}

.nav-item.active {
  background: linear-gradient(90deg, rgba(255, 107, 157, 0.2), rgba(0, 188, 212, 0.1));
  color: white;
  border-left: 3px solid #ff6b9d;
  padding-left: calc(1.5rem - 3px);
}

.nav-icon {
  font-size: 1.2rem;
}

.sidebar-footer {
  padding: 1.5rem;
  border-top: 1px solid rgba(255, 107, 157, 0.2);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.user-info {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #ff6b9d, #00bcd4);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

.user-name {
  font-size: 0.85rem;
  font-weight: 600;
  margin: 0;
}

.user-role {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  margin: 0;
  transition: color 0.3s ease;
}

.logout-btn {
  background: linear-gradient(135deg, #ff6b9d, #e85a8c);
  color: white;
  border: none;
  padding: 0.75rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 157, 0.3);
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.admin-header {
  background: var(--card-bg);
  padding: 2rem;
  border-bottom: 2px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
  transition: background 0.3s ease, border-color 0.3s ease;
}

.admin-header h1 {
  color: var(--text-primary);
  margin: 0;
  font-size: 1.8rem;
  transition: color 0.3s ease;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-box {
  padding: 0.75rem 1rem;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  font-size: 0.9rem;
  min-width: 200px;
  transition: all 0.3s ease;
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.search-box:focus {
  outline: none;
  border-color: #ff6b9d;
  box-shadow: 0 0 0 3px rgba(255, 107, 157, 0.1);
}

.action-btn {
  background: linear-gradient(135deg, #00bcd4, #00a8cc);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 188, 212, 0.3);
}

.tab-content {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--card-bg);
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: flex;
  gap: 1rem;
  align-items: center;
  transition: all 0.3s ease;
  border-left: 4px solid #ff6b9d;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(255, 107, 157, 0.15);
}

.stat-icon {
  font-size: 2.5rem;
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin: 0;
  transition: color 0.3s ease;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  transition: color 0.3s ease;
}

.products-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.add-btn {
  background: linear-gradient(135deg, #ff6b9d, #e85a8c);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 157, 0.3);
}

.products-table {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-collapse: collapse;
  transition: background 0.3s ease;
  width: 100%;
}

.products-table thead {
  background: linear-gradient(135deg, #f0d4e6 0%, #d4f0f7 100%);
}

[data-theme="dark"] .products-table thead {
  background: linear-gradient(135deg, #2d2d2d 0%, #1a1a1a 100%);
}

.products-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: var(--text-primary);
  transition: color 0.3s ease;
}

.products-table td {
  padding: 1rem;
  border-top: 1px solid var(--border-color);
  color: var(--text-primary);
  transition: all 0.3s ease;
  padding: 1rem;
}

.products-table tr:hover {
  background: var(--bg-secondary);
  transition: background 0.3s ease;
}

.status-badge {
  display: inline-block;
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.inactive {
  background: #f8d7da;
  color: #721c24;
}

.action-link {
  background: none;
  border: none;
  color: #00bcd4;
  cursor: pointer;
  text-decoration: underline;
  font-size: 0.85rem;
  margin-right: 1rem;
  transition: color 0.3s ease;
}

.action-link:hover {
  color: #0095b0;
}

.action-link.delete {
  color: #ff6b9d;
}

.action-link.delete:hover {
  color: #e85a8c;
}

.orders-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.filter-select {
  padding: 0.75rem 1rem;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--card-bg);
  color: var(--text-primary);
}

.filter-select:focus {
  outline: none;
  border-color: #ff6b9d;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.order-card {
  background: var(--card-bg);
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border-left: 4px solid #00bcd4;
}

.order-card:hover {
  transform: translateX(4px);
  box-shadow: 0 8px 20px rgba(0, 188, 212, 0.15);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.order-id {
  font-weight: 700;
  color: var(--text-primary);
  transition: color 0.3s ease;
}

.order-status {
  display: inline-block;
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  background: #d4edda;
  color: #155724;
}

.order-status.pendiente {
  background: #fff3cd;
  color: #856404;
}

.order-status.enviado {
  background: #d1ecf1;
  color: #0c5460;
}

.order-status.entregado {
  background: #d4edda;
  color: #155724;
}

.order-status.cancelado {
  background: #f8d7da;
  color: #721c24;
}

.order-details p {
  margin: 0.5rem 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.analytics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
}

.analytics-card {
  background: var(--card-bg);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-top: 4px solid #ff6b9d;
  transition: background 0.3s ease;
}

.analytics-card h3 {
  color: var(--text-secondary);
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 1rem;
  transition: color 0.3s ease;
}

.big-number {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0.5rem 0;
  transition: color 0.3s ease;
}

.trend {
  font-size: 0.9rem;
  margin: 0.5rem 0 0 0;
}

.trend.positive {
  color: #28a745;
}

.trend.negative {
  color: #dc3545;
}

.trend.neutral {
  color: #ffc107;
}

@media (max-width: 1024px) {
  .admin-sidebar {
    width: 220px;
  }

  .admin-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .header-actions {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .admin-layout {
    flex-direction: column;
  }

  .admin-sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 2px solid;
    border-image: linear-gradient(90deg, #ff6b9d, #00bcd4) 1;
  }

  .sidebar-nav {
    flex-direction: row;
    overflow-x: auto;
  }

  .nav-item {
    flex-shrink: 0;
  }

  .products-table {
    font-size: 0.85rem;
  }
}

/* Modal Styling */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-card {
  background: var(--card-bg, #ffffff);
  color: var(--text-primary, #333333);
  padding: 2rem;
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  border: 1px solid var(--border-color, #eee);
}

.modal-card h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: var(--text-primary);
  font-size: 1.4rem;
}

.form-group {
  margin-bottom: 1.2rem;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.form-group label {
  font-size: 0.9rem;
  font-weight: 600;
}

.form-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-color, #ccc);
  border-radius: 8px;
  background: var(--bg-primary, #fff);
  color: var(--text-primary, #333);
  font-size: 0.95rem;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #00bcd4;
  box-shadow: 0 0 0 3px rgba(0, 188, 212, 0.2);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.btn-cancel {
  padding: 0.7rem 1.4rem;
  border: 1px solid #ccc;
  background: transparent;
  color: var(--text-primary);
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.btn-save {
  padding: 0.7rem 1.5rem;
  border: none;
  background: linear-gradient(135deg, #00bcd4 0%, #00a8cc 100%);
  color: white;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.btn-save:hover {
  opacity: 0.9;
}
</style>
