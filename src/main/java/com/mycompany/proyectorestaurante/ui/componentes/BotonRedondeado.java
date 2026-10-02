package com.mycompany.proyectorestaurante.ui.componentes;

import com.mycompany.proyectorestaurante.ui.Estilo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class BotonRedondeado extends JButton {

    private static final long serialVersionUID = 1L;

    private final int radio;
    private final Color fondo;
    private final Color textoColor;
    private boolean sobreEl;

    public BotonRedondeado(String texto, Color fondo, Color textoColor) {
        this(texto, fondo, textoColor, 18);
    }

    public BotonRedondeado(String texto, Color fondo, Color textoColor, int radio) {
        super(texto);
        this.radio = radio;
        this.fondo = fondo;
        this.textoColor = textoColor;
        setFont(Estilo.fuenteNegrita(13));
        setForeground(textoColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 38));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sobreEl = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sobreEl = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isEnabled()) {
            Color color = getModel().isArmed() ? oscurecer(fondo, 0.12)
                    : (sobreEl || isFocusOwner() ? oscurecer(fondo, 0.07) : fondo);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        } else {
            g2.setColor(new Color(0xF3EDF0));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        }
        if (isFocusOwner() && isEnabled()) {
            g2.setColor(Estilo.FOCO);
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radio, radio);
        }
        g2.dispose();
        setForeground(isEnabled() ? textoColor : Estilo.TEXTO_SUAVE);
        super.paintComponent(g);
    }

    private static Color oscurecer(Color color, double factor) {
        int r = (int) Math.max(0, color.getRed() * (1 - factor));
        int g = (int) Math.max(0, color.getGreen() * (1 - factor));
        int b = (int) Math.max(0, color.getBlue() * (1 - factor));
        return new Color(r, g, b);
    }
}
