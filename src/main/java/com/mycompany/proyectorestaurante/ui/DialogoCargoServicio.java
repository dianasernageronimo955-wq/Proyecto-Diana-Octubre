package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogoCargoServicio extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField txtPorcentaje = new JTextField(8);
    private final JCheckBox chkAplicar = new JCheckBox("Aplicar también a los pedidos abiertos", true);
    private double porcentaje = -1;
    private boolean aplicarAPedidosAbiertos = false;

    public DialogoCargoServicio(Frame padre, double cargoActual) {
        super(padre, "Cargo de servicio", true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(Estilo.FONDO);
        txtPorcentaje.setText(Formatos.numero(cargoActual * 100));
        txtPorcentaje.setHorizontalAlignment(JLabel.CENTER);
        txtPorcentaje.setFont(Estilo.fuenteNegrita(15));
        txtPorcentaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        chkAplicar.setFont(Estilo.fuenteTexto(13));
        chkAplicar.setBackground(Estilo.TARJETA);
        chkAplicar.setForeground(Estilo.TEXTO);
        chkAplicar.setFocusPainted(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JPanel campo = new JPanel(new GridBagLayout());
        campo.setOpaque(false);
        JLabel etiqueta = new JLabel("Porcentaje");
        etiqueta.setFont(Estilo.fuenteTexto(13));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        campo.add(etiqueta, new GridBagConstraints());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 1;
        restricciones.weightx = 1;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(0, 10, 0, 0);
        campo.add(txtPorcentaje, restricciones);
        panel.add(campo, new GridBagConstraints());

        GridBagConstraints restriccionesOpciones = new GridBagConstraints();
        restriccionesOpciones.gridy = 1;
        restriccionesOpciones.insets = new Insets(18, 0, 0, 0);
        restriccionesOpciones.anchor = GridBagConstraints.WEST;
        panel.add(chkAplicar, restriccionesOpciones);

        JPanel panelInformacion = new JPanel(new GridLayout(2, 1, 0, 4));
        panelInformacion.setOpaque(false);
        panelInformacion.add(Estilo.etiquetaSuave("El cargo se calcula sobre el subtotal del pedido"));
        panelInformacion.add(Estilo.etiquetaSuave("Ejemplo: 10 % de un subtotal de S/ 50.00 da un cargo de S/ 5.00"));

        BotonRedondeado btnGuardar = Estilo.botonPrimario("Guardar");
        btnGuardar.addActionListener(e -> aceptar());
        BotonRedondeado btnCancelar = Estilo.botonSuave("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(Estilo.relleno(20, 26, 18, 26));
        tarjeta.add(Estilo.etiquetaSeccion("Cargo de servicio"), BorderLayout.NORTH);
        tarjeta.add(panel, BorderLayout.CENTER);
        JPanel sur = new JPanel(new BorderLayout(0, 12));
        sur.setOpaque(false);
        sur.add(panelInformacion, BorderLayout.CENTER);
        sur.add(panelBotones, BorderLayout.SOUTH);
        tarjeta.add(sur, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tarjeta, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnGuardar);
        Accesibilidad.describir(txtPorcentaje, "Porcentaje de cargo de servicio",
                "Escriba el porcentaje, por ejemplo 10 para un 10 %. Debe estar entre 0 y 100.");
        Accesibilidad.cerrarConEscape(this);
        setResizable(false);
        pack();
        setLocationRelativeTo(padre);
    }

    private void aceptar() {
        double valor = Formatos.leerMonto(txtPorcentaje.getText());
        if (valor < 0) {
            mostrarError("El cargo de servicio debe ser un número.");
            return;
        }
        if (valor > 100) {
            mostrarError("El cargo de servicio debe estar entre 0% y 100%.");
            return;
        }
        this.porcentaje = valor / 100;
        this.aplicarAPedidosAbiertos = chkAplicar.isSelected();
        dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Cargo de servicio no válido</b><br><br>" + mensaje + "</html>",
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public boolean isAplicarAPedidosAbiertos() {
        return aplicarAPedidosAbiertos;
    }
}
