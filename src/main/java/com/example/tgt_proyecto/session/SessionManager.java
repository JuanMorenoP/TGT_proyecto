package com.example.tgt_proyecto.session;

public class SessionManager {
    private static String currentRol;

    // Método para establecer el rol del usuario actual
    public static void setCurrentRol(String rol) {
        currentRol = rol;
    }

    // Método para obtener el rol del usuario actual
    public static String getCurrentRol() {
        return currentRol;
    }

    // Método para verificar si el usuario es administrador
    public static boolean esAdministrador() {
        return "1".equals(currentRol);  // Si el rol es 1, es administrador
    }
}
