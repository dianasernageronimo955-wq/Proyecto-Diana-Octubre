package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.modelo.Producto;
import com.mycompany.proyectorestaurante.servicio.PedidoService;
import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogoAgregarProducto extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JComboBox<Producto> cmbProductos = new JComboBox<Producto>();
    private final JLabel lblPrecio = new JLabel();
    private final JTextField txtCantidad = new JTextField("1", 6);
    private boolean confirmado = false;
    private int cantidad = 0;

    public DialogoAgregarProducto(Frame padre, PedidoService servicio) {
        super(padre, "Agregar producto", true);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(Estilo.FONDO);

        List<Producto> productos = servicio.getCatalogo();
        cmbProductos.setModel(new DefaultComboBoxModel<Producto>(
                productos.toArray(new Producto[productos.size()])));
        cmbProductos.setFont(Estilo.fuenteTexto(14));
        cmbProductos.setBackground(Estilo.BLANCO);
        cmbProductos.setForeground(Estilo.TEXTO);
        cmbProductos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        cmbProductos.addActionListener(e -> actualizarPrecio());
        actualizarPrecio();

        txtCantidad.setFont(Estilo.fuenteTexto(14));
        txtCantidad.setHorizontalAlignment(JLabel.CENTER);
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(Estilo.relleno(0, 4, 8, 4));
        panel.add(crearEtiqueta("Producto"), crearRestricciones(0, 0));
        panel.add(cmbProductos, crearRestricciones(0, 1, true));
        panel.add(crearEtiqueta("Precio en soles"), crearRestricciones(1, 0));
        lblPrecio.setFont(Estilo.fuenteNegrita(14));
        lblPrecio.setForeground(Estilo.ACENTO_OSCURO);
        panel.add(lblPrecio, crearRestricciones(1, 1, true));
        panel.add(crearEtiqueta("Cantidad"), crearRestricciones(2, 0));
        panel.add(txtCantidad, crearRestricciones(2, 1, true));
        Accesibilidad.describir(cmbProductos, "Producto",
                "Elija el producto que desea agregar al pedido.");
        Accesibilidad.describir(txtCantidad, "Cantidad",
                "Escriba cuántas unidades quiere. Debe ser un número mayor que 0.");

        BotonRedondeado btnAgregar = Estilo.botonPrimario("Agregar");
        btnAgregar.addActionListener(e -> aceptar());
        BotonRedondeado btnCancelar = Estilo.botonSuave("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(Estilo.relleno(20, 22, 18, 22));
        tarjeta.add(Estilo.etiquetaSeccion("Agregar producto al pedido"), BorderLayout.NORTH);
        tarjeta.add(panel, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tarjeta, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnAgregar);
        Accesibilidad.cerrarConEscape(this);
        setResizable(false);
        pack();
        setLocationRelativeTo(padre);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Estilo.fuenteTexto(13));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        return etiqueta;
    }

    private GridBagConstraints crearRestricciones(int fila, int columna) {
        return crearRestricciones(fila, columna, false);
    }

    private GridBagConstraints crearRestricciones(int fila, int columna, boolean estirar) {
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = columna;
        restricciones.gridy = fila;
        restricciones.anchor = columna == 0 ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        restricciones.insets = new Insets(6, 6, 6, 6);
        if (estirar) {
            restricciones.weightx = 1;
            restricciones.fill = GridBagConstraints.HORIZONTAL;
        }
        return restricciones;
    }

    private Producto productoActual() {
        return (Producto) cmbProductos.getSelectedItem();
    }

    private void actualizarPrecio() {
        Producto producto = productoActual();
        lblPrecio.setText(producto == null ? "" : Formatos.moneda(producto.getPrecio()));
    }

    private void aceptar() {
        int valor = Formatos.leerCantidad(txtCantidad.getText());
        if (valor <= 0) {
            mostrarError("La cantidad debe ser un número entero mayor que 0.");
            return;
        }
        this.cantidad = valor;
        this.confirmado = true;
        dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Cantidad no válida</b><br><br>" + mensaje + "</html>",
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Producto getProducto() {
        return productoActual();
    }

    public int getCantidad() {
        return cantidad;
    }
}
