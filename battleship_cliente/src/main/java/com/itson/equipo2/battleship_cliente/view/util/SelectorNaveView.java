/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class SelectorNaveView extends JPanel {

    private final TipoNave tipo;
    private int barcosRestantes;
    private final JLabel lblTitulo;
    private final JLabel lblCantidad;

    public SelectorNaveView(TipoNave tipo, int cantidadInicial) {
        this.tipo = tipo;
        this.barcosRestantes = cantidadInicial;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(200, 80));

        lblTitulo = new JLabel(tipo.name(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCantidad = new JLabel("Disponibles: " + barcosRestantes, SwingConstants.CENTER);

        add(lblTitulo, BorderLayout.NORTH);
        add(lblCantidad, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (barcosRestantes <= 0) {
                    return;
                }

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SelectorNaveView.this);
                if (frame == null) {
                    return;
                }

                JLayeredPane layered = frame.getLayeredPane();

                // Crear la nueva nave del tipo correspondiente
                NaveView nave = new NaveView(tipo);

                // Posición inicial basada en el clik
                Point puntoEnLayered = SwingUtilities.convertPoint(SelectorNaveView.this, e.getPoint(), layered);
                nave.setLocation(puntoEnLayered.x - nave.getWidth() / 2, puntoEnLayered.y - nave.getHeight() / 2);

                // Añadir a la capa superior
                layered.add(nave, JLayeredPane.DRAG_LAYER);
                layered.revalidate();
                layered.repaint();

                // Actualizar contador
                barcosRestantes--;
                lblCantidad.setText("Disponibles: " + barcosRestantes);

                // Comenzar arrastre
                nave.startDragFromEvent(e);
            }
        });
    }

    public void devolverBarco() {
        barcosRestantes++;
        lblCantidad.setText("Disponibles: " + barcosRestantes);
    }
}
