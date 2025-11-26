/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.UnirsePartidaController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mx.itson.equipo_2.common.enums.AccionPartida;

public class LobbyView extends JPanel {

    private final Color COLOR_FONDO = new Color(83, 111, 137);
    private final Color COLOR_BOTON = new Color(75, 75, 75);
    private final Color COLOR_TEXTO = new Color(255, 255, 255);
    private final Font FUENTE_BOTON = new Font("Segoe UI Black", 0, 18);

    private final UnirsePartidaController unirsePartidaController;

    public LobbyView(VistaController viewController, UnirsePartidaController unirsePartidaController) {
        this.unirsePartidaController = unirsePartidaController;
        initComponents(viewController);
    }

    private void initComponents(VistaController viewController) {
        setBackground(COLOR_FONDO);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));

        try {
            ImageIcon shipIcon = new ImageIcon(getClass().getResource("/images/ship.png"));
            JLabel lblShipImage = new JLabel(shipIcon);
            lblShipImage.setBounds(490, 150, 300, 200);
            add(lblShipImage);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del barco: " + e.getMessage());
        }

        JButton btnCrear = new JButton("Crear Partida");
        btnCrear.setBackground(COLOR_BOTON);
        btnCrear.setFont(FUENTE_BOTON);
        btnCrear.setForeground(COLOR_TEXTO);
        btnCrear.setBorder(null);
        btnCrear.setFocusPainted(false);
        btnCrear.setBounds(540, 400, 200, 41);
        btnCrear.addActionListener(e -> {
            unirsePartidaController.solicitarAcceso(AccionPartida.CREAR);
        });
        add(btnCrear);

        JButton btnUnirse = new JButton("Unirse a Partida");
        btnUnirse.setBackground(COLOR_BOTON);
        btnUnirse.setFont(FUENTE_BOTON);
        btnUnirse.setForeground(COLOR_TEXTO);
        btnUnirse.setBorder(null);
        btnUnirse.setFocusPainted(false);
        btnUnirse.setBounds(540, 460, 200, 41);
        btnUnirse.addActionListener(e -> {
            viewController.cambiarPantalla("unirse");
        });
        add(btnUnirse);

        JButton btnVolver = new JButton("+ Volver");
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setFont(FUENTE_BOTON);
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBorder(null);
        btnVolver.setFocusPainted(false);
        btnVolver.setBounds(540, 520, 200, 41);
        btnVolver.addActionListener(e -> viewController.cambiarPantalla("menu"));
        add(btnVolver);
    }

}
