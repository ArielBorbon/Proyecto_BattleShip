package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaManager;
import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class VistaController implements VistaManager {

    private final JFrame frame;
    private final Map<String, VistaFactory> factories;
    private final Map<String, JPanel> pantallas;

    private JPanel pantallaActual;
    
    private final PartidaModel partidaModel;

    public VistaController(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
        
        frame = new JFrame("Battleship");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        factories = new HashMap<>();
        pantallas = new HashMap<>();

        frame.setVisible(true);
    }

    @Override
    public void registrarPantalla(String nombre, VistaFactory factory) {
        factories.put(nombre, factory);
    }

    @Override
    public void cambiarPantalla(String nombre) {
        JPanel panel = pantallas.get(nombre);

        if (panel == null) {
            VistaFactory factory = factories.get(nombre);
            if (factory == null) {
                throw new IllegalArgumentException("Pantalla no registrada: " + nombre);
            }
            panel = factory.crear(this);
            
            if (panel instanceof PartidaObserver observer) {
                partidaModel.addObserver(observer);
            }
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
        if (pantallaActual instanceof DispararView) {

            javax.swing.SwingUtilities.invokeLater(() -> {
                DispararView vista = (DispararView) pantallaActual;
                vista.mostrarNotificacion(resultado);
            });
        }
    }
}
