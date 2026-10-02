package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogoCantidad extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField txtCantidad = new JTextField("1", 8);
    private int cantidad = -1;

    public DialogoCantidad(Frame padre, String titulo, String producto, int cantidadActual) {
        super(padre, titulo, true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(Estilo.FONDO);

        txtCantidad.setFont(Estilo.fuenteTexto(15));
        txtCantidad.setHorizontalAlignment(JLabel.CENTER);
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtCantidad.selectAll();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new GridBagLayout());
        JLabel nombre = new JLabel(producto);
        nombre.setFont(Estilo.fuenteNegrita(15));
        nombre.setForeground(Estilo.TITULO);
        JLabel actual = new JLabel("Cantidad actual: " + cantidadActual);
        actual.setFont(Estilo.fuenteTexto(12));
        actual.setForeground(Estilo.TEXTO_SUAVE);
        textos.add(nombre, new GridBagConstraints());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridy = 1;
        restricciones.insets = new Insets(2, 0, 0, 0);
        textos.add(actual, restricciones);
        panel.add(textos, new GridBagConstraints());

        JLabel etiqueta = new JLabel("Nueva cantidad");
        etiqueta.setFont(Estilo.fuenteTexto(13));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        GridBagConstraints restriccionesCampo = new GridBagConstraints();
        restriccionesCampo.gridy = 1;
        restriccionesCampo.insets = new Insets(16, 0, 0, 0);
        panel.add(campo(etiqueta, txtCantidad), restriccionesCampo);

        BotonRedondeado btnAplicar = Estilo.botonPrimario("Aplicar");
        btnAplicar.addActionListener(e -> aceptar());
        BotonRedondeado btnCancelar = Estilo.botonSuave("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnAplicar);
        panelBotones.add(btnCancelar);

        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(Estilo.relleno(20, 24, 18, 24));
        tarjeta.add(Estilo.etiquetaSeccion("Cantidad"), BorderLayout.NORTH);
        tarjeta.add(panel, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tarjeta, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnAplicar);
        Accesibilidad.describir(txtCantidad, "Nueva cantidad",
                "Escriba la cantidad nueva. Debe ser un número entero mayor que 0.");
        Accesibilidad.cerrarConEscape(this);
        setResizable(false);
        pack();
        setLocationRelativeTo(padre);
    }

    private JPanel campo(JLabel etiqueta, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.add(etiqueta, BorderLayout.WEST);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    private void aceptar() {
        int valor = Formatos.leerCantidad(txtCantidad.getText());
        if (valor <= 0) {
            mostrarError("La cantidad debe ser un número entero mayor que 0.");
            return;
        }
        this.cantidad = valor;
        dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Cantidad no válida</b><br><br>" + mensaje + "</html>",
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public int getCantidad() {
        return cantidad;
    }
}
