const API_URL = import.meta.env.VITE_API_URL || '/api'

async function request(path, options = {}) {
  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers
    }
  })

  const contentType = res.headers.get('content-type') || ''
  const payload = contentType.includes('application/json') ? await res.json() : null

  if (!res.ok) {
    throw new Error(payload?.mensaje || payload?.message || `Error HTTP ${res.status}`)
  }

  return payload
}

export const api = {
  // Productos
  async getProductos() {
    return request('/productos')
  },

  async getProductoById(id) {
    return request(`/productos/${id}`)
  },

  async crearProducto(producto) {
    return request('/productos', {
      method: 'POST',
      body: JSON.stringify(producto)
    })
  },

  async actualizarProducto(id, producto) {
    return request(`/productos/${id}`, {
      method: 'PUT',
      body: JSON.stringify(producto)
    })
  },

  async eliminarProducto(id) {
    await request(`/productos/${id}`, {
      method: 'DELETE'
    })
    return true
  },

  async poblarDatosIniciales() {
    return request('/productos/seed', { method: 'POST' })
  },

  // Ventas / Pedidos
  async getVentas() {
    return request('/ventas')
  },

  async crearVenta(venta) {
    return request('/ventas', {
      method: 'POST',
      body: JSON.stringify(venta)
    })
  },

  async actualizarEstadoVenta(id, estado) {
    return request(`/ventas/${id}/estado`, {
      method: 'PATCH',
      body: JSON.stringify({ estado })
    })
  },

  // Estadísticas Dashboard
  async getStats() {
    return request('/stats')
  }
}
