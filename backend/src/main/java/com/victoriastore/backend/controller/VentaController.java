package com.victoriastore.backend.controller;

import com.victoriastore.backend.model.Cliente;
import com.victoriastore.backend.model.DetalleVenta;
import com.victoriastore.backend.model.Producto;
import com.victoriastore.backend.model.Venta;
import com.victoriastore.backend.repository.ClienteRepository;
import com.victoriastore.backend.repository.ProductoRepository;
import com.victoriastore.backend.repository.VentaRepository;
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
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest req) {
        Cliente cliente = clienteRepository.findByCorreo(req.clienteEmail)
                .orElseGet(() -> {
                    Cliente nuevo = new Cliente();
                    nuevo.setNombre(req.clienteNombre != null ? req.clienteNombre : "Cliente");
                    nuevo.setCorreo(req.clienteEmail);
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

        if (req.items != null) {
            for (DetalleItemRequest itemReq : req.items) {
                Producto prod = productoRepository.findById(itemReq.productoId).orElse(null);
                if (prod != null) {
                    int cant = itemReq.cantidad != null ? itemReq.cantidad : 1;
                    BigDecimal subtotal = prod.getPrecio().multiply(BigDecimal.valueOf(cant));
                    total = total.add(subtotal);

                    DetalleVenta detalle = new DetalleVenta(venta, prod, cant, prod.getPrecio());
                    detalles.add(detalle);

                    // Reducir stock si existe
                    if (prod.getStock() != null && prod.getStock().getCantidad() >= cant) {
                        prod.getStock().setCantidad(prod.getStock().getCantidad() - cant);
                    }
                }
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
}
