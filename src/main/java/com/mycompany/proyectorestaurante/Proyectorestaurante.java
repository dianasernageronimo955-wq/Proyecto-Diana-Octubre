/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectorestaurante;

import com.mycompany.proyectorestaurante.servicio.PedidoService;
import com.mycompany.proyectorestaurante.ui.VentanaPrincipal;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Proyectorestaurante {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception otra) {
                        UIManager.put("Nimbus", "false");
                    }
                }
                PedidoService servicio = new PedidoService();
                JFrame ventana = new VentanaPrincipal(servicio);
                ventana.setVisible(true);
            }
        });
    }
}
