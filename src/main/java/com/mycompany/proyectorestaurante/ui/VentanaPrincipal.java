package com.mycompany.proyectorestaurante.ui;

import com.mycompany.proyectorestaurante.excepcion.ReglaNegocioException;
import com.mycompany.proyectorestaurante.modelo.DetalleCuenta;
import com.mycompany.proyectorestaurante.modelo.Estado;
import com.mycompany.proyectorestaurante.modelo.LineaPedido;
import com.mycompany.proyectorestaurante.modelo.Mesa;
import com.mycompany.proyectorestaurante.modelo.Pedido;
import com.mycompany.proyectorestaurante.servicio.PedidoService;
import com.mycompany.proyectorestaurante.ui.componentes.BotonRedondeado;
import com.mycompany.proyectorestaurante.ui.componentes.EtiquetaRedondeada;
import com.mycompany.proyectorestaurante.ui.componentes.PanelRedondeado;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String REGLAS = "<html><ul>"
            + "<li>Una mesa no puede tener dos pedidos abiertos al mismo tiempo.</li>"
            + "<li>Las cantidades deben ser mayores que 0.</li>"
            + "<li>Un pedido pagado no puede modificarse.</li>"
            + "<li>El precio de cada producto queda congelado en la línea de pedido.</li>"
            + "<li>El cargo de servicio es un porcentaje configurable sobre el subtotal.</li>"
            + "<li>Todos los importes se muestran en soles peruanos (S/).</li>"
            + "</ul></html>";

    private static final String USO = "<html><b>Cómo usar el programa</b><br><br>"
            + "<b>Ratón o teclado:</b> todas las acciones se pueden hacer con Tab y Enter.<br><br>"
            + "<b>Atajos de teclado:</b><br>"
            + "• Tab y Shift+Tab: moverse entre los botones.<br>"
            + "• Flechas arriba y abajo: recorrer las mesas y los productos.<br>"
            + "• Enter: confirmar la ventana abierta.<br>"
            + "• Esc: cerrar la ventana sin hacer cambios.<br><br>"
            + "<b>Ver mejor:</b> active Ayuda › Texto grande para agrandar las letras.<br>"
            + "Pase el ratón sobre cualquier botón para ver qué hace.</html>";

    private final PedidoService servicio;
    private final MesaTableModel mesaModel;
    private final PedidoTableModel pedidoModel = new PedidoTableModel();

    private final JTable tablaMesas;
    private final JTable tablaPedido = new JTable(pedidoModel);
    private final JScrollPane scrollMesas = new JScrollPane();
    private final JScrollPane scrollPedido = new JScrollPane();

    private final JLabel lblMesa = new JLabel(" ");
    private final JLabel lblResumen = new JLabel(" ");
    private final EtiquetaRedondeada etiquetaEstado = new EtiquetaRedondeada("Libre", Estilo.MENTA_CLARO, Estilo.MENTA);
    private final EtiquetaRedondeada etiquetaCargo = new EtiquetaRedondeada("10.00 %", Estilo.ACENTO_CLARO, Estilo.ACENTO_OSCURO);
    private final JPanel panelTotales = new JPanel(new GridBagLayout());
    private final JPanel panelVacio = new JPanel(new GridBagLayout());
    private final CardLayout layoutPedido = new CardLayout();
    private final JPanel panelPedido = new JPanel(layoutPedido);

    private final BotonRedondeado btnAbrir = Estilo.botonPrimario("Abrir mesa");
    private final BotonRedondeado btnAgregar = Estilo.botonPrimario("Agregar producto");
    private final BotonRedondeado btnQuitar = Estilo.botonSecundario("Quitar producto");
    private final BotonRedondeado btnModificar = Estilo.botonSecundario("Cambiar cantidad");
    private final BotonRedondeado btnAtender = Estilo.botonSecundario("Marcar atendida");
    private final BotonRedondeado btnCuenta = Estilo.botonSecundario("Calcular cuenta");
    private final BotonRedondeado btnPagar = Estilo.botonPrimario("Pagar");
    private final BotonRedondeado btnLiberar = Estilo.botonSuave("Liberar mesa");

    private int numeroMesaSeleccionada = -1;
    private boolean refrescando = false;

    public VentanaPrincipal(PedidoService servicio) {
        super("Restaurante · Operador de mesas");
        this.servicio = servicio;
        this.mesaModel = new MesaTableModel(servicio);
        this.tablaMesas = new JTable(mesaModel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 620));
        setSize(1120, 680);
        getContentPane().setBackground(Estilo.FONDO);
        setLayout(new BorderLayout());

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        setJMenuBar(crearMenu());

        configurarTablas();
        registrarEventos();
        explicarBotones();
        refrescar();
        tablaMesas.setRowSelectionInterval(0, 0);
        tablaMesas.requestFocusInWindow();
        setLocationRelativeTo(null);
    }

    private JPanel crearCabecera() {
        PanelRedondeado cabecera = new PanelRedondeado(24, Estilo.CABECERA_INICIO, Estilo.BORDE, Estilo.CABECERA_FIN);
        cabecera.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        cabecera.setLayout(new BorderLayout(20, 0));

        JPanel panelIzquierda = new JPanel(new BorderLayout(14, 0));
        panelIzquierda.setOpaque(false);
        PanelRedondeado emblema = new PanelRedondeado(46, Estilo.ACENTO, null);
        emblema.setLayout(new GridBagLayout());
        JLabel letra = new JLabel("R");
        letra.setFont(Estilo.fuenteTitulo(24));
        letra.setForeground(Estilo.BLANCO);
        emblema.add(letra);
        panelIzquierda.add(emblema, BorderLayout.WEST);

        JPanel panelTextos = new JPanel(new GridBagLayout());
        panelTextos.setOpaque(false);
        JLabel titulo = new JLabel("Restaurante");
        titulo.setFont(Estilo.fuenteTitulo(22));
        titulo.setForeground(Estilo.TITULO);
        JLabel subtitulo = new JLabel("Operador de mesas y pedidos");
        subtitulo.setFont(Estilo.fuenteTexto(13));
        subtitulo.setForeground(Estilo.TEXTO_SUAVE);
        panelTextos.add(titulo, new GridBagConstraints());
        GridBagConstraints restriccionesSubtitulo = new GridBagConstraints();
        restriccionesSubtitulo.gridy = 1;
        panelTextos.add(subtitulo, restriccionesSubtitulo);
        panelIzquierda.add(panelTextos, BorderLayout.CENTER);
        cabecera.add(panelIzquierda, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel(new GridBagLayout());
        panelDerecha.setOpaque(false);
        JPanel panelOpciones = new JPanel(new GridLayout(1, 3, 10, 0));
        panelOpciones.setOpaque(false);
        BotonRedondeado btnCargo = Estilo.botonSecundario("Cargo de servicio");
        btnCargo.addActionListener(e -> accionConfigurarCargoServicio());
        BotonRedondeado btnPrecio = Estilo.botonSecundario("Precios");
        btnPrecio.addActionListener(e -> accionActualizarPrecio());
        BotonRedondeado btnRefrescar = Estilo.botonSecundario("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        Accesibilidad.describir(btnCargo, "Cargo de servicio",
                "Cambia el porcentaje que se suma a cada cuenta. Ejemplo: 10 para un 10 %.");
        Accesibilidad.describir(btnPrecio, "Precios",
                "Actualiza el precio en soles de un producto del catálogo.");
        Accesibilidad.describir(btnRefrescar, "Refrescar",
                "Vuelve a leer el estado de las mesas y de los pedidos.");
        Accesibilidad.describir(etiquetaCargo, "Cargo de servicio vigente",
                "Porcentaje de cargo que se aplica a las cuentas nuevas.");
        panelOpciones.add(btnCargo);
        panelOpciones.add(btnPrecio);
        panelOpciones.add(btnRefrescar);

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.anchor = GridBagConstraints.CENTER;
        restricciones.insets = new Insets(0, 0, 0, 18);
        panelDerecha.add(etiquetaCargo, restricciones);
        restricciones.gridx = 1;
        restricciones.weightx = 1;
        restricciones.anchor = GridBagConstraints.LINE_END;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        panelDerecha.add(panelOpciones, restricciones);
        cabecera.add(panelDerecha, BorderLayout.EAST);
        return cabecera;
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setBackground(Estilo.FONDO);
        panel.setBorder(Estilo.relleno(18, 20, 20, 20));
        panel.add(crearTarjetaMesas(), BorderLayout.WEST);
        panel.add(crearTarjetaPedido(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTarjetaMesas() {
        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.setPreferredSize(new Dimension(300, 10));
        tarjeta.setBorder(Estilo.relleno(16, 8, 16, 8));

        tarjeta.add(Estilo.etiquetaSeccion("Mesas"), BorderLayout.NORTH);
        tarjeta.add(scrollMesas, BorderLayout.CENTER);

        PanelRedondeado panelBoton = new PanelRedondeado(14, Estilo.FONDO, null);
        panelBoton.setLayout(new BorderLayout());
        panelBoton.setBorder(Estilo.relleno(6, 4, 6, 4));
        panelBoton.add(btnAbrir, BorderLayout.CENTER);
        tarjeta.add(panelBoton, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearTarjetaPedido() {
        PanelRedondeado tarjeta = new PanelRedondeado(20, Estilo.TARJETA, Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(Estilo.relleno(18, 20, 20, 20));

        tarjeta.add(crearEncabezadoPedido(), BorderLayout.NORTH);

        panelPedido.setOpaque(false);
        panelPedido.setBackground(Estilo.TARJETA);
        panelPedido.add(crearVistaVacia(), "vacio");
        panelPedido.add(scrollPedido, "tabla");
        tarjeta.add(panelPedido, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout(0, 14));
        panelSur.setOpaque(false);
        panelSur.add(panelTotales, BorderLayout.NORTH);
        panelSur.add(crearBotonesPedido(), BorderLayout.CENTER);
        tarjeta.add(panelSur, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearEncabezadoPedido() {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);
        panel.add(lblMesa, BorderLayout.WEST);
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelDerecho.setOpaque(false);
        panelDerecho.add(lblResumen);
        panelDerecho.add(etiquetaEstado);
        panel.add(panelDerecho, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearVistaVacia() {
        panelVacio.setOpaque(false);
        panelVacio.setBackground(Estilo.TARJETA);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new GridBagLayout());
        JLabel titulo = new JLabel("Sin pedido abierto");
        titulo.setFont(Estilo.fuenteTitulo(18));
        titulo.setForeground(Estilo.TEXTO_SUAVE);
        JLabel detalle = new JLabel("Seleccione una mesa de la lista y presione Abrir mesa");
        detalle.setFont(Estilo.fuenteTexto(13));
        detalle.setForeground(Estilo.TEXTO_SUAVE);
        textos.add(titulo);
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridy = 1;
        restricciones.insets = new Insets(6, 0, 0, 0);
        textos.add(detalle, restricciones);
        panelVacio.add(textos);
        return panelVacio;
    }

    private JPanel crearBotonesPedido() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 12, 12));
        panel.setOpaque(false);
        panel.add(btnAgregar);
        panel.add(btnQuitar);
        panel.add(btnModificar);
        panel.add(btnAtender);
        panel.add(btnCuenta);
        panel.add(btnPagar);
        panel.add(btnLiberar);
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        panel.add(relleno);
        return panel;
    }

    private JMenuBar crearMenu() {
        JMenuBar barra = new JMenuBar();
        barra.setFont(Estilo.fuenteTexto(14));

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic('A');
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setMnemonic('S');
        itemSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemSalir.setToolTipText("Cerrar el programa (Ctrl+Q)");
        itemSalir.addActionListener(e -> dispose());
        menuArchivo.add(itemSalir);
        barra.add(menuArchivo);

        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic('Y');
        JMenuItem itemUso = new JMenuItem("Cómo usar el programa");
        itemUso.setMnemonic('C');
        itemUso.setAccelerator(javax.swing.KeyStroke.getKeyStroke("F1"));
        itemUso.addActionListener(e -> JOptionPane.showMessageDialog(this, USO,
                "Cómo usar el programa", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemUso);
        JCheckBoxMenuItem itemGrande = new JCheckBoxMenuItem("Texto grande", Accesibilidad.esTextoGrande());
        itemGrande.setMnemonic('T');
        itemGrande.setToolTipText("Agranda las letras de toda la aplicación");
        itemGrande.addActionListener(e -> Accesibilidad.alternarTextoGrande(this));
        menuAyuda.add(itemGrande);
        JMenuItem itemReglas = new JMenuItem("Reglas de negocio");
        itemReglas.setMnemonic('R');
        itemReglas.addActionListener(e -> JOptionPane.showMessageDialog(this, REGLAS,
                "Reglas de negocio", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemReglas);
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.setMnemonic('A');
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "<html><b>Restaurante</b><br>Sistema de gestión de mesas y pedidos<br>"
                        + "Importes en soles peruanos (S/)<br>Proyecto de grado</html>",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);
        barra.add(menuAyuda);
        return barra;
    }

    private void configurarTablas() {
        prepararScroll(scrollMesas, tablaMesas);
        prepararScroll(scrollPedido, tablaPedido);

        tablaMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilo.configurarTabla(tablaMesas);
        tablaMesas.getColumnModel().getColumn(0).setCellRenderer(
                new RenderizadorSimple(SwingConstants.CENTER, Estilo.fuenteNegrita(15)));
        tablaMesas.getColumnModel().getColumn(1).setCellRenderer(new RenderizadorEstado());
        tablaMesas.getColumnModel().getColumn(2).setCellRenderer(
                new RenderizadorSimple(SwingConstants.RIGHT, Estilo.fuenteNegrita(13)));
        tablaMesas.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaMesas.getColumnModel().getColumn(1).setPreferredWidth(110);
        tablaMesas.getColumnModel().getColumn(2).setPreferredWidth(110);

        tablaPedido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilo.configurarTabla(tablaPedido);
        tablaPedido.getColumnModel().getColumn(0).setCellRenderer(
                new RenderizadorSimple(SwingConstants.LEFT, Estilo.fuenteTexto(14)));
        tablaPedido.getColumnModel().getColumn(1).setCellRenderer(
                new RenderizadorSimple(SwingConstants.CENTER, Estilo.fuenteNegrita(13)));
        tablaPedido.getColumnModel().getColumn(2).setCellRenderer(
                new RenderizadorSimple(SwingConstants.RIGHT, Estilo.fuenteTexto(13)));
        tablaPedido.getColumnModel().getColumn(3).setCellRenderer(
                new RenderizadorSimple(SwingConstants.RIGHT, Estilo.fuenteNegrita(13)));

        lblMesa.setFont(Estilo.fuenteTitulo(20));
        lblMesa.setForeground(Estilo.TITULO);
        lblResumen.setFont(Estilo.fuenteTexto(12));
        lblResumen.setForeground(Estilo.TEXTO_SUAVE);
    }

    private void prepararScroll(JScrollPane scroll, JTable tabla) {
        scroll.setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(true);
        scroll.getViewport().setBackground(Estilo.TARJETA);
        scroll.setViewportView(tabla);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(Accesibilidad.anchoBarra(), 0));
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
    }

    private void explicarBotones() {
        Accesibilidad.describir(btnAbrir, "Abrir mesa", "Abre un pedido nuevo en la mesa seleccionada.");
        Accesibilidad.describir(btnAgregar, "Agregar producto", "Agrega un producto del catálogo al pedido de la mesa.");
        Accesibilidad.describir(btnQuitar, "Quitar producto", "Resta unidades del producto seleccionado, o lo quita si se agotan.");
        Accesibilidad.describir(btnModificar, "Cambiar cantidad", "Cambia la cantidad del producto seleccionado.");
        Accesibilidad.describir(btnAtender, "Marcar atendida", "Marca el pedido como atendido, listo para cobrar.");
        Accesibilidad.describir(btnCuenta, "Calcular cuenta", "Muestra el subtotal, el cargo de servicio y el total en soles.");
        Accesibilidad.describir(btnPagar, "Pagar", "Registra el efectivo recibido y muestra el cambio.");
        Accesibilidad.describir(btnLiberar, "Liberar mesa", "Deja la mesa libre para abrir un pedido nuevo.");
        Accesibilidad.describir(tablaMesas, "Lista de mesas",
                "Use las flechas del teclado para recorrer las mesas y ver su estado y su consumo.");
        Accesibilidad.describir(tablaPedido, "Productos del pedido",
                "Lista los productos de la mesa con su cantidad y su importe en soles.");
    }

    private void registrarEventos() {
        tablaMesas.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || refrescando) {
                return;
            }
            Mesa mesa = obtenerMesaSeleccionada();
            numeroMesaSeleccionada = mesa == null ? -1 : mesa.getNumero();
            refrescarPedido();
        });
        btnAbrir.addActionListener(e -> accionAbrirMesa());
        btnAgregar.addActionListener(e -> accionAgregarProducto());
        btnQuitar.addActionListener(e -> accionQuitarProducto());
        btnModificar.addActionListener(e -> accionModificarCantidad());
        btnAtender.addActionListener(e -> ejecutar(() -> servicio.atender(numeroMesaSeleccionada())));
        btnCuenta.addActionListener(e -> accionCalcularCuenta());
        btnPagar.addActionListener(e -> accionPagar());
        btnLiberar.addActionListener(e -> accionLiberarMesa());
    }

    private void accionAbrirMesa() {
        int numeroMesa = numeroMesaSeleccionada();
        if (numeroMesa < 0) {
            return;
        }
        ejecutar(() -> servicio.abrirMesa(numeroMesa));
    }

    private void accionAgregarProducto() {
        int numeroMesa = numeroMesaSeleccionada();
        if (numeroMesa < 0) {
            return;
        }
        DialogoAgregarProducto dialogo = new DialogoAgregarProducto(this, servicio);
        dialogo.setVisible(true);
        if (!dialogo.isConfirmado()) {
            return;
        }
        ejecutar(() -> servicio.agregarItem(numeroMesa, dialogo.getProducto(), dialogo.getCantidad()));
    }

    private void accionQuitarProducto() {
        LineaPedido linea = obtenerLineaSeleccionada();
        int numeroMesa = numeroMesaSeleccionada();
        if (linea == null || numeroMesa < 0) {
            mostrarMensaje("Seleccione un producto de la lista del pedido.");
            return;
        }
        DialogoCantidad dialogo = new DialogoCantidad(this, "Quitar producto",
                linea.getProducto().getNombre(), linea.getCantidad());
        dialogo.setVisible(true);
        int cantidad = dialogo.getCantidad();
        if (cantidad <= 0) {
            return;
        }
        ejecutar(() -> servicio.quitarItem(numeroMesa, linea.getProducto(), cantidad));
    }

    private void accionModificarCantidad() {
        LineaPedido linea = obtenerLineaSeleccionada();
        int numeroMesa = numeroMesaSeleccionada();
        if (linea == null || numeroMesa < 0) {
            mostrarMensaje("Seleccione un producto de la lista del pedido.");
            return;
        }
        DialogoCantidad dialogo = new DialogoCantidad(this, "Cambiar cantidad",
                linea.getProducto().getNombre(), linea.getCantidad());
        dialogo.setVisible(true);
        int cantidad = dialogo.getCantidad();
        if (cantidad <= 0) {
            return;
        }
        ejecutar(() -> servicio.modificarCantidad(numeroMesa, linea.getProducto(), cantidad));
    }

    private void accionCalcularCuenta() {
        int numeroMesa = numeroMesaSeleccionada();
        if (numeroMesa < 0) {
            return;
        }
        try {
            mostrarDetalleCuenta(servicio.calcularCuenta(numeroMesa), "Detalle de la cuenta");
        } catch (ReglaNegocioException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    private void accionPagar() {
        int numeroMesa = numeroMesaSeleccionada();
        if (numeroMesa < 0) {
            return;
        }
        try {
            DialogoPagar dialogo = new DialogoPagar(this, servicio.calcularCuenta(numeroMesa));
            dialogo.setVisible(true);
            double efectivo = dialogo.getEfectivoRecibido();
            if (efectivo < 0) {
                return;
            }
            DetalleCuenta cuenta = servicio.pagar(numeroMesa, efectivo);
            refrescar();
            mostrarDetalleCuenta(cuenta, "Pago realizado");
        } catch (ReglaNegocioException e) {
            mostrarMensaje(e.getMessage());
            refrescar();
        }
    }

    private void accionLiberarMesa() {
        int numeroMesa = numeroMesaSeleccionada();
        if (numeroMesa < 0) {
            return;
        }
        ejecutar(() -> servicio.liberarMesa(numeroMesa));
    }

    private void accionConfigurarCargoServicio() {
        DialogoCargoServicio dialogo = new DialogoCargoServicio(this, servicio.getCargoServicioPorDefecto());
        dialogo.setVisible(true);
        double porcentaje = dialogo.getPorcentaje();
        if (porcentaje < 0) {
            return;
        }
        ejecutar(() -> servicio.aplicarCargoServicio(porcentaje, dialogo.isAplicarAPedidosAbiertos()));
    }

    private void accionActualizarPrecio() {
        DialogoPrecioProducto dialogo = new DialogoPrecioProducto(this, servicio);
        dialogo.setVisible(true);
        if (dialogo.getNombreProducto() == null) {
            return;
        }
        ejecutar(() -> servicio.actualizarPrecioProducto(dialogo.getNombreProducto(), dialogo.getPrecio()));
    }

    private void refrescar() {
        refrescando = true;
        mesaModel.actualizar();
        etiquetaCargo.setText("Cargo " + Formatos.porcentaje(servicio.getCargoServicioPorDefecto()));
        restaurarSeleccion();
        refrescando = false;
        refrescarPedido();
    }

    private void restaurarSeleccion() {
        int fila = numeroMesaSeleccionada <= 0 ? -1 : mesaModel.getFilaDeMesa(numeroMesaSeleccionada);
        if (fila < 0) {
            tablaMesas.clearSelection();
            numeroMesaSeleccionada = -1;
            return;
        }
        if (tablaMesas.getSelectedRow() != fila) {
            tablaMesas.setRowSelectionInterval(fila, fila);
        }
    }

    private void refrescarPedido() {
        Mesa mesa = obtenerMesaPorNumero();
        Pedido pedido = mesa == null ? null : servicio.getPedido(mesa.getNumero());
        pedidoModel.actualizar(pedido);
        tablaPedido.clearSelection();
        layoutPedido.show(panelPedido, pedido == null ? "vacio" : "tabla");
        actualizarEncabezado(mesa, pedido);
        actualizarTotales(pedido);
        actualizarBotones(mesa, pedido);
    }

    private Mesa obtenerMesaPorNumero() {
        if (numeroMesaSeleccionada <= 0) {
            return null;
        }
        return mesaModel.getMesa(mesaModel.getFilaDeMesa(numeroMesaSeleccionada));
    }

    private void actualizarEncabezado(Mesa mesa, Pedido pedido) {
        if (mesa == null) {
            lblMesa.setText("Pedido de mesa");
            lblResumen.setText("Sin mesa seleccionada");
            etiquetaEstado.setColores(Estilo.NEUTRO_CLARO, Estilo.TEXTO_SUAVE);
            etiquetaEstado.setText("Libre");
            return;
        }
        lblMesa.setText("Mesa " + mesa.getNumero());
        Color[] colores = Estilo.coloresEstado(mesa.getEstado());
        etiquetaEstado.setColores(colores[0], colores[1]);
        etiquetaEstado.setText(mesa.getEstado().getDescripcion());
        lblResumen.setText(pedido == null
                ? "Sin pedido abierto"
                : pedido.getCantidadDeItems() + (pedido.getCantidadDeItems() == 1 ? " producto" : " productos"));
    }

    private void actualizarTotales(Pedido pedido) {
        panelTotales.removeAll();
        panelTotales.setOpaque(false);
        if (pedido == null) {
            panelTotales.add(crearTotalVacio());
        } else if (pedido.estaPagado()) {
            DetalleCuenta cuenta = pedido.getCuentaFinal();
            panelTotales.add(crearCampoTotal("Subtotal", Formatos.moneda(cuenta.getSubtotal()), Estilo.TEXTO, false));
            panelTotales.add(crearCampoTotal("Cargo (" + Formatos.porcentaje(cuenta.getPorcentajeCargoServicio()) + ")",
                    Formatos.moneda(cuenta.getMontoCargoServicio()), Estilo.TEXTO, false));
            panelTotales.add(crearCampoTotal("Total pagado", Formatos.moneda(cuenta.getTotal()), Estilo.MENTA, true));
            panelTotales.add(crearCampoTotal("Cambio", Formatos.moneda(cuenta.getCambio()), Estilo.MENTA, false));
        } else {
            panelTotales.add(crearCampoTotal("Subtotal", Formatos.moneda(pedido.getSubtotal()), Estilo.TEXTO, false));
            panelTotales.add(crearCampoTotal("Cargo (" + Formatos.porcentaje(pedido.getCargoServicio()) + ")",
                    Formatos.moneda(pedido.getMontoCargoServicio()), Estilo.TEXTO, false));
            panelTotales.add(crearCampoTotal("Total a pagar", Formatos.moneda(pedido.getTotal()),
                    Estilo.ACENTO_OSCURO, true));
            panelTotales.add(crearCampoTotal("Productos", String.valueOf(pedido.getCantidadDeItems()),
                    Estilo.TEXTO, false));
        }
        panelTotales.revalidate();
        panelTotales.repaint();
    }

    private JLabel crearTotalVacio() {
        JLabel etiqueta = new JLabel("El total aparecerá aquí cuando agregue productos");
        etiqueta.setFont(Estilo.fuenteTexto(13));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        return etiqueta;
    }

    private JPanel crearCampoTotal(String titulo, String valor, Color colorValor, boolean destacado) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(Estilo.fuenteTexto(11));
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        JLabel monto = new JLabel(valor);
        monto.setFont(Estilo.fuenteNegrita(destacado ? 24 : 16));
        monto.setForeground(colorValor);
        panel.add(etiqueta, new GridBagConstraints());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridy = 1;
        restricciones.insets = new Insets(2, 0, 0, 0);
        restricciones.anchor = GridBagConstraints.WEST;
        panel.add(monto, restricciones);
        return panel;
    }

    private void actualizarBotones(Mesa mesa, Pedido pedido) {
        boolean pedidoAbierto = pedido != null && !pedido.estaPagado();
        btnAbrir.setEnabled(mesa != null && pedido == null);
        btnAgregar.setEnabled(pedidoAbierto);
        btnQuitar.setEnabled(pedidoAbierto);
        btnModificar.setEnabled(pedidoAbierto);
        btnAtender.setEnabled(pedidoAbierto && pedido.getEstado() == Estado.ABIERTA && !pedido.estaVacio());
        btnCuenta.setEnabled(mesa != null && pedido != null);
        btnPagar.setEnabled(pedidoAbierto && !pedido.estaVacio());
        btnLiberar.setEnabled(pedido != null && pedido.estaPagado());
    }

    private void mostrarDetalleCuenta(DetalleCuenta cuenta, String titulo) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("<html><b>Mesa ").append(cuenta.getNumeroMesa()).append("</b><br><br>");
        mensaje.append("Subtotal: ").append(Formatos.moneda(cuenta.getSubtotal())).append("<br>");
        mensaje.append("Cargo de servicio (").append(Formatos.porcentaje(cuenta.getPorcentajeCargoServicio()))
                .append("): ").append(Formatos.moneda(cuenta.getMontoCargoServicio())).append("<br>");
        mensaje.append("<b>TOTAL: ").append(Formatos.moneda(cuenta.getTotal())).append("</b>");
        if (cuenta.getEfectivoRecibido() > 0) {
            mensaje.append("<br><br>Efectivo recibido: ").append(Formatos.moneda(cuenta.getEfectivoRecibido()));
            mensaje.append("<br>Cambio: ").append(Formatos.moneda(cuenta.getCambio()));
        }
        mensaje.append("</html>");
        JOptionPane.showMessageDialog(this, mensaje.toString(), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Operación no permitida</b><br><br>" + mensaje + "</html>",
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void ejecutar(Operacion operacion) {
        try {
            operacion.ejecutar();
        } catch (ReglaNegocioException e) {
            mostrarMensaje(e.getMessage());
        }
        refrescar();
    }

    private Mesa obtenerMesaSeleccionada() {
        return mesaModel.getMesa(tablaMesas.getSelectedRow());
    }

    private LineaPedido obtenerLineaSeleccionada() {
        return pedidoModel.getLinea(tablaPedido.getSelectedRow());
    }

    private int numeroMesaSeleccionada() {
        return numeroMesaSeleccionada;
    }

    @FunctionalInterface
    private interface Operacion {
        void ejecutar() throws ReglaNegocioException;
    }
}
