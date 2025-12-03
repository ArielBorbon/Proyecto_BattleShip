package com.itson.equipo2.battleship_cliente.view.util;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

public class SelectorNaveView extends JPanel {

    private final TipoNave tipo;
    private int barcosRestantes;
    private final JLabel lblCantidad;
    private final JPanel tablero;
    private final PosicionarController posicionarController;
    private Color colorJugador = Color.GRAY;

    // Imagen generada por la fábrica
    private BufferedImage imagenBarco;

    // Constante para el tamaño base de celda (debe coincidir con el resto del juego)
    private static final int CELL_SIZE = 57;

    public SelectorNaveView(TipoNave tipo, JPanel tablero, PosicionarController posicionarController) {
        this.tipo = tipo;
        this.barcosRestantes = tipo.getCantidadInicial();
        this.tablero = tablero;
        this.posicionarController = posicionarController;

        setLayout(new GridBagLayout()); // Mantiene el número centrado
        setOpaque(false); // Importante: fondo transparente para ver solo la silueta

        // Generamos la imagen inicial
        actualizarImagen();

        // Estilo del número (Blanco y Grande para que se vea sobre el barco)
        lblCantidad = new JLabel(String.valueOf(barcosRestantes), SwingConstants.CENTER);
        lblCantidad.setForeground(Color.WHITE);
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 24));
        // Sombra negra simple al texto para asegurar legibilidad en barcos claros
        // (Opcional, pero ayuda mucho en UI de juegos)

        add(lblCantidad);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                if (barcosRestantes <= 0) {
                    return;
                }

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SelectorNaveView.this);
                if (frame == null) {
                    return;
                }

                JLayeredPane layered = frame.getLayeredPane();
                // La NaveView ya sabe generar su propia imagen internamente ahora
                NaveView nave = new NaveView(tipo, tablero, posicionarController, colorJugador);

                Point puntoEnLayered = SwingUtilities.convertPoint(SelectorNaveView.this, e.getPoint(), layered);
                nave.setLocation(puntoEnLayered.x - nave.getWidth() / 2, puntoEnLayered.y - nave.getHeight() / 2);

                nave.addPropertyChangeListener("naveDevuelta", (evt) -> {
                    devolverBarco();
                });

                layered.add(nave, JLayeredPane.DRAG_LAYER);
                layered.revalidate();
                layered.repaint();

                // Lógica de actualización
                barcosRestantes--;
                lblCantidad.setText(String.valueOf(barcosRestantes));

                if (barcosRestantes == 0) {
                    setVisible(false); // Ocultar el selector si se acabaron
                }

                repaint();
                nave.startDragFromEvent(e);
            }
        });
    }

    /**
     * Regenera la imagen del barco usando la fábrica.
     */
    private void actualizarImagen() {
        // Usamos la misma fábrica que creaste anteriormente
        this.imagenBarco = BarcoImageFactory.createImagenBarco(tipo, CELL_SIZE, colorJugador);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujamos la imagen solo si hay barcos disponibles
        if (imagenBarco != null && barcosRestantes > 0) {
            Graphics2D g2 = (Graphics2D) g;

            // Renderizado de alta calidad
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Dibujar la imagen ocupando todo el panel
            g2.drawImage(imagenBarco, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void devolverBarco() {
        barcosRestantes++;
        lblCantidad.setText(String.valueOf(barcosRestantes));

        if (barcosRestantes > 0) {
            setVisible(true);
            repaint();
        }
    }

    public Color getColorJugador() {
        return colorJugador;
    }

    public void setColorJugador(Color colorJugador) {
        this.colorJugador = colorJugador;
        // Importante: Regenerar la imagen si cambia el color (ej. al conectar al server)
        actualizarImagen();
        repaint();
    }

    public int getBarcosRestantes() {
        return barcosRestantes;
    }

    public void reiniciarSelector() {
        this.barcosRestantes = tipo.getCantidadInicial();
        lblCantidad.setText(String.valueOf(barcosRestantes));

        // Asegurar visibilidad y repintado
        setVisible(true);
        this.revalidate();
        this.repaint();
    }

    public TipoNave getTipo() {
        return tipo;
    }
}
