package com.example.bibliotecaapp.models;

import java.io.Serializable;

public class Reporte implements Serializable {

    private int id;
    private String tituloLibro;
    private String sinopsis;
    private String imagenPortada;
    private String usuarioNombre;
    private String usuarioEmail;
    private String fecha;

    private int estado;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}
