
package com.itson.equipo2.battleship_cliente.pattern.mediator;

import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class ViewController implements ViewManager {
    
    private JFrame frame;
    private Map<String, ViewFactory> factories;
    private Map<String, JPanel> pantallas;

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
        frame.revalidate();
        frame.repaint();
        frame.pack();
    }
    
}
