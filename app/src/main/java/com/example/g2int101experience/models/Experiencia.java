package com.example.g2int101experience.models;

public class Experiencia {
    String titulo, imgUlr;
    String descripcion;
    String id;
    private boolean completada; // Nuevo atributo

    // es necesario
    public Experiencia() {}

    public Experiencia(String titulo, String imgUlr) {
        this.titulo = titulo;
        this.imgUlr = imgUlr;
        this.completada = false;
    }

    public Experiencia(String titulo, String imgUlr, String id) {
        this.titulo = titulo;
        this.imgUlr = imgUlr;
        this.id = id;
        this.completada = false;
    }

    public Experiencia(String titulo, String imgUlr, String descripcion, String id) {
        this.titulo = titulo;
        this.imgUlr = imgUlr;
        this.descripcion = descripcion;
        this.id = id;
        this.completada = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImgUlr() {
        return imgUlr;
    }

    public void setImgUlr(String imgUlr) {
        this.imgUlr = imgUlr;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Nuevos métodos para completada
    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}