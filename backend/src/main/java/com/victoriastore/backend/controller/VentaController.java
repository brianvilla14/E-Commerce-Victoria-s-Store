package com.victoriastore.backend.controller;

import com.victoriastore.backend.model.Cliente;
import com.victoriastore.backend.model.DetalleVenta;
import com.victoriastore.backend.model.Producto;
import com.victoriastore.backend.model.Venta;
import com.victoriastore.backend.repository.ClienteRepository;
import com.victoriastore.backend.repository.ProductoRepository;
import com.victoriastore.backend.repository.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")

public class VentaController {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public VentaController(VentaRepository ventaRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Integer id) {
        return ventaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class DetalleItemRequest {
        public Integer productoId;
        public Integer cantidad;
    }

    public static class VentaRequest {
        public String clienteNombre;
        public String clienteEmail;
        public String clienteTelefono;
        public String clienteDomicilio;
        public String metodoPago;
        public List<DetalleItemRequest> items;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest req) {
        String error = validarVenta(req);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", error));
        }

        List<Producto> productos = new ArrayList<>();
        for (DetalleItemRequest itemReq : req.items) {
            Producto prod = productoRepository.findById(itemReq.productoId).orElse(null);
            if (prod == null) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Producto no encontrado: " + itemReq.productoId));
            }

            if (prod.getPrecio() == null || prod.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Producto sin precio válido: " + prod.getNombre()));
            }

            int cantidadDisponible = prod.getStock() != null && prod.getStock().getCantidad() != null
                    ? prod.getStock().getCantidad()
                    : 0;

            if (!Boolean.TRUE.equals(prod.getStock() != null ? prod.getStock().getDisponible() : false)
                    || cantidadDisponible < itemReq.cantidad) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Stock insuficiente para " + prod.getNombre()));
            }

            productos.add(prod);
        }

        Cliente cliente = clienteRepository.findByCorreo(req.clienteEmail.trim())
                .orElseGet(() -> {
                    Cliente nuevo = new Cliente();
                    nuevo.setNombre(req.clienteNombre.trim());
                    nuevo.setCorreo(req.clienteEmail.trim());
                    nuevo.setTelefono(req.clienteTelefono);
                    nuevo.setDomicilio(req.clienteDomicilio);
                    return clienteRepository.save(nuevo);
                });

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFechaPedido(LocalDateTime.now());
        venta.setMetodoPago(req.metodoPago != null ? req.metodoPago : "Tarjeta");
        venta.setEstado("Pendiente");

        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (int i = 0; i < req.items.size(); i++) {
            DetalleItemRequest itemReq = req.items.get(i);
            Producto prod = productos.get(i);
            int cant = itemReq.cantidad;
            BigDecimal subtotal = prod.getPrecio().multiply(BigDecimal.valueOf(cant));
            total = total.add(subtotal);

            DetalleVenta detalle = new DetalleVenta(venta, prod, cant, prod.getPrecio());
            detalles.add(detalle);

            if (prod.getStock() != null) {
                int nuevoStock = prod.getStock().getCantidad() - cant;
                prod.getStock().setCantidad(nuevoStock);
                prod.getStock().setDisponible(nuevoStock > 0);
            }
        }

        venta.setTotal(total);
        venta.setDetalles(detalles);

        Venta guardada = ventaRepository.save(venta);
        return ResponseEntity.ok(guardada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Venta> actualizarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return ventaRepository.findById(id).map(venta -> {
            String nuevoEstado = body.get("estado");
            if (nuevoEstado != null) {
                venta.setEstado(nuevoEstado);
            }
            return ResponseEntity.ok(ventaRepository.save(venta));
        }).orElse(ResponseEntity.notFound().build());
    }

    private String validarVenta(VentaRequest req) {
        if (req == null) return "Los datos de la venta son requeridos";
        if (isBlank(req.clienteNombre)) return "El nombre del cliente es requerido";
        if (isBlank(req.clienteEmail) || !req.clienteEmail.contains("@")) return "El email del cliente es inválido";
        if (req.items == null || req.items.isEmpty()) return "La venta debe tener al menos un producto";

        for (DetalleItemRequest item : req.items) {
            if (item.productoId == null) return "Todos los items deben tener productoId";
            if (item.cantidad == null || item.cantidad <= 0) return "La cantidad de cada producto debe ser mayor a 0";
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
