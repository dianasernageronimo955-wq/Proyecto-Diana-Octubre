package com.mycompany.proyectorestaurante.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

public final class Accesibilidad {

    private static final float ESCALA_NORMAL = 1.0f;
    private static final float ESCALA_GRANDE = 1.3f;
    private static final int FILA_TABLA = 38;
    private static final int ANCHO_BARRA = 14;
    private static float escala = ESCALA_NORMAL;

    private Accesibilidad() {
    }

    public static boolean esTextoGrande() {
        return escala > ESCALA_NORMAL;
    }

    public static float escalaActual() {
        return escala;
    }

    public static int alturaFila() {
        return Math.round(FILA_TABLA * escala);
    }

    public static int anchoBarra() {
        return Math.max(Math.round(ANCHO_BARRA * escala), 11);
    }

    public static void alternarTextoGrande(Component raiz) {
        float anterior = escala;
        escala = esTextoGrande() ? ESCALA_NORMAL : ESCALA_GRANDE;
        redimensionar(raiz, anterior, escala);
        for (java.awt.Window ventana : java.awt.Window.getWindows()) {
            if (ventana instanceof JDialog && ventana.isDisplayable()) {
                redimensionar(ventana, anterior, escala);
                ventana.pack();
            }
        }
    }

    public static void redimensionar(Component raiz, float escalaAnterior, float escalaNueva) {
        if (raiz == null) {
            return;
        }
        ajustar(raiz, escalaAnterior, escalaNueva);
        raiz.invalidate();
        raiz.validate();
        raiz.repaint();
    }

    private static void ajustar(Component componente, float escalaAnterior, float escalaNueva) {
        Font fuente = componente.getFont();
        if (fuente != null && escalaAnterior > 0) {
            float logico = fuente.getSize2D() / escalaAnterior;
            float tamano = Math.round(logico * escalaNueva);
            componente.setFont(fuente.deriveFont(tamano));
        }
        if (componente instanceof JTable) {
            ((JTable) componente).setRowHeight(Math.round(FILA_TABLA * escalaNueva));
        }
        if (componente instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) componente;
            scroll.getVerticalScrollBar().setPreferredSize(new Dimension(
                    Math.max(Math.round(ANCHO_BARRA * escalaNueva), 11),
                    scroll.getVerticalScrollBar().getPreferredSize().height));
        }
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                ajustar(hijo, escalaAnterior, escalaNueva);
            }
        }
    }

    public static void describir(JComponent componente, String nombre, String descripcion) {
        componente.setToolTipText(descripcion);
        componente.getAccessibleContext().setAccessibleName(nombre);
        componente.getAccessibleContext().setAccessibleDescription(descripcion);
    }

    public static void cerrarConEscape(final JDialog dialogo) {
        dialogo.getRootPane().registerKeyboardAction(new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
