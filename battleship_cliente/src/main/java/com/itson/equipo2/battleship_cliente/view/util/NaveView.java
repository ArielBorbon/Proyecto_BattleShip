package com.itson.equipo2.battleship_cliente.view.util;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.exceptions.PosicionarNaveException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

public class NaveView extends JPanel {

    private boolean dragging = false;
    // NUEVO: Bandera para saber si esta instancia se puede mover o está fija
    private boolean isDraggable = true;

    private Point offset;
    private final JPanel tablero;
    private final TipoNave tipo;
    private final PosicionarController partidaController;
    private boolean esHorizontal = true;
    private static final int CELL_SIZE = 57;

    // NUEVO: La imagen generada
    private final BufferedImage imagenBarco;

    public NaveView(TipoNave tipo, JPanel tablero, PosicionarController controller, Color colorJugador) {
        this.tipo = tipo;
        this.tablero = tablero;
        this.partidaController = controller;
        this.setFocusable(true);

        // Generar imagen usando la Factory
        this.imagenBarco = BarcoImageFactory.createImagenBarco(tipo, CELL_SIZE, colorJugador);

        int ancho = CELL_SIZE * tipo.getTamanio();
        int alto = CELL_SIZE;

        setSize(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));

        // Fondo transparente, ya que dibujaremos la imagen
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                // Si no es arrastrable, ignoramos toda la lógica de Drag & Drop global
                if (!isDraggable) {
                    return;
                }

                if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;
                    if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_R && dragging) {
                        rotarNave();
                    }
                    return;
                }

                if (!(event instanceof MouseEvent)) {
                    return;
                }
                MouseEvent me = (MouseEvent) event;

                if (me.getID() == MouseEvent.MOUSE_PRESSED && me.getComponent() == NaveView.this && SwingUtilities.isLeftMouseButton(me)) {
                    startDragFromEvent(me);
                }

                if (me.getID() == MouseEvent.MOUSE_DRAGGED && dragging) {
                    Container parent = getParent();
                    if (parent != null) {
                        Point newPoint = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), parent);
                        int newX = newPoint.x - offset.x;
                        int newY = newPoint.y - offset.y;
                        setLocation(newX, newY);
                        parent.repaint();
                    }
                }

                if (me.getID() == MouseEvent.MOUSE_RELEASED && dragging && SwingUtilities.isLeftMouseButton(me)) {
                    finalizarArrastre();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    // Método extraído para limpieza
    private void finalizarArrastre() {
        dragging = false;
        Container parent = getParent();
        if (parent == null || tablero == null) {
            return;
        }

        Rectangle tableroBounds = SwingUtilities.convertRectangle(tablero.getParent(), tablero.getBounds(), parent);
        Point naveCenter = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);

        if (tableroBounds.contains(naveCenter)) {
            int relativeX = getX() - tableroBounds.x;
            int relativeY = getY() - tableroBounds.y;
            int targetCol = Math.round((float) relativeX / CELL_SIZE);
            int targetFila = Math.round((float) relativeY / CELL_SIZE);

            int numColsNave = esHorizontal ? (getWidth() / CELL_SIZE) : 1;
            int numFilasNave = esHorizontal ? 1 : (getHeight() / CELL_SIZE);
            targetCol = Math.max(0, Math.min(targetCol, 10 - numColsNave));
            targetFila = Math.max(0, Math.min(targetFila, 10 - numFilasNave));

            try {
                partidaController.intentarPosicionarNave(tipo, targetFila, targetCol, esHorizontal);
            } catch (PosicionarNaveException ex) {
                firePropertyChange("naveDevuelta", false, true);
            }
        } else {
            firePropertyChange("naveDevuelta", false, true);
        }
        parent.remove(NaveView.this);
        parent.repaint();
    }

    public void startDragFromEvent(MouseEvent e) {
        if (!isDraggable) {
            return; // Doble check
        }
        Container parent = getParent();
        if (parent == null || this.tablero == null) {
            return;
        }
        this.requestFocusInWindow();
        Point eventPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), parent);
        this.offset = new Point(eventPoint.x - getX(), eventPoint.y - getY());
        this.dragging = true;
    }

    // NUEVO: Renderizado con imagen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Configuración gráfica óptima
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (imagenBarco != null) {
            if (esHorizontal) {
                g2.drawImage(imagenBarco, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Rotación de 90 grados para dibujarlo vertical
                java.awt.geom.AffineTransform old = g2.getTransform();
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.rotate(Math.toRadians(90));
                g2.drawImage(imagenBarco, -imagenBarco.getWidth() / 2, -imagenBarco.getHeight() / 2, null);
                g2.setTransform(old);
            }
        }
    }

    private void rotarNave() {
        int anchoActual = getWidth();
        int altoActual = getHeight();
        int globalPivotX = getX() + offset.x;
        int globalPivotY = getY() + offset.y;
        double centroX = (double) anchoActual / 2;
        double centroY = (double) altoActual / 2;
        double vecRelX = offset.x - centroX;
        double vecRelY = offset.y - centroY;
        double vecRotadoX = -vecRelY;
        double vecRotadoY = vecRelX;
        int nuevoAncho = altoActual;
        int nuevoAlto = anchoActual;
        double nuevoCentroX = (double) nuevoAncho / 2;
        double nuevoCentroY = (double) nuevoAlto / 2;
        int newOffsetX = (int) Math.round(nuevoCentroX + vecRotadoX);
        int newOffsetY = (int) Math.round(nuevoCentroY + vecRotadoY);
        int newX = globalPivotX - newOffsetX;
        int newY = globalPivotY - newOffsetY;

        this.esHorizontal = !this.esHorizontal;
        this.offset = new Point(newOffsetX, newOffsetY);
        setSize(nuevoAncho, nuevoAlto);
        setLocation(newX, newY);

        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }

    public void setOrientacion(boolean horizontal) {
        this.esHorizontal = horizontal;
        // Intercambiar dimensiones si cambia la orientación
        if (!horizontal && getWidth() > getHeight()) {
            setSize(getHeight(), getWidth());
            setPreferredSize(new Dimension(getHeight(), getWidth()));
        }
    }

    // Setter para deshabilitar arrastre en naves ya colocadas
    public void setDraggable(boolean draggable) {
        this.isDraggable = draggable;
    }
}
