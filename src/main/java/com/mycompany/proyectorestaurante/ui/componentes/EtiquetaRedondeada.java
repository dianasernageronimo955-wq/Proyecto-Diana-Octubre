package com.mycompany.proyectorestaurante.ui.componentes;

import com.mycompany.proyectorestaurante.ui.Estilo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class EtiquetaRedondeada extends JLabel {

    private static final long serialVersionUID = 1L;

    private Color fondo;
    private Color textoColor;
    private final int radio;

    public EtiquetaRedondeada(String texto, Color fondo, Color textoColor) {
        this(texto, fondo, textoColor, 14);
    }

    public EtiquetaRedondeada(String texto, Color fondo, Color textoColor, int radio) {
        super(texto);
        this.fondo = fondo;
        this.textoColor = textoColor;
        this.radio = radio;
        setFont(Estilo.fuenteNegrita(12));
        setForeground(textoColor);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
    }

    public void setColores(Color fondo, Color textoColor) {
        this.fondo = fondo;
        this.textoColor = textoColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();
        setForeground(textoColor);
        super.paintComponent(g);
    }
}
