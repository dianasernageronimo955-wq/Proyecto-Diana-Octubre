package com.mycompany.proyectorestaurante.ui.componentes;

import com.mycompany.proyectorestaurante.ui.Estilo;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int radio;
    private final Color fondo;
    private final Color borde;
    private final Color fondoDegradado;

    public PanelRedondeado(int radio, Color fondo) {
        this(radio, fondo, null, null);
    }

    public PanelRedondeado(int radio, Color fondo, Color borde) {
        this(radio, fondo, borde, null);
    }

    public PanelRedondeado(int radio, Color fondo, Color borde, Color fondoDegradado) {
        this.radio = radio;
        this.fondo = fondo;
        this.borde = borde;
        this.fondoDegradado = fondoDegradado;
        setOpaque(false);
    }

    public PanelRedondeado margen(int valor) {
        setBorder(BorderFactory.createEmptyBorder(valor, valor, valor, valor));
        return this;
    }

    public PanelRedondeado margen(int arriba, int izquierda, int abajo, int derecha) {
        setBorder(BorderFactory.createEmptyBorder(arriba, izquierda, abajo, derecha));
        return this;
    }

    public PanelRedondeado bordeInferior(Color color) {
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color));
        return this;
    }

    public Color getColorFondo() {
        return fondo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ancho = getWidth();
        int alto = getHeight();
        if (fondoDegradado != null) {
            g2.setPaint(new GradientPaint(0, 0, fondo, ancho, alto, fondoDegradado));
        } else {
            g2.setPaint(fondo);
        }
        g2.fillRoundRect(0, 0, ancho - 1, alto - 1, radio, radio);
        if (borde != null) {
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, ancho - 1, alto - 1, radio, radio);
        }
        g2.dispose();
    }

    @Override
    public Color getBackground() {
        return fondo;
    }

    public Color getColorBorde() {
        return borde == null ? Estilo.BORDE : borde;
    }
}
