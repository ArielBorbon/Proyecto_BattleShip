/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;
import com.itson.equipo2.battleship_cliente.view.util.NaveView;
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
import javax.swing.LayoutStyle;
import javax.swing.border.LineBorder;
import mx.itson.equipo_2.common.enums.TipoNave;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;

/**
 * Vista principal utilizada para la fase de posicionamiento de naves del
 * jugador. Implementa {@code VistaFactory} y {@code PartidaObserver} para crear
 * la vista y sincronizar el estado visual del tablero con el
 * {@code PartidaModel}.
 */
public class PosicionarNaveVista extends javax.swing.JPanel implements VistaFactory, PartidaObserver {

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
        crearCeldas();
    }

    // --- MÉTODOS DE LA INTERFAZ ---
    /**
     * Implementación del patrón Factory. Devuelve esta instancia de JPanel.
     *
     * @param control Referencia al {@code VistaController} (no utilizada
     * directamente en esta clase).
     * @return Esta instancia de {@code JPanel}.
     */
    @Override
    public JPanel crear(VistaController control) {
        return this;
    }

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
            System.out.println("tablero nulo");
            return;
        }

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

            nave1.setEnabled(true);
            nave2.setEnabled(true);
            nave3.setEnabled(true);
            nave4.setEnabled(true);
            btnConfirmar.setEnabled(false); 
        }   

        Color colorJugador = Color.GRAY; // Default
        if (model.getYo() != null && model.getYo().getColor() != null) {
            colorJugador = model.getYo().getColor().getColor();
        }

        if (nave1 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave1).setColorJugador(colorJugador);
        }
        if (nave2 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave2).setColorJugador(colorJugador);
        }
        if (nave3 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave3).setColorJugador(colorJugador);
        }
        if (nave4 instanceof SelectorNaveView) {
            ((SelectorNaveView) nave4).setColorJugador(colorJugador);
        }

        // Recorre las celdas y sincroniza el color con el estado del modelo.
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                CeldaModel celdaModelo = tableroPropio.getCelda(f, c);
                // La celda en la UI se obtiene por índice (f * 10 + c)
                JButton celdaUI = (JButton) tablero.getComponent(f * 10 + c);

                if (celdaModelo.tieneNave()) {
                    // Colorea la celda con el color del jugador si tiene una nave.
                    celdaUI.setBackground(model.getYo().getColor().getColor());
                } else {
                    // Mantiene el color base (agua) si no tiene nave.
                    celdaUI.setBackground(new Color(50, 70, 100));
                }
            }
        }

        actualizarEstadoBoton();
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
                celdaPropio.setEnabled(false); // Las celdas son solo visuales, no interactivas
                celdaPropio.setBackground(new Color(50, 70, 100)); // Color de agua inicial
                celdaPropio.setBorder(new LineBorder(Color.BLACK, 1));
                tablero.add(celdaPropio);
            }
        }
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

        setBackground(new Color(83, 111, 137));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));

        tablero.setBackground(new Color(82, 113, 177));
        tablero.setMaximumSize(new Dimension(570, 570));
        tablero.setMinimumSize(new Dimension(570, 570));
        tablero.setPreferredSize(new Dimension(570, 570));
        tablero.setLayout(new GridLayout(10, 10));

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

        nave1.setMaximumSize(new Dimension(57, 57));
        nave1.setMinimumSize(new Dimension(57, 57));
        nave1.setName(""); // NOI18N
        nave1.setPreferredSize(new Dimension(57, 57));

        GroupLayout nave1Layout = new GroupLayout(nave1);
        nave1.setLayout(nave1Layout);
        nave1Layout.setHorizontalGroup(nave1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );
        nave1Layout.setVerticalGroup(nave1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

        nave2.setMaximumSize(new Dimension(114, 57));
        nave2.setMinimumSize(new Dimension(114, 57));
        nave2.setName(""); // NOI18N
        nave2.setPreferredSize(new Dimension(114, 57));

        GroupLayout nave2Layout = new GroupLayout(nave2);
        nave2.setLayout(nave2Layout);
        nave2Layout.setHorizontalGroup(nave2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 114, Short.MAX_VALUE)
        );
        nave2Layout.setVerticalGroup(nave2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

        nave3.setMaximumSize(new Dimension(171, 57));
        nave3.setMinimumSize(new Dimension(171, 57));
        nave3.setName(""); // NOI18N
        nave3.setPreferredSize(new Dimension(171, 57));
        nave3.setRequestFocusEnabled(false);

        GroupLayout nave3Layout = new GroupLayout(nave3);
        nave3.setLayout(nave3Layout);
        nave3Layout.setHorizontalGroup(nave3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );
        nave3Layout.setVerticalGroup(nave3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

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
            .addGap(0, 57, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1080, 1080, 1080)
                .addComponent(btnConfirmar, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 44, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(tablero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(nave4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(183, 183, 183))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(nave3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(nave2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(237, 237, 237))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(nave1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(266, 266, 266))))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(tablero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(nave1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(nave2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nave3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(nave4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)))
                .addComponent(btnConfirmar, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
        );

        nave1.getAccessibleContext().setAccessibleName("");
        nave1.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        int resultado = JOptionPane.showConfirmDialog(this, "¿Deseas confirmar el tablero?");

        if (resultado != JOptionPane.YES_OPTION) {
            return;
        }

        posicionarController.confirmarPosicionamiento();

        // Deshabilita todo para que no pueda mover nada más
        btnConfirmar.setEnabled(false);
        nave1.setEnabled(false);
        nave2.setEnabled(false);
        nave3.setEnabled(false);
        nave4.setEnabled(false);
    }//GEN-LAST:event_btnConfirmarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnConfirmar;
    private JPanel nave1;
    private JPanel nave2;
    private JPanel nave3;
    private JPanel nave4;
    private JPanel tablero;
    // End of variables declaration//GEN-END:variables
}
