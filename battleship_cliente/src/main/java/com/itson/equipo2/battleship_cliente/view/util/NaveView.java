package com.itson.equipo2.battleship_cliente.view.util;

// Importaciones necesarias
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 * Representa la vista de una nave que puede ser arrastrada y soltada. Versión
 * con AWTEventListener para manejar drag-and-drop global.
 */
public class NaveView extends JPanel {

    // --- Variables de Clase ---
    private boolean dragging = false;
    private Point offset;

    private JPanel tablero;
    private final TipoNave tipo;
    private boolean esHorizontal = true;

    private final java.util.List<NaveView> navesEnTablero;

    // Constante para el tamaño de la celda
    private static final int CELL_SIZE = 57;

    // --- CONSTRUCTOR ---
    public NaveView(TipoNave tipo, JPanel tablero, List<NaveView> navesEnTablero) {
        this.tipo = tipo;
        this.tablero = tablero;
        this.navesEnTablero = navesEnTablero;

        // --- 1. Arreglo del Ancho (+1 celda) ---
        // Volvemos a tu cálculo original. Si esto sigue siendo incorrecto,
        // la discrepancia está entre `TipoNave.getTamanio()` y `CELL_SIZE`.
        int ancho = (CELL_SIZE * tipo.getTamanio()) - CELL_SIZE;
        int alto = CELL_SIZE;

        setSize(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));

        setBackground(new Color(100, 100, 100, 200));
        setOpaque(false); // Para que la transparencia funcione

        // --- 2. Lógica de Arrastre Global (Arregla #2 y #3) ---
        // Este listener se añade una vez por nave y maneja todo.
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                // --- 1. LÓGICA DE TECLADO (NUEVO) ---
                if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;

