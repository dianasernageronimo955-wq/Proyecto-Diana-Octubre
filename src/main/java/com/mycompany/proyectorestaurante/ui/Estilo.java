package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.Estado;
import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class Estilo {

    public static final Color FONDO = new Color(0xFBF4F7);
    public static final Color TARJETA = Color.WHITE;
    public static final Color BORDE = new Color(0xE4CCD9);
    public static final Color TITULO = new Color(0x4A1F33);
    public static final Color TEXTO = new Color(0x372A33);
    public static final Color TEXTO_SUAVE = new Color(0x77666F);
    public static final Color ACENTO = new Color(0xC0507F);
    public static final Color ACENTO_OSCURO = new Color(0x93355C);
    public static final Color ACENTO_CLARO = new Color(0xFAE4EE);
    public static final Color CABECERA_INICIO = new Color(0xFDF1F7);
    public static final Color CABECERA_FIN = new Color(0xF8DDEB);
    public static final Color MENTA = new Color(0x24614A);
    public static final Color MENTA_CLARO = new Color(0xDDEDE4);
    public static final Color ORO = new Color(0x8A5F08);
    public static final Color ORO_CLARO = new Color(0xFAEDCF);
    public static final Color AZUL = new Color(0x3B4B85);
    public static final Color AZUL_CLARO = new Color(0xE1E6F6);
    public static final Color LILA = new Color(0x5C3E7E);
    public static final Color LILA_CLARO = new Color(0xEBE3F6);
    public static final Color NEUTRO_CLARO = new Color(0xF0E6EC);
    public static final Color FILA_ALT = new Color(0xFAF4F8);
    public static final Color SELECCION = new Color(0xF7D9E8);
    public static final Color FOCO = new Color(0x9E3D68);
    public static final Color BLANCO = Color.WHITE;

    private static final String FUENTE_TITULO = "Georgia";
    private static final String FUENTE_TEXTO = "Segoe UI";

    private Estilo() {
    }

    public static Font fuenteTitulo(int tamano) {
        return new Font(FUENTE_TITULO, Font.BOLD, Math.round(tamano * Accesibilidad.escalaActual()));
    }

    public static Font fuenteTexto(int tamano) {
        return new Font(FUENTE_TEXTO, Font.PLAIN, Math.round(tamano * Accesibilidad.escalaActual()));
    }

    public static Font fuenteNegrita(int tamano) {
        return new Font(FUENTE_TEXTO, Font.BOLD, Math.round(tamano * Accesibilidad.escalaActual()));
    }

    public static Border relleno(int valor) {
        return BorderFactory.createEmptyBorder(valor, valor, valor, valor);
    }

    public static Border relleno(int arriba, int izquierda, int abajo, int derecha) {
        return BorderFactory.createEmptyBorder(arriba, izquierda, abajo, derecha);
    }

    public static JLabel etiquetaSeccion(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuenteTitulo(18));
        etiqueta.setForeground(TITULO);
        return etiqueta;
    }

    public static JLabel etiquetaSuave(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuenteTexto(13));
        etiqueta.setForeground(TEXTO_SUAVE);
        return etiqueta;
    }

    public static BotonRedondeado botonPrimario(String texto) {
        BotonRedondeado boton = new BotonRedondeado(texto, ACENTO, BLANCO);
        boton.setFont(fuenteNegrita(15));
        boton.setPreferredSize(new Dimension(130, 42));
        return boton;
    }

    public static BotonRedondeado botonSecundario(String texto) {
        BotonRedondeado boton = new BotonRedondeado(texto, ACENTO_CLARO, ACENTO_OSCURO);
        boton.setFont(fuenteNegrita(15));
        boton.setPreferredSize(new Dimension(130, 42));
        return boton;
    }

    public static BotonRedondeado botonSuave(String texto) {
        BotonRedondeado boton = new BotonRedondeado(texto, NEUTRO_CLARO, TEXTO);
        boton.setFont(fuenteNegrita(15));
        boton.setPreferredSize(new Dimension(130, 42));
        return boton;
    }

    public static PanelRedondeado tarjeta(int radio, int relleno) {
        return new PanelRedondeado(radio, TARJETA, BORDE).margen(relleno);
    }

    public static Color[] coloresEstado(Estado estado) {
        switch (estado) {
            case LIBRE:
                return new Color[]{MENTA_CLARO, MENTA};
            case ABIERTA:
                return new Color[]{ACENTO_CLARO, ACENTO_OSCURO};
            case ATENDIDA:
                return new Color[]{AZUL_CLARO, AZUL};
            default:
                return new Color[]{LILA_CLARO, LILA};
        }
    }

    public static void configurarTabla(JTable tabla) {
        tabla.setFont(fuenteTexto(15));
        tabla.setForeground(TEXTO);
        tabla.setBackground(TARJETA);
        tabla.setRowHeight(Accesibilidad.alturaFila());
        tabla.setShowGrid(false);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(TITULO);
        tabla.setFillsViewportHeight(true);
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(fuenteNegrita(12));
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(10, 36));
        encabezado.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE));
        encabezado.setBackground(TARJETA);
        encabezado.setForeground(TEXTO_SUAVE);
        encabezado.setOpaque(false);
        encabezado.setDefaultRenderer(new RenderizadorEncabezado());
    }

    public static void scrollDelgado(JScrollBar barra) {
        barra.setPreferredSize(new Dimension(11, 0));
        barra.setOpaque(false);
        barra.setUnitIncrement(20);
    }

    public static void efectoMano(JComponent componente) {
        componente.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                componente.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                componente.repaint();
            }
        });
    }

    private static class RenderizadorEncabezado extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                boolean foco, int fila, int columna) {
            JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(tabla, valor, seleccionado, foco,
                    fila, columna);
            etiqueta.setHorizontalAlignment(columna == 1 ? SwingConstants.CENTER : SwingConstants.LEFT);
            etiqueta.setFont(fuenteNegrita(12));
            etiqueta.setForeground(TEXTO_SUAVE);
            etiqueta.setOpaque(false);
            etiqueta.setBorder(relleno(8, 16, 8, 16));
            return etiqueta;
        }
    }
}
