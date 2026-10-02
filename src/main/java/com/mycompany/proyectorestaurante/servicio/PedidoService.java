package com.mycompany.proyectorestaurante.servicio;

import com.mycompany.proyectorestaurante.excepcion.ReglaNegocioException;
import com.mycompany.proyectorestaurante.modelo.DetalleCuenta;
import com.mycompany.proyectorestaurante.modelo.Estado;
import com.mycompany.proyectorestaurante.modelo.LineaPedido;
import com.mycompany.proyectorestaurante.modelo.Mesa;
import com.mycompany.proyectorestaurante.modelo.Pedido;
import com.mycompany.proyectorestaurante.modelo.Producto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoService {

    private final Map<Integer, Mesa> mesas = new LinkedHashMap<Integer, Mesa>();
    private final Map<Integer, Pedido> pedidosPorMesa = new LinkedHashMap<Integer, Pedido>();
    private final List<Producto> catalogo = new ArrayList<Producto>();
    private double cargoServicioPorDefecto;

    public PedidoService() {
        this(10, 0.10);
    }

    public PedidoService(int cantidadMesas, double cargoServicioPorDefecto) {
        if (cantidadMesas <= 0) {
            throw new IllegalArgumentException("La cantidad de mesas debe ser mayor que 0.");
        }
        this.cargoServicioPorDefecto = comprobarCargoServicio(cargoServicioPorDefecto);
        for (int numero = 1; numero <= cantidadMesas; numero++) {
            mesas.put(numero, new Mesa(numero));
        }
        cargarCatalogoInicial();
    }

    private void cargarCatalogoInicial() {
        catalogo.add(new Producto("Sancocho de pollo", 22.00));
        catalogo.add(new Producto("Bandeja paisa", 32.50));
        catalogo.add(new Producto("Arepa de queso", 8.50));
        catalogo.add(new Producto("Jugo de mango", 9.00));
        catalogo.add(new Producto("Cerveza", 10.00));
        catalogo.add(new Producto("Postre de la casa", 12.00));
    }

    public List<Mesa> getMesas() {
        return new ArrayList<Mesa>(mesas.values());
    }

    public List<Producto> getCatalogo() {
        return new ArrayList<Producto>(catalogo);
    }

    public Pedido getPedido(int numeroMesa) {
        return pedidosPorMesa.get(numeroMesa);
    }

    public List<Pedido> listarMesasAbiertas() {
        List<Pedido> pedidosAbiertos = new ArrayList<Pedido>();
        for (Pedido pedido : pedidosPorMesa.values()) {
            if (!pedido.estaPagado()) {
                pedidosAbiertos.add(pedido);
            }
        }
        return pedidosAbiertos;
    }

    public Pedido abrirMesa(int numeroMesa) throws ReglaNegocioException {
        Mesa mesa = obtenerMesa(numeroMesa);
        Pedido pedidoExistente = pedidosPorMesa.get(numeroMesa);
        if (pedidoExistente != null && !pedidoExistente.estaPagado()) {
            throw new ReglaNegocioException("La mesa ya tiene un pedido abierto.");
        }
        if (pedidoExistente != null && pedidoExistente.estaPagado()) {
            throw new ReglaNegocioException("La mesa " + numeroMesa
                    + " está pagada. Libere la mesa antes de abrir un pedido nuevo.");
        }
        Pedido pedido = new Pedido(mesa, cargoServicioPorDefecto);
        pedidosPorMesa.put(numeroMesa, pedido);
        mesa.setEstado(Estado.ABIERTA);
        return pedido;
    }

    public void agregarItem(int numeroMesa, Producto producto, int cantidad) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        validarPedidoModificable(pedido);
        validarCantidad(cantidad);
        Producto productoDelCatalogo = buscarProductoPorNombre(producto.getNombre());
        LineaPedido linea = pedido.buscarLinea(productoDelCatalogo);
        if (linea == null) {
            pedido.agregarLinea(new LineaPedido(productoDelCatalogo, cantidad));
        } else {
            linea.setCantidad(linea.getCantidad() + cantidad);
        }
    }

    public void quitarItem(int numeroMesa, Producto producto, int cantidad) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        validarPedidoModificable(pedido);
        validarCantidad(cantidad);
        LineaPedido linea = obtenerLinea(pedido, producto);
        if (cantidad >= linea.getCantidad()) {
            pedido.quitarLinea(linea);
        } else {
            linea.setCantidad(linea.getCantidad() - cantidad);
        }
    }

    public void modificarCantidad(int numeroMesa, Producto producto, int cantidad) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        validarPedidoModificable(pedido);
        validarCantidad(cantidad);
        obtenerLinea(pedido, producto).setCantidad(cantidad);
    }

    public void atender(int numeroMesa) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        validarPedidoModificable(pedido);
        if (pedido.estaVacio()) {
            throw new ReglaNegocioException("El pedido no tiene productos agregados.");
        }
        pedido.setEstado(Estado.ATENDIDA);
        pedido.getMesa().setEstado(Estado.ATENDIDA);
    }

    public DetalleCuenta calcularCuenta(int numeroMesa) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        return new DetalleCuenta(numeroMesa, pedido.getSubtotal(), pedido.getCargoServicio(),
                pedido.getMontoCargoServicio(), pedido.getTotal());
    }

    public DetalleCuenta pagar(int numeroMesa, double efectivoRecibido) throws ReglaNegocioException {
        Pedido pedido = obtenerPedido(numeroMesa);
        validarPedidoModificable(pedido);
        if (efectivoRecibido < 0) {
            throw new ReglaNegocioException("El monto recibido no puede ser negativo.");
        }
        if (pedido.estaVacio()) {
            throw new ReglaNegocioException("No se puede pagar un pedido sin productos.");
        }
        DetalleCuenta cuenta = calcularCuenta(numeroMesa);
        if (efectivoRecibido < cuenta.getTotal()) {
            double faltan = cuenta.getTotal() - efectivoRecibido;
            throw new ReglaNegocioException("El efectivo es insuficiente. Faltan S/ "
                    + String.format(java.util.Locale.US, "%,.2f", faltan) + ".");
        }
        cuenta.setEfectivoRecibido(efectivoRecibido);
        pedido.setCuentaFinal(cuenta);
        pedido.setEstado(Estado.PAGADA);
        pedido.getMesa().setEstado(Estado.PAGADA);
        return cuenta;
    }

    public void liberarMesa(int numeroMesa) throws ReglaNegocioException {
        Mesa mesa = obtenerMesa(numeroMesa);
        Pedido pedido = pedidosPorMesa.get(numeroMesa);
        if (pedido != null && !pedido.estaPagado()) {
            throw new ReglaNegocioException("No se puede liberar una mesa con un pedido abierto.");
        }
        pedidosPorMesa.remove(numeroMesa);
        mesa.setEstado(Estado.LIBRE);
    }

    public Producto buscarProductoPorNombre(String nombre) throws ReglaNegocioException {
        for (Producto producto : catalogo) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        throw new ReglaNegocioException("El producto \"" + nombre + "\" no existe en el catálogo.");
    }

    public void actualizarPrecioProducto(String nombre, double nuevoPrecio) throws ReglaNegocioException {
        Producto producto = buscarProductoPorNombre(nombre);
        if (nuevoPrecio <= 0) {
            throw new ReglaNegocioException("El precio debe ser un número mayor que 0.");
        }
        producto.setPrecio(nuevoPrecio);
    }

    public double getCargoServicioPorDefecto() {
        return cargoServicioPorDefecto;
    }

    public void aplicarCargoServicio(double cargoServicio, boolean aplicarAPedidosAbiertos)
            throws ReglaNegocioException {
        try {
            this.cargoServicioPorDefecto = comprobarCargoServicio(cargoServicio);
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException(e.getMessage());
        }
        if (aplicarAPedidosAbiertos) {
            for (Pedido pedido : listarMesasAbiertas()) {
                pedido.setCargoServicio(this.cargoServicioPorDefecto);
            }
        }
    }

    private Mesa obtenerMesa(int numeroMesa) throws ReglaNegocioException {
        Mesa mesa = mesas.get(numeroMesa);
        if (mesa == null) {
            throw new ReglaNegocioException("La mesa " + numeroMesa + " no existe.");
        }
        return mesa;
    }

    private Pedido obtenerPedido(int numeroMesa) throws ReglaNegocioException {
        obtenerMesa(numeroMesa);
        Pedido pedido = pedidosPorMesa.get(numeroMesa);
        if (pedido == null) {
            throw new ReglaNegocioException("La mesa " + numeroMesa
                    + " no tiene un pedido abierto. Abra la mesa primero.");
        }
        return pedido;
    }

    private LineaPedido obtenerLinea(Pedido pedido, Producto producto) throws ReglaNegocioException {
        Producto productoDelCatalogo = buscarProductoPorNombre(producto.getNombre());
        LineaPedido linea = pedido.buscarLinea(productoDelCatalogo);
        if (linea == null) {
            throw new ReglaNegocioException("El producto \"" + producto.getNombre()
                    + "\" no está en el pedido de la mesa " + pedido.getMesa().getNumero() + ".");
        }
        return linea;
    }

    private void validarPedidoModificable(Pedido pedido) throws ReglaNegocioException {
        if (pedido.estaPagado()) {
            throw new ReglaNegocioException("El pedido ya fue pagado y no puede modificarse.");
        }
    }

    private void validarCantidad(int cantidad) throws ReglaNegocioException {
        if (cantidad <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser un número mayor que 0.");
        }
    }

    private static double comprobarCargoServicio(double cargoServicio) {
        if (cargoServicio < 0 || cargoServicio > 1) {
            throw new IllegalArgumentException("El cargo de servicio debe estar entre 0% y 100%.");
        }
        return cargoServicio;
    }
}
