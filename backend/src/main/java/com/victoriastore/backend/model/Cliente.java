package com.victoriastore.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "`clientes`")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idclientes`")
    private Integer id;

    @Column(name = "`nombre`", length = 45)
    private String nombre;

    @Column(name = "`apellido`", length = 45)
    private String apellido;

    @Column(name = "`domicilio`", length = 100)
    private String domicilio;

    @Column(name = "`telefono`", length = 20)
    private String telefono;

    @Column(name = "`correo`", length = 45)
    private String correo;

    public Cliente() {}

    public Cliente(String nombre, String apellido, String domicilio, String telefono, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.correo = correo;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
