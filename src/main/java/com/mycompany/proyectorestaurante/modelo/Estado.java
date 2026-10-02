package com.mycompany.proyectorestaurante.modelo;

public enum Estado {

    LIBRE("Libre"),
    ABIERTA("Abierta"),
    ATENDIDA("Atendida"),
    PAGADA("Pagada");

    private final String descripcion;

    private Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
