package com.itson.equipo2.battleship_cliente.view.util;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 * Representa la vista de un selector en el panel lateral, permitiendo al
 * usuario elegir un tipo de nave para arrastrarla y posicionarla en el tablero.
 * <p>
 * Su principal función es crear instancias de {@code NaveView} cuando se hace
 * clic, iniciar el proceso de arrastre y gestionar el conteo de naves
 * disponibles de ese tipo.
 * </p>
 */
public class SelectorNaveView extends JPanel {

    // --- Variables de Estado y Componentes ---
    /**
     * El tipo de nave que este selector representa (ej. ACORAZADO, SUBMARINO).
     */
    private final TipoNave tipo;

    /**
     * Contador de cuántas naves de este tipo quedan por posicionar.
     */
    private int barcosRestantes;

    /**
     * Etiqueta que muestra el nombre del tipo de nave.
     */
    private final JLabel lblTitulo;

    /**
     * Etiqueta que muestra la cantidad de naves disponibles.
     */
    private final JLabel lblCantidad;

    /**
     * Referencia al panel del tablero donde se intentarán soltar las naves.
     */
    private final JPanel tablero;

    /**
     * Referencia al controlador de posicionamiento para la lógica de negocio
     * (MVC).
     */
    private final PosicionarController posicionarController;
    
    
    private Color colorJugador = Color.GRAY; // Color del jugador por defecto

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el selector visual para un tipo de nave específico.
     *
     * @param tipo El tipo de nave que gestionará este selector.
     * @param tablero El panel del tablero del jugador.
     * @param posicionarController El controlador de posicionamiento.
     */
    public SelectorNaveView(TipoNave tipo, JPanel tablero, PosicionarController posicionarController) {
        this.tipo = tipo;
        // Inicializa el contador con la cantidad predefinida para este tipo de nave.
        this.barcosRestantes = tipo.getCantidadInicial();
        this.tablero = tablero;
        this.posicionarController = posicionarController;

        // 1. Configuración Visual
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(200, 80));

        // Configuración de etiquetas
        lblTitulo = new JLabel(tipo.name(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCantidad = new JLabel("Disponibles: " + barcosRestantes, SwingConstants.CENTER);

        add(lblTitulo, BorderLayout.NORTH);
        add(lblCantidad, BorderLayout.SOUTH);

        // 2. Lógica de Interacción (Click para iniciar Drag-and-Drop)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Previene la acción si no quedan barcos disponibles
                if (barcosRestantes <= 0) {
                    return;
                }

                // 2.1. Obtener la referencia a la ventana principal (JFrame)
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SelectorNaveView.this);
                if (frame == null) {
                    return;
                }

                // El JLayeredPane es el lugar ideal para el arrastre, ya que está
                // sobre todos los demás componentes y permite el movimiento libre.
                JLayeredPane layered = frame.getLayeredPane();

                // 2.2. Crear la vista arrastrable
                NaveView nave = new NaveView(tipo, tablero, posicionarController, colorJugador);

                // 2.3. Posición inicial: centrar la nave bajo el cursor
                // Convierte las coordenadas del clic de este SelectorNaveView a las del JLayeredPane
                Point puntoEnLayered = SwingUtilities.convertPoint(SelectorNaveView.this, e.getPoint(), layered);
                // Coloca la nave centrada alrededor del punto del clic
                nave.setLocation(puntoEnLayered.x - nave.getWidth() / 2, puntoEnLayered.y - nave.getHeight() / 2);

                // 2.4. Suscribirse al evento de "naveDevuelta"
                // Si la NaveView se suelta fuera del tablero o hay una colisión,
                // emite un evento para notificar al Selector que debe devolver la cuenta.
                nave.addPropertyChangeListener("naveDevuelta", (evt) -> {
                    devolverBarco();
                });

                // 2.5. Añadir y actualizar la interfaz
                layered.add(nave, JLayeredPane.DRAG_LAYER); // Añade a la capa superior
                layered.revalidate();
                layered.repaint();

                // 2.6. Actualizar el estado del Selector
                barcosRestantes--;
                lblCantidad.setText("Disponibles: " + barcosRestantes);

                // 2.7. Iniciar el arrastre
                // Se llama al método de la NaveView para que inicie la escucha de eventos de arrastre
                nave.startDragFromEvent(e);
            }
        });
    }

    /**
     * Incrementa el contador de naves disponibles y actualiza la etiqueta. Este
     * método se llama cuando una {@code NaveView} es arrastrada y no se puede
     * posicionar (colisión o fuera del tablero).
     */
    public void devolverBarco() {
        barcosRestantes++;
        lblCantidad.setText("Disponibles: " + barcosRestantes);
    }

    public Color getColorJugador() {
        return colorJugador;
    }

    public void setColorJugador(Color colorJugador) {
        this.colorJugador = colorJugador;
    }
    
    

    /**
     * Obtiene la cantidad de naves de este tipo que quedan por posicionar.
     *
     * @return El número de barcos restantes.
     */
    public int getBarcosRestantes() {
        return barcosRestantes;
    }

}
