package com.example.tgt_proyecto.usuarios;

public class Usuario {

    private int id;              // Identificador único del usuario
    private String nombre;       // Nombre del usuario
    private String apellido;     // Apellido del usuario
    private String usuario;      // Nombre de usuario
    private String contraseña;   // Contraseña del usuario
    private String rol;          // Rol del usuario como String

    // Constructor vacío
    public Usuario() {
    }

    // Constructor completo (incluye contraseña)
    public Usuario(int id, String nombre, String apellido, String usuario, String contraseña, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.contraseña = contraseña;  // Asegurarse de incluir la contraseña en el constructor
        this.rol = rol;  // El rol se maneja como String para mostrar el nombre del rol
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método toString para mostrar el usuario como texto en caso de necesidad
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contraseña='" + contraseña + '\'' +  // Asegúrate de no mostrar la contraseña si no es necesario.
                ", rol='" + rol + '\'' +
                '}';
    }
}
