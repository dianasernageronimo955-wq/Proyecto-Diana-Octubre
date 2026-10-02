package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.Estado;
import com.mycompany.proyectorestaurante.modelo.Mesa;
import com.mycompany.proyectorestaurante.modelo.Pedido;
import com.mycompany.proyectorestaurante.servicio.PedidoService;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MesaTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"Mesa", "Estado", "Total"};

    private final PedidoService servicio;
    private List<Mesa> mesas = new ArrayList<Mesa>();

    public MesaTableModel(PedidoService servicio) {
        this.servicio = servicio;
    }

    public void actualizar() {
        this.mesas = servicio.getMesas();
        fireTableDataChanged();
    }

    public Mesa getMesa(int fila) {
        if (fila < 0 || fila >= mesas.size()) {
            return null;
        }
        return mesas.get(fila);
    }

    public Estado getEstado(int fila) {
        Mesa mesa = getMesa(fila);
        return mesa == null ? Estado.LIBRE : mesa.getEstado();
    }

    public int getFilaDeMesa(int numeroMesa) {
        for (int i = 0; i < mesas.size(); i++) {
            if (mesas.get(i).getNumero() == numeroMesa) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return mesas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        return columna == 2 ? Double.class : String.class;
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Mesa mesa = mesas.get(fila);
        switch (columna) {
            case 0:
                return String.valueOf(mesa.getNumero());
            case 1:
                return mesa.getEstado().getDescripcion();
            default:
                return obtenerTotal(mesa);
        }
    }

    private double obtenerTotal(Mesa mesa) {
        Pedido pedido = servicio.getPedido(mesa.getNumero());
        if (pedido == null) {
            return 0;
        }
        if (pedido.getCuentaFinal() != null) {
            return pedido.getCuentaFinal().getTotal();
        }
        return pedido.getTotal();
    }
}
