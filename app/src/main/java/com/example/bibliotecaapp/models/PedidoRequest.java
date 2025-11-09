package com.example.bibliotecaapp.models;

import java.io.Serializable;

public class PedidoRequest implements Serializable {
    private Integer libroId;
    private String tituloSolicitado;

    public PedidoRequest(Integer libroId, String tituloSolicitado) {
        this.libroId = libroId;
        this.tituloSolicitado = tituloSolicitado;
    }

    public Integer getLibroId() {
        return libroId;
    }

    public String getTituloSolicitado() {
        return tituloSolicitado;
    }
}
