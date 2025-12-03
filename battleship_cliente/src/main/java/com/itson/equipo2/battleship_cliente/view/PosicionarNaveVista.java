/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.NaveModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.view.util.SelectorNaveView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import mx.itson.equipo_2.common.enums.TipoNave;
import com.itson.equipo2.battleship_cliente.pattern.observer.IObserver;
import com.itson.equipo2.battleship_cliente.view.util.NaveView;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Vista principal utilizada para la fase de posicionamiento de naves del
 * jugador. Implementa {@code VistaFactory} y {@code IObserver} para crear la
 * vista y sincronizar el estado visual del tablero con el {@code PartidaModel}.
 */
public class PosicionarNaveVista extends javax.swing.JPanel implements IObserver<PartidaModel> {

    // Lista para rastrear las naves visuales agregadas al panel
    private List<Component> navesPintadas = new ArrayList<>();

    // --- Variables de Referencia y Modelo ---
    /**
     * Referencia a la lista (histórica) de NaveView. No usada actualmente, pero
     * mantenida si se requiere.
     */
    private List navesEnTablero = new java.util.ArrayList<>();

    /**
     * El controlador de la lógica de posicionamiento (parte del patrón MVC).
     */
    private PosicionarController posicionarController;

    /**
     * Crea e inicializa la vista de posicionamiento de naves.
     *
     * @param posicionarController El controlador de lógica de posicionamiento
     * asociado.
     */
    public PosicionarNaveVista(PosicionarController posicionarController) {
        this.posicionarController = posicionarController;
        initComponents();
        crearCeldas(); // Mantenemos las celdas (botones) debajo como referencia y click
        setLayout(null); // Aseguramos Layout Absoluto para poder posicionar encima
    }

    // --- MÉTODOS DE LA INTERFAZ ---
    /**
     * Implementación del patrón Observer. Se llama cuando el
     * {@code PartidaModel} cambia, permitiendo la sincronización de la vista
     * con el modelo.
     * <p>
     * Se utiliza para colorear las celdas del tablero propio con el color de la
     * nave después de un posicionamiento exitoso.
     * </p>
     *
     * @param model El {@code PartidaModel} actualizado.
     */
    @Override
    public void onChange(PartidaModel model) {
        TableroModel tableroPropio = model.getTableroPropio();
        if (tableroPropio == null) {
            return;
        }

        // 1. Lógica de habilitar/deshabilitar selectores (Tu código original intacto)
        if (tableroPropio.getNavesPosicionadas().isEmpty()) {
            if (nave1 instanceof SelectorNaveView) {
                ((SelectorNaveView) nave1).reiniciarSelector();
            }
            if (nave2 instanceof SelectorNaveView) {
                ((SelectorNaveView) nave2).reiniciarSelector();
            }
            if (nave3 instanceof SelectorNaveView) {
                ((SelectorNaveView) nave3).reiniciarSelector();
            }
            if (nave4 instanceof SelectorNaveView) {
                ((SelectorNaveView) nave4).reiniciarSelector();
            }
            btnConfirmar.setEnabled(false);
        }

        Color colorJugador = Color.GRAY;
        if (model.getYo() != null && model.getYo().getColor() != null) {
            colorJugador = model.getYo().getColor().getColor();
        }
        actualizarSelectoresColor(colorJugador);

        // --- 2. NUEVA LÓGICA DE PINTADO ---
        // A) Limpiar naves visuales anteriores
        for (Component c : navesPintadas) {
            this.remove(c);
        }
        navesPintadas.clear();

        // B) Resetear colores de celdas a "Agua" (limpieza visual del fondo)
        for (Component c : tablero.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(new Color(50, 70, 100)); // Agua
            }
        }

