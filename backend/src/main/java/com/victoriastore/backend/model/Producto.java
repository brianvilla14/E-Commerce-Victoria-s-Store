package com.victoriastore.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "`productos`")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idproductos`")
    private Integer id;

    @Column(name = "`nombre`", length = 100)
    private String nombre;

    @Column(name = "`marca`", length = 45)
    private String marca;

    @Column(name = "`descripcion`", columnDefinition = "LONGTEXT")
    private String descripcion;

    @Column(name = "`precio`", precision = 10, scale = 2)
    private BigDecimal precio;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Stock stock;

    public Producto() {}

    public Producto(String nombre, String marca, String descripcion, BigDecimal precio) {
        this.nombre = nombre;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        if (stock != null) {
            stock.setProducto(this);
        }
    }
}
