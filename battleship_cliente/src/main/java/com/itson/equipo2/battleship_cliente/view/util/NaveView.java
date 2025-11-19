package com.itson.equipo2.battleship_cliente.view.util;

// Importaciones necesarias para la interfaz gráfica, manejo de eventos y lógica del juego
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
 * Representa la vista de una **nave (barco) arrastrable** que el jugador puede
 * posicionar sobre el tablero.
 * <p>
 * Esta clase utiliza un {@code AWTEventListener} global para manejar la lógica
 * de
 * <b>Drag-and-Drop (Arrastrar y Soltar)</b> y <b>Rotación (tecla 'R')</b>,
 * desacoplando la nave del contenedor y permitiendo el movimiento libre sobre
 * el panel de la ventana principal.
 * </p>
 */
public class NaveView extends JPanel {

    // --- Variables de Estado y Configuración ---
    /**
     * Indica si la nave está siendo arrastrada actualmente por el usuario.
     */
    private boolean dragging = false;

    /**
     * Almacena la diferencia entre la esquina superior izquierda de la nave
     * (0,0) y el punto exacto donde se hizo clic con el mouse. Es crucial para
     * el arrastre para que la nave no "salte" y se mueva desde donde se hizo
     * clic.
     */
    private Point offset;

    /**
     * Referencia al panel que representa el tablero de juego donde se intentará
     * posicionar la nave.
     */
    private final JPanel tablero;

    /**
     * El tipo de nave que representa esta vista (ej. Acorazado, Submarino), que
     * define su tamaño.
     */
    private final TipoNave tipo;

    /**
     * Referencia al **Controlador** (patrón MVC) que contiene la lógica de
     * negocio para validar si la nave puede ser posicionada o no.
     */
    private final PosicionarController partidaController;

    /**
     * Indica la orientación actual de la nave. {@code true} si es horizontal,
     * {@code false} si es vertical.
     */
    private boolean esHorizontal = true;

    /**
     * Tamaño en píxeles de un solo lado de una celda del tablero. Constante
     * para el cálculo de dimensiones.
     */
    private static final int CELL_SIZE = 57;