                    // Si presionan 'R' MIENTRAS arrastramos
                    if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_R && dragging) {
                        rotarNave();
                    }
                    return; // Termina el evento de teclado
                }

                // --- 2. LÓGICA DE MOUSE (MODIFICADA) ---
                if (!(event instanceof MouseEvent)) {
                    return; // Ignora otros eventos
                }

                MouseEvent me = (MouseEvent) event;

                // --- A. INICIO DEL ARRASTRE (Solo Clic Izquierdo) ---
                if (me.getID() == MouseEvent.MOUSE_PRESSED && me.getComponent() == NaveView.this && SwingUtilities.isLeftMouseButton(me)) {
                    // Quitarse de la lista para no colisionar consigo mismo
                    navesEnTablero.remove(NaveView.this);
                    startDragFromEvent(me);
                }

                // --- B. DURANTE EL ARRASTRE (Sigue al cursor) ---
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

                // --- C. SOLTAR LA NAVE (¡SÓLO CON CLIC IZQUIERDO!) ---
                if (me.getID() == MouseEvent.MOUSE_RELEASED) {
                    // Solo actuar si estábamos arrastrando Y si el botón que se soltó fue el izquierdo
                    if (dragging && SwingUtilities.isLeftMouseButton(me)) {
                        dragging = false;

                        Container parent = getParent();
                        if (parent == null || tablero == null) {
                            return;
                        }

                        Rectangle tableroBounds = SwingUtilities.convertRectangle(tablero.getParent(), tablero.getBounds(), parent);
                        Point naveCenter = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);

                        // 1. DEVOLVER SI ESTÁ FUERA
                        if (!tableroBounds.contains(naveCenter)) {
                            firePropertyChange("naveDevuelta", false, true);
                            parent.remove(NaveView.this);
                            parent.repaint();
                            return;
                        }

                        // 2. CALCULAR POSICIÓN "SNAP" (CONSIDERA ROTACIÓN)
                        int relativeX = getX() - tableroBounds.x;
                        int relativeY = getY() - tableroBounds.y;
                        int targetCol = Math.round((float) relativeX / CELL_SIZE);
                        int targetFila = Math.round((float) relativeY / CELL_SIZE);

                        int numColsNave, numFilasNave;
                        if (esHorizontal) {
                            numColsNave = (getWidth() / CELL_SIZE);
                            numFilasNave = getHeight() / CELL_SIZE;
                        } else {
                            numColsNave = getWidth() / CELL_SIZE;
                            numFilasNave = (getHeight() / CELL_SIZE);
                        }

                        targetCol = Math.max(0, Math.min(targetCol, 10 - numColsNave));
                        targetFila = Math.max(0, Math.min(targetFila, 10 - numFilasNave));

                        int newX = tableroBounds.x + (targetCol * CELL_SIZE);
                        int newY = tableroBounds.y + (targetFila * CELL_SIZE);

                        // 3. LÓGICA DE COLISIÓN
                        Rectangle proposedBounds = new Rectangle(newX, newY, getWidth(), getHeight());
                        boolean colision = false;
                        for (NaveView otraNave : navesEnTablero) {
                            if (otraNave.getBounds().intersects(proposedBounds)) {
                                colision = true;
                                break;
                            }
                        }

                        // 4. DEVOLVER SI HAY COLISIÓN
                        if (colision) {
                            firePropertyChange("naveDevuelta", false, true);
                            parent.remove(NaveView.this);
                            parent.repaint();
                            return;
                        }

                        // 5. SI TODO ESTÁ BIEN: Posicionar y añadir a la lista
                        setLocation(newX, newY);
                        navesEnTablero.add(NaveView.this);
                        parent.repaint();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK); // <-- MÁSCARA MODIFICADA
    }

    // --- Métodos Auxiliares ---
    /**
     * Método público que inicia el arrastre. Es llamado por SelectorNaveView y
     * por este mismo listener.
     */
    public void startDragFromEvent(MouseEvent e) {
        Container parent = getParent();
        if (parent == null) {
            return;
        }

        Point eventPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), parent);
        this.offset = new Point(eventPoint.x - getX(), eventPoint.y - getY());
        this.dragging = true;
    }

    /**
     * Dibuja la nave
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void rotarNave() {
        // 1. Obtener dimensiones y offset actuales
        int anchoActual = getWidth();
        int altoActual = getHeight();

        // Punto de pivote (mouse) en coordenadas globales (del JLayeredPane)
        int globalPivotX = getX() + offset.x;
        int globalPivotY = getY() + offset.y;

        // Centro de la nave (relativo a la nave)
        double centroX = (double) anchoActual / 2;
        double centroY = (double) altoActual / 2;

        // 2. Calcular el vector desde el centro de la nave hasta el puntero
        double vecRelX = offset.x - centroX;
        double vecRelY = offset.y - centroY;

        // 3. Rotar ese vector 90 grados (x, y) -> (-y, x)
        double vecRotadoX = -vecRelY;
        double vecRotadoY = vecRelX;

        // 4. Calcular nuevas dimensiones y nuevo centro
        int nuevoAncho = altoActual;
        int nuevoAlto = anchoActual;
        double nuevoCentroX = (double) nuevoAncho / 2;
        double nuevoCentroY = (double) nuevoAlto / 2;

        // 5. Calcular el *nuevo* offset (dónde estará el puntero en la nave rotada)
        int newOffsetX = (int) Math.round(nuevoCentroX + vecRotadoX);
        int newOffsetY = (int) Math.round(nuevoCentroY + vecRotadoY);

        // 6. Calcular la nueva posición TOP-LEFT de la nave
        // (El pivote global no se mueve, así que restamos el nuevo offset)
        int newX = globalPivotX - newOffsetX;
        int newY = globalPivotY - newOffsetY;

        // 7. Aplicar todos los cambios
        this.esHorizontal = !this.esHorizontal; //
        this.offset = new Point(newOffsetX, newOffsetY); // ¡Actualizar el offset!

        setSize(nuevoAncho, nuevoAlto);
        setLocation(newX, newY);

        // 8. Forzar al JLayeredPane a redibujar
        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }

    // --- Getters (útiles para el botón Confirmar) ---
    public TipoNave getTipoNave() {
        return this.tipo;
    }

    public int getColumna() {
        if (tablero == null) {
            return -1;
        }
        Rectangle tableroBounds = tablero.getBounds();
        int relativeX = getX() - tableroBounds.x;
        return Math.round((float) relativeX / CELL_SIZE);
    }

    public int getFila() {
        if (tablero == null) {
            return -1;
        }
        Rectangle tableroBounds = tablero.getBounds();
        int relativeY = getY() - tableroBounds.y;
        return Math.round((float) relativeY / CELL_SIZE);
    }
}