        // C) Crear y colocar las imágenes de las naves
        for (NaveModel naveModel : tableroPropio.getNavesPosicionadas()) {

            // Crear la vista de la nave (reutilizamos NaveView pero estática)
            NaveView naveVisual = new NaveView(
                    naveModel.getTipo(),
                    tablero,
                    posicionarController,
                    colorJugador
            );

            // Configurar estado estático
            naveVisual.setOrientacion(naveModel.isEsHorizontal());
            naveVisual.setDraggable(false); // ¡Importante! No queremos que se arrastre sola

            // Calcular posición absoluta
            // El tablero está en (40, 40) y cada celda mide 57
            int size = 57;
            int xTablero = tablero.getX();
            int yTablero = tablero.getY();

            int xFinal = xTablero + (naveModel.getColumna() * size);
            int yFinal = yTablero + (naveModel.getFila() * size);

            naveVisual.setLocation(xFinal, yFinal);

            // Listener para levantar la nave al hacer clic en la imagen
            naveVisual.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!btnConfirmar.isEnabled() || !verificarSiTodoEstaPuesto()) {
                        // Lógica de "Levantar" nave
                        posicionarController.retirarNave(naveModel);
                        // Nota: Al retirar, el modelo notifica, entra a onChange y se repinta todo sin esta nave
                    }
                }
            });

            // Agregar al panel principal (encima del tablero)
            this.add(naveVisual);
            this.setComponentZOrder(naveVisual, 0); // Poner al frente de todo
            navesPintadas.add(naveVisual);
        }

        this.revalidate();
        this.repaint();
        actualizarEstadoBoton();
    }

    private void actualizarSelectoresColor(Color c) {
        if (nave1 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave1).setColorJugador(c);
        }
        if (nave2 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave2).setColorJugador(c);
        }
        if (nave3 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave3).setColorJugador(c);
        }
        if (nave4 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave4).setColorJugador(c);
        }
    }

    // --- MÉTODOS AUXILIARES ---
    /**
     * Inicializa y añade los 100 {@code JButton} que representan las celdas del
     * tablero del jugador.
     */
    private void crearCeldas() {
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                JButton celdaPropio = new JButton();

                // Habilitamos la celda para recibir clicks
                celdaPropio.setEnabled(true);
                celdaPropio.setFocusPainted(false);
                celdaPropio.setBorderPainted(true);

                // Guardamos coordenadas para saber dónde se hizo clic
                celdaPropio.putClientProperty("fila", fila);
                celdaPropio.putClientProperty("col", col);

                celdaPropio.setBackground(new Color(50, 70, 100));
                celdaPropio.setBorder(new LineBorder(Color.BLACK, 1));

                // Listener para detectar clic en un barco ya puesto
                celdaPropio.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                            int f = (int) celdaPropio.getClientProperty("fila");
                            int c = (int) celdaPropio.getClientProperty("col");
                            // Intentamos levantar la nave
                            intentarLevantarNave(f, c, e);
                        }
                    }
                });

                tablero.add(celdaPropio);
            }
        }
    }

    private void intentarLevantarNave(int fila, int col, java.awt.event.MouseEvent e) {
        // 1. Preguntar al controlador si hay una nave en esa celda
        com.itson.equipo2.battleship_cliente.models.NaveModel naveEncontrada = posicionarController.obtenerNaveEn(fila, col);

        // Si no hay nave, no hacemos nada
        if (naveEncontrada == null) {
            return;
        }

        // Si el botón de confirmar ya se presionó, no permitir mover nada
        if (!btnConfirmar.isEnabled() && verificarSiTodoEstaPuesto()) {
            // Opcional: Si ya confirmaste, quizás no quieras permitir mover. 
            // Si quieres permitir mover hasta antes de enviar al server, elimina este if.
        }

        // 2. Extraer datos antes de borrarla
        TipoNave tipo = naveEncontrada.getTipo();
        boolean estabaHorizontal = naveEncontrada.isEsHorizontal();

        // Obtenemos el color actual del jugador del modelo
        Color colorJugador = Color.GRAY;
        // (Asumiendo que tienes acceso al modelo o lo sacas del selector)
        SelectorNaveView selectorOrigen = getSelectorPorTipo(tipo);
        if (selectorOrigen != null) {
            colorJugador = selectorOrigen.getColorJugador();
        }

        // 3. ELIMINAR del modelo lógico 
        // (Esto disparará el onChange, pintando el hueco de agua azul)
        posicionarController.retirarNave(naveEncontrada);

        // 4. CREAR la nave visual (NaveView) para arrastrar
        javax.swing.JLayeredPane layered = javax.swing.SwingUtilities.getRootPane(this).getLayeredPane();

        NaveView naveView = new NaveView(tipo, tablero, posicionarController, colorJugador);

        // Configurar la rotación que tenía
        naveView.setOrientacion(estabaHorizontal);

        // Ubicar la nave visualmente donde está el ratón
        java.awt.Point puntoEnLayered = javax.swing.SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), layered);

        // Centramos la nave en el cursor para mejor experiencia
        naveView.setLocation(
                puntoEnLayered.x - (naveView.getWidth() / 2),
                puntoEnLayered.y - (naveView.getHeight() / 2)
        );

        // 5. Manejar el caso de que la suelte fuera (Cancelar movimiento)
        if (selectorOrigen != null) {
            naveView.addPropertyChangeListener("naveDevuelta", (evt) -> {
                // Si la suelta fuera, vuelve al inventario del selector
                selectorOrigen.devolverBarco();
                actualizarEstadoBoton();
            });
        }

        // 6. Añadir al panel y forzar el inicio del arrastre
        layered.add(naveView, javax.swing.JLayeredPane.DRAG_LAYER);
        layered.revalidate();
        layered.repaint();

        naveView.startDragFromEvent(e);

        // Actualizamos el botón "Confirmar" (se desactiva porque acabamos de quitar un barco)
        actualizarEstadoBoton();
    }

