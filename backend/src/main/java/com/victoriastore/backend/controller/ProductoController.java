package com.victoriastore.backend.controller;

import com.victoriastore.backend.model.Producto;
import com.victoriastore.backend.model.Stock;
import com.victoriastore.backend.repository.ProductoRepository;
import com.victoriastore.backend.repository.StockRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {

    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;

    public ProductoController(ProductoRepository productoRepository, StockRepository stockRepository) {
        this.productoRepository = productoRepository;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class ProductoRequest {
        public String nombre;
        public String marca;
        public String descripcion;
        public BigDecimal precio;
        public Integer stock;
        public Boolean disponible;
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody ProductoRequest req) {
        String error = validarProducto(req, true);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", error));
        }

        Producto producto = new Producto(req.nombre, req.marca, req.descripcion, req.precio);
        int cantidad = req.stock != null ? req.stock : 0;
        boolean disponible = req.disponible != null ? req.disponible : cantidad > 0;
        Stock stock = new Stock(producto, disponible, cantidad);
        producto.setStock(stock);

        Producto guardado = productoRepository.save(producto);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoRequest req) {
        String error = validarProducto(req, false);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", error));
        }

        return productoRepository.findById(id).map(prod -> {
            if (req.nombre != null) prod.setNombre(req.nombre);
            if (req.marca != null) prod.setMarca(req.marca);
            if (req.descripcion != null) prod.setDescripcion(req.descripcion);
            if (req.precio != null) prod.setPrecio(req.precio);

            if (prod.getStock() != null) {
                if (req.stock != null) prod.getStock().setCantidad(req.stock);
                if (req.disponible != null) {
                    prod.getStock().setDisponible(req.disponible);
                } else if (req.stock != null) {
                    prod.getStock().setDisponible(req.stock > 0);
                }
            } else if (req.stock != null || req.disponible != null) {
                int cantidad = req.stock != null ? req.stock : 0;
                boolean disponible = req.disponible != null ? req.disponible : cantidad > 0;
                Stock stock = new Stock(prod, disponible, cantidad);
                prod.setStock(stock);
            }

            return ResponseEntity.ok(productoRepository.save(prod));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (productoRepository.existsById(id)) {
            try {
                productoRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(409).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> poblarDatosIniciales() {
        if (productoRepository.count() == 0) {
            List<ProductoRequest> demo = Arrays.asList(
                createReq("Perfume Elegancia", "Victoria's Store", "Aroma exclusivo floral y duradero", new BigDecimal("89.99"), 45),
                createReq("Bolsa Premium", "Victoria's Store", "Diseño minimalista y funcional en cuero", new BigDecimal("129.99"), 12),
                createReq("Joyería Artesanal", "Victoria's Store", "Piezas únicas de plata y circonio", new BigDecimal("149.99"), 8),
                createReq("Zapatos Confort", "Victoria's Store", "Comodidad y elegancia para todo el día", new BigDecimal("99.99"), 20),
                createReq("Sombrilla Elegante", "Victoria's Store", "Protección UV y lluvia con estilo refinado", new BigDecimal("59.99"), 15),
                createReq("Cinturón Sofisticado", "Victoria's Store", "Detalle de lujo que marca la diferencia", new BigDecimal("74.99"), 30)
            );

            for (ProductoRequest req : demo) {
                crearProducto(req);
            }
            return ResponseEntity.ok(Map.of("mensaje", "Datos iniciales creados exitosamente", "insertados", demo.size()));
        }
        return ResponseEntity.ok(Map.of("mensaje", "La base de datos ya contiene productos", "total", productoRepository.count()));
    }

    private ProductoRequest createReq(String nombre, String marca, String descripcion, BigDecimal precio, int stock) {
        ProductoRequest r = new ProductoRequest();
        r.nombre = nombre;
        r.marca = marca;
        r.descripcion = descripcion;
        r.precio = precio;
        r.stock = stock;
        r.disponible = stock > 0;
        return r;
    }

    private String validarProducto(ProductoRequest req, boolean crear) {
        if (req == null) return "Los datos del producto son requeridos";
        if (crear && isBlank(req.nombre)) return "El nombre del producto es requerido";
        if (req.nombre != null && isBlank(req.nombre)) return "El nombre del producto no puede estar vacío";
        if (crear && req.precio == null) return "El precio del producto es requerido";
        if (req.precio != null && req.precio.compareTo(BigDecimal.ZERO) <= 0) return "El precio debe ser mayor a 0";
        if (req.stock != null && req.stock < 0) return "El stock no puede ser negativo";
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
