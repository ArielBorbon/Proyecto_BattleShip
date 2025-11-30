package com.itson.equipo2.battleship_cliente.view.util;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
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

public class SelectorNaveView extends JPanel {

    private final TipoNave tipo;
    private int barcosRestantes;
    private final JLabel lblCantidad;
    private final JPanel tablero;
    private final PosicionarController posicionarController;
    private Color colorJugador = Color.GRAY;

    // Colores constantes para fácil modificación
    private final Color COLOR_DISPONIBLE = Color.LIGHT_GRAY;

    public SelectorNaveView(TipoNave tipo, JPanel tablero, PosicionarController posicionarController) {
        this.tipo = tipo;
        this.barcosRestantes = tipo.getCantidadInicial();
        this.tablero = tablero;
        this.posicionarController = posicionarController;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        setBackground(COLOR_DISPONIBLE); // Color inicial
        setPreferredSize(new Dimension(200, 80));

        lblCantidad = new JLabel(String.valueOf(barcosRestantes), SwingConstants.CENTER);
        lblCantidad.setForeground(Color.BLACK); // Texto negro inicialmente

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
                NaveView nave = new NaveView(tipo, tablero, posicionarController, colorJugador);

                Point puntoEnLayered = SwingUtilities.convertPoint(SelectorNaveView.this, e.getPoint(), layered);
                nave.setLocation(puntoEnLayered.x - nave.getWidth() / 2, puntoEnLayered.y - nave.getHeight() / 2);

                nave.addPropertyChangeListener("naveDevuelta", (evt) -> {
                    devolverBarco();
                });

                layered.add(nave, JLayeredPane.DRAG_LAYER);
                layered.revalidate();
                layered.repaint();

                // --- LÓGICA DE ACTUALIZACIÓN VISUAL ---
                barcosRestantes--;
                lblCantidad.setText(String.valueOf(barcosRestantes));

                // Si ya no quedan barcos, cambiar a NEGRO (deshabilitado)
                if (barcosRestantes == 0) {
                    setVisible(false);
                }

                repaint(); // Forzar repintado para asegurar cambio de color inmediato
                // --------------------------------------

                nave.startDragFromEvent(e);
            }
        });
    }

    public void devolverBarco() {
        barcosRestantes++;
        lblCantidad.setText(String.valueOf(barcosRestantes));

        // Si recuperamos un barco (ahora hay > 0), volver a color GRIS (habilitado)
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
    }

    public int getBarcosRestantes() {
        return barcosRestantes;
    }

    public void reiniciarSelector() {
        this.barcosRestantes = tipo.getCantidadInicial();
        lblCantidad.setText(String.valueOf(barcosRestantes));

        // Restaurar colores originales
        setBackground(COLOR_DISPONIBLE);
        setVisible(true);

        this.revalidate();
        this.repaint();
    }

    public TipoNave getTipo() {
        return tipo;
    }
}
