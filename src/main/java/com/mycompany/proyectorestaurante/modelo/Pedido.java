package com.mycompany.proyectorestaurante.modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private final Mesa mesa;
    private final List<LineaPedido> lineas = new ArrayList<LineaPedido>();
    private Estado estado;
    private double cargoServicio;
    private DetalleCuenta cuentaFinal;

    public Pedido(Mesa mesa, double cargoServicio) {
        this.mesa = mesa;
        this.cargoServicio = cargoServicio;
        this.estado = Estado.ABIERTA;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getCargoServicio() {
        return cargoServicio;
    }

    public void setCargoServicio(double cargoServicio) {
        this.cargoServicio = cargoServicio;
    }

    public DetalleCuenta getCuentaFinal() {
        return cuentaFinal;
    }

    public void setCuentaFinal(DetalleCuenta cuentaFinal) {
        this.cuentaFinal = cuentaFinal;
    }

    public boolean estaPagado() {
        return estado == Estado.PAGADA;
    }

    public boolean estaVacio() {
        return lineas.isEmpty();
    }

    public int getCantidadDeItems() {
        int total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.getCantidad();
        }
        return total;
    }

    public LineaPedido buscarLinea(Producto producto) {
        for (LineaPedido linea : lineas) {
            if (linea.getProducto().getNombre().equals(producto.getNombre())) {
                return linea;
            }
        }
        return null;
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public void quitarLinea(LineaPedido linea) {
        lineas.remove(linea);
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (LineaPedido linea : lineas) {
            subtotal += linea.getSubtotal();
        }
        return subtotal;
    }

    public double getMontoCargoServicio() {
        return getSubtotal() * cargoServicio;
    }

    public double getTotal() {
        return getSubtotal() + getMontoCargoServicio();
    }
}
