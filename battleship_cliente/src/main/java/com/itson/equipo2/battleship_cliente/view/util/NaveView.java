package com.itson.equipo2.battleship_cliente.view.util;

// Importaciones necesarias
import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
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

    // --- Referencias de Arquitectura ---
    private final JPanel tablero;
    private final TipoNave tipo;
    private final PosicionarController partidaController; // <-- ¡NUEVO! Habla con el Controlador

    // --- Estado Visual ---
    private boolean esHorizontal = true;
    private static final int CELL_SIZE = 57;

    // --- CONSTRUCTOR MODIFICADO ---
    // Ya no recibe la lista, ahora recibe el Controlador
    public NaveView(TipoNave tipo, JPanel tablero, PosicionarController controller) {
        this.tipo = tipo;
        this.tablero = tablero;
        this.partidaController = controller; // <-- Asignar el controlador

        // --- Configuración Visual ---
        // (Usamos tu cálculo de ancho visual, la lógica de 'soltar' lo compensará)
        int ancho = CELL_SIZE * tipo.getTamanio();
        int alto = CELL_SIZE;

        setSize(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(new Color(100, 100, 100, 200));
        setOpaque(false);

        // --- 2. Lógica de Arrastre, Rotación y MVC ---
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                // --- 1. LÓGICA DE TECLADO (Rotar con 'R') ---
                if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;
                    if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_R && dragging) {
                        rotarNave();
                    }
                    return; // Termina el evento de teclado
                }

                // --- 2. LÓGICA DE MOUSE ---
                if (!(event instanceof MouseEvent)) {
                    return;
                }
                MouseEvent me = (MouseEvent) event;

                // --- A. INICIO DEL ARRASTRE (Clic Izquierdo) ---
                if (me.getID() == MouseEvent.MOUSE_PRESSED && me.getComponent() == NaveView.this && SwingUtilities.isLeftMouseButton(me)) {
                    // Ya no se quita de ninguna lista, solo empieza a arrastrar
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

                // --- C. SOLTAR LA NAVE (Lógica MVC) ---
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

                        boolean exito = false;

                        // 1. SI ESTÁ DENTRO DEL TABLERO...
                        if (tableroBounds.contains(naveCenter)) {
                            // Calcular celda (targetCol, targetFila) basado en la esquina (getX, getY)
                            int relativeX = getX() - tableroBounds.x;
                            int relativeY = getY() - tableroBounds.y;
                            int targetCol = Math.round((float) relativeX / CELL_SIZE);
                            int targetFila = Math.round((float) relativeY / CELL_SIZE);

                            // Validar límites visuales (para que el 'snap' no se salga)
                            int numColsNave, numFilasNave;
                            if (esHorizontal) {
                                numColsNave = (getWidth() / CELL_SIZE); // (ej. 3)
                                numFilasNave = getHeight() / CELL_SIZE; // (ej. 1)
                            } else {
                                numColsNave = getWidth() / CELL_SIZE; // (ej. 1)
                                numFilasNave = (getHeight() / CELL_SIZE); // (ej. 3)
                            }
                            targetCol = Math.max(0, Math.min(targetCol, 10 - numColsNave));
                            targetFila = Math.max(0, Math.min(targetFila, 10 - numFilasNave));

                            // 2. ¡LLAMAR AL CONTROLADOR!
                            // Esta es la nueva lógica MVC
                            exito = partidaController.intentarPosicionarNave(tipo, targetFila, targetCol, esHorizontal);
                        }

                        // 3. SI FALLÓ (colisión) O SE SOLTÓ FUERA...
                        if (!exito) {
                            // Notificar al SelectorNaveView que debe devolver el barco
                            firePropertyChange("naveDevuelta", false, true);
                        }

                        // 4. ¡SIEMPRE AUTO-DESTRUIRSE!
                        // El NaveView ya cumplió su propósito (enviar el evento al C).
                        parent.remove(NaveView.this);
                        parent.repaint();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK); // <-- MÁSCARA MODIFICADA
    }

    /**
     * Inicia el proceso de arrastre. Ya no necesita 'buscarTablero' porque lo
     * recibe en el constructor.
     */
    public void startDragFromEvent(MouseEvent e) {
        Container parent = getParent();
        if (parent == null || this.tablero == null) {
            return; // 'tablero' ya es una variable de clase
        }
        Point eventPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), parent);
        this.offset = new Point(eventPoint.x - getX(), eventPoint.y - getY());
        this.dragging = true;
    }

    /**
     * Dibuja la nave (el rectángulo y el borde).
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

    /**
     * Rota la nave 90 grados sobre el eje del puntero del mouse (this.offset).
     * Esta es la versión "compleja" que gira sobre el eje.
     */
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
        int newX = globalPivotX - newOffsetX;
        int newY = globalPivotY - newOffsetY;

        // 7. Aplicar todos los cambios
        this.esHorizontal = !this.esHorizontal;
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
}
