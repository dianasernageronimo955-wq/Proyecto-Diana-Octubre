package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.Estado;
import com.mycompany.proyectorestaurante.ui.componentes.EtiquetaRedondeada;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class RenderizadorEstado extends JPanel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private final EtiquetaRedondeada etiqueta = new EtiquetaRedondeada("", Estilo.ACENTO_CLARO, Estilo.ACENTO_OSCURO);

    public RenderizadorEstado() {
        setLayout(new BorderLayout());
        setOpaque(true);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        add(etiqueta, BorderLayout.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
            boolean foco, int fila, int columna) {
        MesaTableModel modelo = (MesaTableModel) tabla.getModel();
        Estado estado = modelo.getMesa(fila).getEstado();
        Color[] colores = Estilo.coloresEstado(estado);
        Color fondo = seleccionado ? Estilo.SELECCION : (fila % 2 == 0 ? Estilo.TARJETA : Estilo.FILA_ALT);

        setBackground(fondo);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.BORDE));
        etiqueta.setText(estado.getDescripcion());
        etiqueta.setColores(colores[0], colores[1]);
        return this;
    }
}
