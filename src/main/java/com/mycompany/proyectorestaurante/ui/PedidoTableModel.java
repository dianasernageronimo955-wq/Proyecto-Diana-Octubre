package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.LineaPedido;
import com.mycompany.proyectorestaurante.modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class PedidoTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"Producto", "Cantidad", "Precio unitario", "Subtotal"};

    private List<LineaPedido> lineas = new ArrayList<LineaPedido>();

    public void actualizar(Pedido pedido) {
        this.lineas = pedido == null ? new ArrayList<LineaPedido>() : pedido.getLineas();
        fireTableDataChanged();
    }

    public LineaPedido getLinea(int fila) {
        if (fila < 0 || fila >= lineas.size()) {
            return null;
        }
        return lineas.get(fila);
    }

    @Override
    public int getRowCount() {
        return lineas.size();
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
        return columna == 1 ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        LineaPedido linea = lineas.get(fila);
        switch (columna) {
            case 0:
                return linea.getProducto().getNombre();
            case 1:
                return linea.getCantidad();
            case 2:
                return Formatos.moneda(linea.getPrecioUnitario());
            default:
                return Formatos.moneda(linea.getSubtotal());
        }
    }
}
