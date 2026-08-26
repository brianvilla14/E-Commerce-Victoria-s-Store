package com.victoriastore.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "`detalles_ventas`")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`iddetalles_ventas`")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "`idventas.fk`", nullable = false)
    @JsonBackReference
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "`idproducto.fk`", nullable = false)
    private Producto producto;

    @Column(name = "`cantidad`")
    private Integer cantidad;

    @Column(name = "`precio_unit`", precision = 10, scale = 2)
    private BigDecimal precioUnit;

    public DetalleVenta() {}

    public DetalleVenta(Venta venta, Producto producto, Integer cantidad, BigDecimal precioUnit) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(BigDecimal precioUnit) {
        this.precioUnit = precioUnit;
    }
}
