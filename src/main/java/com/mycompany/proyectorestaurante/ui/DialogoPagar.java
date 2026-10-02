package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.DetalleCuenta;
import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogoPagar extends JDialog {

    private static final long serialVersionUID = 1L;

    private final DetalleCuenta cuenta;
    private final JTextField txtEfectivo = new JTextField(12);
    private final JLabel lblCambio = new JLabel(" ");
    private final BotonRedondeado btnPagar = Estilo.botonPrimario("Pagar");
    private double efectivoRecibido = -1;

    public DialogoPagar(Frame padre, DetalleCuenta cuenta) {
        super(padre, "Pagar mesa " + cuenta.getNumeroMesa(), true);
        this.cuenta = cuenta;
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(Estilo.FONDO);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        int fila = 0;
        fila = agregarFila(panel, fila, "Subtotal", valor(Formatos.moneda(cuenta.getSubtotal()), 15, Estilo.TEXTO, false));
        fila = agregarFila(panel, fila, "Cargo de servicio ("
                + Formatos.porcentaje(cuenta.getPorcentajeCargoServicio()) + ")",
                valor(Formatos.moneda(cuenta.getMontoCargoServicio()), 15, Estilo.TEXTO, false));
        fila = agregarFila(panel, fila, "TOTAL", valor(Formatos.moneda(cuenta.getTotal()), 26, Estilo.ACENTO_OSCURO, true));

        txtEfectivo.setFont(Estilo.fuenteNegrita(15));
        txtEfectivo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtEfectivo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarCambio();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarCambio();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarCambio();
            }
        });
        fila = agregarFila(panel, fila, "Efectivo recibido", txtEfectivo);
        agregarFila(panel, fila, "Cambio", lblCambio);

        BotonRedondeado btnCancelar = Estilo.botonSuave("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        btnPagar.addActionListener(e -> aceptar());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnPagar);
        panelBotones.add(btnCancelar);

        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 16));
        tarjeta.setBorder(Estilo.relleno(20, 26, 18, 26));
        tarjeta.add(Estilo.etiquetaSeccion("Detalle del pago"), BorderLayout.NORTH);
        tarjeta.add(panel, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tarjeta, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnPagar);
        Accesibilidad.describir(txtEfectivo, "Efectivo recibido en soles",
                "Escriba el dinero que recibió el cliente en soles. Ejemplo: 100 o 1,250.50");
        Accesibilidad.cerrarConEscape(this);
        actualizarCambio();
        setResizable(false);
        pack();
        setLocationRelativeTo(padre);
    }

    private JLabel valor(String texto, int tamano, Color color, boolean destacado) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(destacado ? Estilo.fuenteNegrita(tamano) : Estilo.fuenteNegrita(tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    private int agregarFila(JPanel panel, int fila, String titulo, Component campo) {
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(Estilo.fuenteTexto(13));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);

        JPanel filaPanel = new JPanel(new GridBagLayout());
        filaPanel.setOpaque(false);
        GridBagConstraints restriccionesEtiqueta = new GridBagConstraints();
        restriccionesEtiqueta.anchor = GridBagConstraints.WEST;
        filaPanel.add(etiqueta, restriccionesEtiqueta);
        GridBagConstraints restriccionesCampo = new GridBagConstraints();
        restriccionesCampo.gridx = 1;
        restriccionesCampo.weightx = 1;
        restriccionesCampo.fill = GridBagConstraints.HORIZONTAL;
        restriccionesCampo.anchor = GridBagConstraints.EAST;
        filaPanel.add(campo, restriccionesCampo);

        GridBagConstraints restriccionesFila = new GridBagConstraints();
        restriccionesFila.gridy = fila;
        restriccionesFila.anchor = GridBagConstraints.WEST;
        restriccionesFila.fill = GridBagConstraints.HORIZONTAL;
        restriccionesFila.insets = new Insets(5, 0, 5, 0);
        panel.add(filaPanel, restriccionesFila);
        return fila + 1;
    }

    private void actualizarCambio() {
        double efectivo = leerEfectivo();
        lblCambio.setFont(Estilo.fuenteNegrita(16));
        if (efectivo < 0) {
            lblCambio.setForeground(Estilo.TEXTO_SUAVE);
            lblCambio.setText(txtEfectivo.getText().trim().isEmpty() ? " " : "Ingrese un monto válido.");
            btnPagar.setEnabled(false);
            return;
        }
        double diferencia = efectivo - cuenta.getTotal();
        lblCambio.setForeground(diferencia < 0 ? Estilo.ACENTO_OSCURO : Estilo.MENTA);
        lblCambio.setText(diferencia < 0 ? "Faltan " + Formatos.moneda(-diferencia) : Formatos.moneda(diferencia));
        btnPagar.setEnabled(diferencia >= 0);
    }

    private double leerEfectivo() {
        String texto = txtEfectivo.getText().trim();
        if (texto.isEmpty()) {
            return -1;
        }
        return Formatos.leerMonto(texto);
    }

    private void aceptar() {
        double efectivo = leerEfectivo();
        if (efectivo < cuenta.getTotal()) {
            actualizarCambio();
            btnPagar.setEnabled(false);
            return;
        }
        this.efectivoRecibido = efectivo;
        dispose();
    }

    public double getEfectivoRecibido() {
        return efectivoRecibido;
    }
}
