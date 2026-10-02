package com.mycompany.proyectorestaurante.modelo;

public class Mesa {

    private final int numero;
    private Estado estado;

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = Estado.LIBRE;
    }

    public int getNumero() {
        return numero;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean estaLibre() {
        return estado == Estado.LIBRE;
    }

    public boolean estaPagada() {
        return estado == Estado.PAGADA;
    }

    @Override
    public String toString() {
        return "Mesa " + numero + " (" + estado.getDescripcion() + ")";
    }
}
