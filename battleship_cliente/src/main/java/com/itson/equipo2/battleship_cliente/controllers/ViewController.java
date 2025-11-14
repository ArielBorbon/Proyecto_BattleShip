package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.mediator.ViewManager;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class ViewController implements ViewManager {

    private JFrame frame;
    private Map<String, ViewFactory> factories;
    private Map<String, JPanel> pantallas;

    private JPanel pantallaActual;

    public ViewController() {
        frame = new JFrame("App con Mediator");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        factories = new HashMap<>();
        pantallas = new HashMap<>();

        frame.setVisible(true);
    }

    @Override
    public void registrarPantalla(String nombre, ViewFactory factory) {
        factories.put(nombre, factory);
    }

    @Override
    public void cambiarPantalla(String nombre) {
        JPanel panel = pantallas.get(nombre);

        if (panel == null) {
            ViewFactory factory = factories.get(nombre);
            if (factory == null) {
                throw new IllegalArgumentException("Pantalla no registrada: " + nombre);
            }
            panel = factory.crear(this);
            pantallas.put(nombre, panel);
        }

        frame.setContentPane(panel);

        this.pantallaActual = panel;

        frame.revalidate();
        frame.repaint();
        frame.pack();
    }

    public void mostrarError(String message) {
        if (frame != null) {
            JOptionPane.showMessageDialog(
                    frame,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } else {
            System.err.println("Error: " + message);
        }
    }

    public void mostrarResultadoDisparo(ResultadoDisparo resultado) {
        // Verificamos si la pantalla actual es la que sabe mostrar notificaciones
        if (pantallaActual instanceof DispararView) {

            // Usamos invokeLater para asegurar que se ejecute en el hilo de Swing
            javax.swing.SwingUtilities.invokeLater(() -> {
                DispararView vista = (DispararView) pantallaActual;
                vista.mostrarNotificacion(resultado);
            });
        }
    }
}
