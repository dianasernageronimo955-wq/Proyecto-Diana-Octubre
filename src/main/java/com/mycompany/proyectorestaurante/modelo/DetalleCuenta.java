package com.mycompany.proyectorestaurante.modelo;

public class DetalleCuenta {

    private final int numeroMesa;
    private final double subtotal;
    private final double porcentajeCargoServicio;
    private final double montoCargoServicio;
    private final double total;
    private double efectivoRecibido;

    public DetalleCuenta(int numeroMesa, double subtotal, double porcentajeCargoServicio,
            double montoCargoServicio, double total) {
        this.numeroMesa = numeroMesa;
        this.subtotal = subtotal;
        this.porcentajeCargoServicio = porcentajeCargoServicio;
        this.montoCargoServicio = montoCargoServicio;
        this.total = total;
        this.efectivoRecibido = 0;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getPorcentajeCargoServicio() {
        return porcentajeCargoServicio;
    }

    public double getMontoCargoServicio() {
        return montoCargoServicio;
    }

    public double getTotal() {
        return total;
    }

    public double getEfectivoRecibido() {
        return efectivoRecibido;
    }

    public void setEfectivoRecibido(double efectivoRecibido) {
        this.efectivoRecibido = efectivoRecibido;
    }

    public double getCambio() {
        return efectivoRecibido - total;
    }
}