    // --- CONSTRUCTOR ---
    /**
     * Crea una nueva vista de nave arrastrable.
     *
     * @param tipo El tipo de nave (tamaño) a crear.
     * @param tablero El panel que representa el tablero del jugador para
     * calcular límites.
     * @param controller El controlador de posicionamiento para la lógica de
     * negocio (MVC).
     */
    public NaveView(TipoNave tipo, JPanel tablero, PosicionarController controller) {
        this.tipo = tipo;
        this.tablero = tablero;
        this.partidaController = controller; // Asigna el controlador para la interacción MVC

        // 1. Configuración Visual Inicial
        int ancho = CELL_SIZE * tipo.getTamanio(); // Ancho inicial según el tamaño de la nave
        int alto = CELL_SIZE;

        setSize(ancho, alto);
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(new Color(100, 100, 100, 200)); // Color con transparencia
        setOpaque(false); // Necesario para que el fondo del contenedor se vea

        // 2. Lógica de Eventos Globales (Arrastre y Rotación)
        // Se añade un listener que captura eventos de mouse y teclado EN TODA LA APLICACIÓN.
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                // --- 1. LÓGICA DE TECLADO (Rotar con 'R') ---
                if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;
                    // Solo si la tecla es 'R', está presionada y la nave está siendo arrastrada.
                    if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_R && dragging) {
                        rotarNave();
                    }
                    return; // Ignora el resto si es un evento de teclado
                }

                // --- 2. LÓGICA DE MOUSE ---
                if (!(event instanceof MouseEvent)) {
                    return;
                }
                MouseEvent me = (MouseEvent) event;

                // --- A. INICIO DEL ARRASTRE (MOUSE_PRESSED) ---
                // Verifica que el evento sea el clic inicial, que haya ocurrido sobre ESTA nave
                // y que sea el botón izquierdo.
                if (me.getID() == MouseEvent.MOUSE_PRESSED && me.getComponent() == NaveView.this && SwingUtilities.isLeftMouseButton(me)) {
                    startDragFromEvent(me); // Inicia el arrastre
                }

                // --- B. DURANTE EL ARRASTRE (MOUSE_DRAGGED) ---
                // Solo mueve la nave si el estado 'dragging' es verdadero.
                if (me.getID() == MouseEvent.MOUSE_DRAGGED && dragging) {
                    Container parent = getParent();
                    if (parent != null) {
                        // Convierte la posición del mouse al sistema de coordenadas del contenedor padre.
                        Point newPoint = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), parent);
                        // Resta el 'offset' para mantener la posición relativa al clic inicial.
                        int newX = newPoint.x - offset.x;
                        int newY = newPoint.y - offset.y;
                        setLocation(newX, newY);
                        parent.repaint(); // Fuerza el redibujo del contenedor
                    }
                }

                // --- C. SOLTAR LA NAVE (MOUSE_RELEASED) ---
                if (me.getID() == MouseEvent.MOUSE_RELEASED) {
                    // Solo procede si la nave estaba siendo arrastrada y se soltó el botón izquierdo.
                    if (dragging && SwingUtilities.isLeftMouseButton(me)) {
                        dragging = false; // Finaliza el estado de arrastre

                        Container parent = getParent();
                        if (parent == null || tablero == null) {
                            return; // Se necesita un padre y una referencia al tablero
                        }

                        // Calcula las coordenadas del tablero relativas al contenedor padre (generalmente un JLayeredPane)
                        Rectangle tableroBounds = SwingUtilities.convertRectangle(tablero.getParent(), tablero.getBounds(), parent);
                        // Calcula el centro de la nave en coordenadas del contenedor padre
                        Point naveCenter = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);

                        boolean exito = false;

                        // 1. Verifica si el centro de la nave está sobre los límites visuales del tablero
                        if (tableroBounds.contains(naveCenter)) {
                            // Calcula la posición relativa al tablero (coordenadas de la celda)
                            int relativeX = getX() - tableroBounds.x;
                            int relativeY = getY() - tableroBounds.y;
                            // Calcula la celda de inicio redondeando al múltiplo de CELL_SIZE más cercano
                            int targetCol = Math.round((float) relativeX / CELL_SIZE);
                            int targetFila = Math.round((float) relativeY / CELL_SIZE);

                            // Ajuste para evitar que la nave se salga del borde del tablero (ej. un barco de 3 celdas en Col 9)
                            int numColsNave = esHorizontal ? (getWidth() / CELL_SIZE) : 1;
                            int numFilasNave = esHorizontal ? 1 : (getHeight() / CELL_SIZE);

                            // Asegura que la celda de inicio esté dentro de [0, 10 - tamaño_nave]
                            targetCol = Math.max(0, Math.min(targetCol, 10 - numColsNave));
                            targetFila = Math.max(0, Math.min(targetFila, 10 - numFilasNave));

                            // 2. ¡LLAMADA AL CONTROLADOR!
                            // Se intenta posicionar la nave en la cuadrícula del Modelo.
                            exito = partidaController.intentarPosicionarNave(tipo, targetFila, targetCol, esHorizontal);
                        }

                        // 3. Manejo de Fallo
                        if (!exito) {
                            // Si el posicionamiento falló (colisión o fuera del tablero),
                            // se notifica al panel que contiene la lista de naves para que la devuelva.
                            firePropertyChange("naveDevuelta", false, true);
                        }

                        // 4. AUTO-DESTRUCCIÓN
                        // La NaveView solo existe para capturar el arrastre. Una vez enviado el
                        // intento de posicionamiento (exitoso o fallido), se elimina de la interfaz.
                        parent.remove(NaveView.this);
                        parent.repaint(); // Actualiza el contenedor para eliminar la vista
                    }
                }
            }
            // Se define qué eventos deben ser escuchados: mouse y teclado.
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Inicializa el estado de arrastre (`dragging = true`) y calcula el
     * `offset`. Convierte las coordenadas del clic al sistema de coordenadas
     * del padre.
     *
     * @param e El evento de mouse de tipo {@code MOUSE_PRESSED}.
     */
    public void startDragFromEvent(MouseEvent e) {
        Container parent = getParent();
        if (parent == null || this.tablero == null) {
            return;
        }
        // Convierte el punto del clic de las coordenadas de la NaveView a las del contenedor padre.
        Point eventPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), parent);
        // Calcula el offset (distancia del clic a la esquina superior izquierda de la nave).
        this.offset = new Point(eventPoint.x - getX(), eventPoint.y - getY());
        this.dragging = true;
    }

    /**
     * Dibuja el cuerpo y el borde de la nave.
     *
     * @param g El contexto gráfico.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dibuja el relleno con el color de fondo (transparente)
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Dibuja el borde con un grosor de 2 píxeles
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

    }

    /**
     * Rota la nave 90 grados alrededor del punto donde el cursor del mouse está
     * actualmente sobre ella (definido por {@code this.offset}). Esto mantiene
     * la nave "pegada" al cursor durante la rotación.
     */
    private void rotarNave() {
        // 1. Obtener dimensiones y offset actuales
        int anchoActual = getWidth();
        int altoActual = getHeight();

        // Punto de pivote global (coordenadas del padre)
        int globalPivotX = getX() + offset.x;
        int globalPivotY = getY() + offset.y;

        // Centro de la nave (relativo a la nave)
        double centroX = (double) anchoActual / 2;
        double centroY = (double) altoActual / 2;

        // 2. Vector: Centro de la nave -> Punto de Clic (Offset)
        double vecRelX = offset.x - centroX;
        double vecRelY = offset.y - centroY;

        // 3. Rotar ese vector 90 grados: (x, y) -> (-y, x)
        double vecRotadoX = -vecRelY;
        double vecRotadoY = vecRelX;

        // 4. Calcular nuevas dimensiones y centro
        int nuevoAncho = altoActual;
        int nuevoAlto = anchoActual;
        double nuevoCentroX = (double) nuevoAncho / 2;
        double nuevoCentroY = (double) nuevoAlto / 2;

        // 5. Calcular el *nuevo* offset. El puntero debe estar a una distancia (vecRotado)
        // del nuevo centro (nuevoCentro) en la nave rotada.
        int newOffsetX = (int) Math.round(nuevoCentroX + vecRotadoX);
        int newOffsetY = (int) Math.round(nuevoCentroY + vecRotadoY);

        // 6. Calcular la nueva posición superior izquierda (TOP-LEFT)
        // Nueva Posición = Punto de Pivote Global - Nuevo Offset
        int newX = globalPivotX - newOffsetX;
        int newY = globalPivotY - newOffsetY;

        // 7. Aplicar todos los cambios al estado y a la vista
        this.esHorizontal = !this.esHorizontal;
        this.offset = new Point(newOffsetX, newOffsetY); // Actualiza el offset para el arrastre futuro

        setSize(nuevoAncho, nuevoAlto);
        setLocation(newX, newY);

        // 8. Forzar el redibujo del contenedor
        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }
}
