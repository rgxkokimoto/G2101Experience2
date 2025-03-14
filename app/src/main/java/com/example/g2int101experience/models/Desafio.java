package com.example.g2int101experience.models;

import android.widget.ImageView;

public class Desafio {
    String titulo , imgUlr;

    String id;

    // es necesario
    public Desafio() {}

    public Desafio(String titulo, String imgUlr) {
        this.titulo = titulo;
        this.imgUlr = imgUlr;
    }

    public Desafio(String titulo, String imgUlr, String id) {
        this.titulo = titulo;
        this.imgUlr = imgUlr;
        this.id = id;
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


}
