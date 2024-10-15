package com.example.tgt_proyecto.proveedores;

public class Proveedor {

    private int id;
    private String nombre;
    private String contacto;
    private String direccion;
    private String metodoPago;
    private String nitRut;

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor completo
    public Proveedor(int id, String nombre, String contacto, String direccion, String metodoPago, String nitRut) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
        this.nitRut = nitRut;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNitRut() {
        return nitRut;
    }

    public void setNitRut(String nitRut) {
        this.nitRut = nitRut;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", contacto='" + contacto + '\'' +
                ", direccion='" + direccion + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", nitRut='" + nitRut + '\'' +
                '}';
    }
}
