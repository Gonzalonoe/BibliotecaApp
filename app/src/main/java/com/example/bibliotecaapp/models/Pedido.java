package com.example.bibliotecaapp.models;

import java.io.Serializable;

public class Pedido implements Serializable {
    private int id;
    private int usuarioId;
    private Libro libro;
    private String tituloSolicitado;
    private String fechaPedido;         // 🔹 antes Date
    private String fechaVencimiento;    // 🔹 antes Date
    private String fechaDevolucion;     // 🔹 antes Date
    private int estado;
    private String observaciones;
    private Usuario usuario;            // 🔹 Agregamos para mostrar detalles

    // --- Getters ---
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public Libro getLibro() { return libro; }
    public String getTituloSolicitado() { return tituloSolicitado; }
    public String getFechaPedido() { return fechaPedido; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public String getFechaDevolucion() { return fechaDevolucion; }
    public int getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
    public Usuario getUsuario() { return usuario; }

    // --- Setters ---
    public void setEstado(int estado) { this.estado = estado; }
}