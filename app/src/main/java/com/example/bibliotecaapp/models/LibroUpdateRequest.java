package com.example.bibliotecaapp.models;

public class LibroUpdateRequest {

    private String titulo;
    private String autor;
    private Integer anio;
    private int stock;
    private String descripcion;

    public LibroUpdateRequest(String titulo, String autor, Integer anio, int stock, String descripcion) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.stock = stock;
        this.descripcion = descripcion;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
