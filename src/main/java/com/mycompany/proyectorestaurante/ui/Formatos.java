package com.mycompany.proyectorestaurante.ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Formatos {

    private static final String SIMBOLO = "S/ ";

    private Formatos() {
    }

    public static String moneda(double valor) {
        return SIMBOLO + numero(valor);
    }

    public static String porcentaje(double valor) {
        return numero(valor * 100) + " %";
    }

    public static String numero(double valor) {
        return formato().format(valor);
    }

    public static String entero(double valor) {
        return String.valueOf((int) Math.round(valor));
    }

    private static DecimalFormat formato() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.ROOT);
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        return new DecimalFormat("#,##0.00", simbolos);
    }

    public static double leerMonto(String texto) {
        if (texto == null) {
            return -1;
        }
        String limpio = texto.trim().replace("S/", "").replace("%", "").replace(" ", "").replace(" ", "");
        if (limpio.isEmpty()) {
            return -1;
        }
        int coma = limpio.lastIndexOf(',');
        int punto = limpio.lastIndexOf('.');
        if (coma >= 0 && punto >= 0) {
            limpio = limpiarConSeparadores(limpio, Math.max(coma, punto));
        } else if (coma >= 0 || punto >= 0) {
            int separador = Math.max(coma, punto);
            int decimales = limpio.length() - separador - 1;
            limpio = decimales == 3 ? quitarSeparador(limpio, separador)
                    : limpiarConSeparadores(limpio, separador);
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String limpiarConSeparadores(String texto, int posicionDecimal) {
        String entero = texto.substring(0, posicionDecimal).replace(",", "").replace(".", "");
        return entero + "." + texto.substring(posicionDecimal + 1);
    }

    private static String quitarSeparador(String texto, int posicion) {
        return texto.substring(0, posicion) + texto.substring(posicion + 1);
    }

    public static int leerCantidad(String texto) {
        double valor = leerMonto(texto);
        if (valor < 1 || valor != Math.floor(valor)) {
            return -1;
        }
        return (int) valor;
    }
}
