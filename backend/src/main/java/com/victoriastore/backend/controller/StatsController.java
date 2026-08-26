package com.victoriastore.backend.controller;

import com.victoriastore.backend.model.Venta;
import com.victoriastore.backend.repository.ClienteRepository;
import com.victoriastore.backend.repository.ProductoRepository;
import com.victoriastore.backend.repository.VentaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")

public class StatsController {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    public StatsController(VentaRepository ventaRepository, ProductoRepository productoRepository, ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        List<Venta> ventas = ventaRepository.findAll();
        
        BigDecimal ingresosTotales = ventas.stream()
                .filter(v -> v.getTotal() != null)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("ingresosTotales", ingresosTotales);
        stats.put("totalPedidos", ventas.size());
        stats.put("totalProductos", productoRepository.count());
        stats.put("totalClientes", clienteRepository.count());

        return ResponseEntity.ok(stats);
    }
}
