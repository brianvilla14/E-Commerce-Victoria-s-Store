const API_URL = 'http://localhost:8080/api';

export const api = {
  // Productos
  async getProductos() {
    const res = await fetch(`${API_URL}/productos`);
    if (!res.ok) throw new Error('Error al obtener productos');
    return await res.json();
  },

  async getProductoById(id) {
    const res = await fetch(`${API_URL}/productos/${id}`);
    if (!res.ok) throw new Error('Error al obtener producto');
    return await res.json();
  },

  async crearProducto(producto) {
    const res = await fetch(`${API_URL}/productos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });
    if (!res.ok) throw new Error('Error al crear producto');
    return await res.json();
  },

  async actualizarProducto(id, producto) {
    const res = await fetch(`${API_URL}/productos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });
    if (!res.ok) throw new Error('Error al actualizar producto');
    return await res.json();
  },

  async eliminarProducto(id) {
    const res = await fetch(`${API_URL}/productos/${id}`, {
      method: 'DELETE',
    });
    if (!res.ok) throw new Error('Error al eliminar producto');
    return true;
  },

  async poblarDatosIniciales() {
    const res = await fetch(`${API_URL}/productos/seed`, { method: 'POST' });
    return await res.json();
  },

  // Ventas / Pedidos
  async getVentas() {
    const res = await fetch(`${API_URL}/ventas`);
    if (!res.ok) throw new Error('Error al obtener pedidos');
    return await res.json();
  },

  async crearVenta(venta) {
    const res = await fetch(`${API_URL}/ventas`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(venta),
    });
    if (!res.ok) throw new Error('Error al registrar pedido');
    return await res.json();
  },

  async actualizarEstadoVenta(id, estado) {
    const res = await fetch(`${API_URL}/ventas/${id}/estado`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ estado }),
    });
    if (!res.ok) throw new Error('Error al actualizar estado del pedido');
    return await res.json();
  },

  // Estadísticas Dashboard
  async getStats() {
    const res = await fetch(`${API_URL}/stats`);
    if (!res.ok) throw new Error('Error al obtener estadísticas');
    return await res.json();
  },
};
