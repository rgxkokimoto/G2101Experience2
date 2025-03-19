package com.example.g2int101experience.ui.perfil;

import java.util.List;

public class Usuario {

    private String email;
    private String nombre;
    private String imagen;
    private List<String> experienciasCompletadas;

    // Constructor vacío necesario para Firebase
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String email, String nombre, String imagen, List<String> experienciasCompletadas) {
        this.email = email;
        this.nombre = nombre;
        this.imagen = imagen;
        this.experienciasCompletadas = experienciasCompletadas;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<String> getExperienciasCompletadas() {
        return experienciasCompletadas;
    }

    public void setExperienciasCompletadas(List<String> experienciasCompletadas) {
        this.experienciasCompletadas = experienciasCompletadas;
    }
}