// Método auxiliar necesario para el chequeo del if anterior
    private boolean verificarSiTodoEstaPuesto() {
        return ((SelectorNaveView) nave1).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave2).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave3).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave4).getBarcosRestantes() == 0;
    }

    /**
     * Verifica si todos los selectores de naves indican que no quedan barcos
     * restantes. Si es así, habilita el botón "Confirmar".
     */
    public void actualizarEstadoBoton() {
        // Se realiza un casting para acceder al método getBarcosRestantes() del SelectorNaveView
        boolean todosPuestos
                = ((SelectorNaveView) nave1).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave2).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave3).getBarcosRestantes() == 0
                && ((SelectorNaveView) nave4).getBarcosRestantes() == 0;

        btnConfirmar.setEnabled(todosPuestos);
    }

    private SelectorNaveView getSelectorPorTipo(TipoNave tipo) {
        if (nave1 instanceof SelectorNaveView && ((SelectorNaveView) nave1).getTipo() == tipo) {
            return (SelectorNaveView) nave1;
        }
        if (nave2 instanceof SelectorNaveView && ((SelectorNaveView) nave2).getTipo() == tipo) {
            return (SelectorNaveView) nave2;
        }
        if (nave3 instanceof SelectorNaveView && ((SelectorNaveView) nave3).getTipo() == tipo) {
            return (SelectorNaveView) nave3;
        }
        if (nave4 instanceof SelectorNaveView && ((SelectorNaveView) nave4).getTipo() == tipo) {
            return (SelectorNaveView) nave4;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablero = new JPanel();
        btnConfirmar = new JButton();
        nave1 = new SelectorNaveView(TipoNave.BARCO, this.tablero, this.posicionarController);
        nave2 = new SelectorNaveView(TipoNave.SUBMARINO, this.tablero, this.posicionarController);
        nave3 = new SelectorNaveView(TipoNave.CRUCERO, this.tablero, this.posicionarController);
        nave4 = new SelectorNaveView(TipoNave.PORTA_AVIONES, this.tablero, this.posicionarController);
        jLabel1 = new JLabel();

        setBackground(new Color(83, 111, 137));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);

        tablero.setBackground(new Color(82, 113, 177));
        tablero.setMaximumSize(new Dimension(570, 570));
        tablero.setMinimumSize(new Dimension(570, 570));
        tablero.setPreferredSize(new Dimension(570, 570));
        tablero.setLayout(new GridLayout(10, 10));
        add(tablero);
        tablero.setBounds(40, 40, 570, 570);

        btnConfirmar.setBackground(new Color(75, 75, 75));
        btnConfirmar.setFont(new Font("Segoe UI Black", 0, 18)); // NOI18N
        btnConfirmar.setForeground(new Color(255, 255, 255));
        btnConfirmar.setText("Confirmar");
        btnConfirmar.setBorder(null);
        btnConfirmar.setEnabled(false);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        add(btnConfirmar);
        btnConfirmar.setBounds(1080, 640, 156, 41);

        nave1.setMaximumSize(new Dimension(57, 57));
        nave1.setMinimumSize(new Dimension(57, 57));
        nave1.setName(""); // NOI18N
        nave1.setPreferredSize(new Dimension(57, 57));

        GroupLayout nave1Layout = new GroupLayout(nave1);
        nave1.setLayout(nave1Layout);
        nave1Layout.setHorizontalGroup(nave1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        nave1Layout.setVerticalGroup(nave1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(nave1);
        nave1.setBounds(957, 119, 57, 57);
        nave1.getAccessibleContext().setAccessibleName("");
        nave1.getAccessibleContext().setAccessibleDescription("");

        nave2.setMaximumSize(new Dimension(114, 57));
        nave2.setMinimumSize(new Dimension(114, 57));
        nave2.setName(""); // NOI18N
        nave2.setPreferredSize(new Dimension(114, 57));

        GroupLayout nave2Layout = new GroupLayout(nave2);
        nave2.setLayout(nave2Layout);
        nave2Layout.setHorizontalGroup(nave2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        nave2Layout.setVerticalGroup(nave2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(nave2);
        nave2.setBounds(929, 234, 114, 57);

        nave3.setMaximumSize(new Dimension(171, 57));
        nave3.setMinimumSize(new Dimension(171, 57));
        nave3.setName(""); // NOI18N
        nave3.setPreferredSize(new Dimension(171, 57));
        nave3.setRequestFocusEnabled(false);

        GroupLayout nave3Layout = new GroupLayout(nave3);
        nave3.setLayout(nave3Layout);
        nave3Layout.setHorizontalGroup(nave3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        nave3Layout.setVerticalGroup(nave3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(nave3);
        nave3.setBounds(898, 362, 171, 57);

        nave4.setMaximumSize(new Dimension(228, 57));
        nave4.setMinimumSize(new Dimension(228, 57));
        nave4.setName(""); // NOI18N
        nave4.setPreferredSize(new Dimension(228, 57));
        nave4.setRequestFocusEnabled(false);

        GroupLayout nave4Layout = new GroupLayout(nave4);
        nave4.setLayout(nave4Layout);
        nave4Layout.setHorizontalGroup(nave4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );
        nave4Layout.setVerticalGroup(nave4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(nave4);
        nave4.setBounds(871, 492, 228, 57);

        jLabel1.setFont(new Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new Color(255, 255, 255));
        jLabel1.setIcon(new ImageIcon(getClass().getResource("/images/r_key.png"))); // NOI18N
        jLabel1.setText("para rotar mientras sostienes una nave");
        add(jLabel1);
        jLabel1.setBounds(120, 640, 410, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        int resultado = JOptionPane.showConfirmDialog(this, "¿Deseas confirmar el tablero?");

        if (resultado != JOptionPane.YES_OPTION) {
            return;
        }

        posicionarController.confirmarPosicionamiento();

        // Deshabilita todo para que no pueda mover nada más
        btnConfirmar.setEnabled(false);
        ((SelectorNaveView) nave1).reiniciarSelector();
        ((SelectorNaveView) nave2).reiniciarSelector();
        ((SelectorNaveView) nave3).reiniciarSelector();
        ((SelectorNaveView) nave4).reiniciarSelector();
    }//GEN-LAST:event_btnConfirmarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnConfirmar;
    private JLabel jLabel1;
    private JPanel nave1;
    private JPanel nave2;
    private JPanel nave3;
    private JPanel nave4;
    private JPanel tablero;
    // End of variables declaration//GEN-END:variables
}
