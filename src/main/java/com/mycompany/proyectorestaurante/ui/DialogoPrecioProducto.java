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

public class DialogoPrecioProducto extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JComboBox<Producto> cmbProductos = new JComboBox<Producto>();
    private final JTextField txtPrecio = new JTextField(10);
    private String nombreProducto = null;
    private double precio = -1;

    public DialogoPrecioProducto(Frame padre, PedidoService servicio) {
        super(padre, "Actualizar precio de un producto", true);
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
        cmbProductos.addActionListener(e -> txtPrecio.setText(Formatos.numero(productoActual().getPrecio())));

        txtPrecio.setFont(Estilo.fuenteNegrita(15));
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtPrecio.setText(Formatos.numero(productoActual().getPrecio()));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.add(crearEtiqueta("Producto"), crearRestricciones(0, 0));
        panel.add(cmbProductos, crearRestricciones(0, 1, true));
        panel.add(crearEtiqueta("Nuevo precio (S/)"), crearRestricciones(1, 0));
        panel.add(txtPrecio, crearRestricciones(1, 1, true));
        Accesibilidad.describir(txtPrecio, "Nuevo precio en soles",
                "Escriba el precio en soles, por ejemplo 32.50. Debe ser un número mayor que 0.");

        PanelRedondeado aviso = new PanelRedondeado(14, Estilo.ACENTO_CLARO, null);
        aviso.setLayout(new GridBagLayout());
        aviso.setBorder(Estilo.relleno(12, 16, 12, 16));
        JLabel textoAviso = new JLabel("<html>Los pedidos que ya tienen este producto conservan "
                + "el precio con el que fueron solicitados.</html>");
        textoAviso.setFont(Estilo.fuenteTexto(12));
        textoAviso.setForeground(Estilo.ACENTO_OSCURO);
        aviso.add(textoAviso);

        BotonRedondeado btnActualizar = Estilo.botonPrimario("Actualizar");
        btnActualizar.addActionListener(e -> aceptar());
        BotonRedondeado btnCancelar = Estilo.botonSuave("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCancelar);

        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(Estilo.relleno(20, 26, 18, 26));
        tarjeta.add(Estilo.etiquetaSeccion("Precios del catálogo"), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);
        centro.add(panel, BorderLayout.CENTER);
        centro.add(aviso, BorderLayout.SOUTH);
        tarjeta.add(centro, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tarjeta, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnActualizar);
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

    private void aceptar() {
        double valor = Formatos.leerMonto(txtPrecio.getText());
        if (valor <= 0) {
            mostrarError("El precio debe ser un número mayor que 0.");
            return;
        }
        this.nombreProducto = productoActual().getNombre();
        this.precio = valor;
        dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Precio no válido</b><br><br>" + mensaje + "</html>",
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }
}
