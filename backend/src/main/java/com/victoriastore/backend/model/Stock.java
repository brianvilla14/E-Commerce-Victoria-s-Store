package com.victoriastore.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "`stock`")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idstock`")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "`idproductos.fk`", nullable = false)
    @JsonBackReference
    private Producto producto;

    @Column(name = "`disponible`", nullable = false)
    private Boolean disponible = true;

    @Column(name = "`cantidad`")
    private Integer cantidad = 0;

    public Stock() {}

    public Stock(Producto producto, Boolean disponible, Integer cantidad) {
        this.producto = producto;
        this.disponible = disponible;
        this.cantidad = cantidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
