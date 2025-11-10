/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view.util;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class NaveView extends JPanel {

    private boolean dragging;
    private Point offset;
    private JPanel tablero;
    private static final int CELL_SIZE = 57;

    public NaveView(TipoNave tipo) {
        // 🔹 El tamaño real de la nave (horizontal)
        int ancho = (CELL_SIZE * tipo.getTamanio()) - CELL_SIZE;
        int alto = CELL_SIZE;

        setSize(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.GRAY);
        setOpaque(true);

        // 🔹 Dibujar el borde manualmente para no alterar el tamaño real
        setBorder(null);

        // 🔹 Permitir volver a arrastrar después de haber sido soltada
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point punto = e.getPoint();
                Point puntoEnLayered = SwingUtilities.convertPoint(NaveView.this, punto, getParent());
                startDragFrom(puntoEnLayered);
            }
        });
    }

    public void startDragFromEvent(MouseEvent e) {
        Point puntoEnLayered = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), getParent());
        startDragFrom(puntoEnLayered);
    }

    public void startDragFrom(Point mousePosLayered) {
        Container parent = getParent();
        if (parent == null) {
            return;
        }

        Point barcoOnScreen = getLocationOnScreen();
        Point mouseOnScreen = MouseInfo.getPointerInfo().getLocation();
        offset = new Point(mouseOnScreen.x - barcoOnScreen.x, mouseOnScreen.y - barcoOnScreen.y);
        dragging = true;

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (!(event instanceof MouseEvent)) {
                    return;
                }
                MouseEvent me = (MouseEvent) event;

                if (me.getID() == MouseEvent.MOUSE_RELEASED) {
                    dragging = false;
                    Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                    return;
                }

                if (me.getID() == MouseEvent.MOUSE_DRAGGED && dragging) {
                    Point mouseParent = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), parent);

                    int newX = mouseParent.x - offset.x;
                    int newY = mouseParent.y - offset.y;

                    if (tablero == null) {
                        tablero = buscarTablero(parent);
                    }

                    if (tablero != null) {
                        Rectangle bounds = SwingUtilities.convertRectangle(tablero.getParent(), tablero.getBounds(), parent);
                        newX = Math.max(bounds.x, Math.min(newX, bounds.x + bounds.width - getWidth()));
                        newY = Math.max(bounds.y, Math.min(newY, bounds.y + bounds.height - getHeight()));
                    }

                    setLocation(newX, newY);
                    parent.repaint();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    private JPanel buscarTablero(Container parent) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JPanel && "tablero".equals(c.getName())) {
                return (JPanel) c;
            }
            if (c instanceof Container) {
                JPanel found = buscarTablero((Container) c);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // 🔹 Borde visual sin alterar tamaño
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
    }
}
