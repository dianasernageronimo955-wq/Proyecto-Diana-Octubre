package com.mycompany.proyectorestaurante.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorSimple extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private final int alineacion;
    private final Font fuente;

    public RenderizadorSimple(int alineacion, Font fuente) {
        this.alineacion = alineacion;
        this.fuente = fuente;
    }

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
            boolean foco, int fila, int columna) {
        Object dato = valor instanceof Double ? Formatos.moneda((Double) valor) : valor;
        JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(tabla, dato, seleccionado, foco, fila, columna);
        Color fondo = seleccionado ? Estilo.SELECCION : (fila % 2 == 0 ? Estilo.TARJETA : Estilo.FILA_ALT);
        etiqueta.setBackground(fondo);
        etiqueta.setOpaque(true);
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.BORDE),
                BorderFactory.createEmptyBorder(0, 14, 0, 14)));
        etiqueta.setHorizontalAlignment(alineacion);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(seleccionado ? Estilo.TITULO : Estilo.TEXTO);
        return etiqueta;
    }
}
