/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import mx.itson.equipo_2.common.enums.ColorJugador;


public class MockRegistroView extends JPanel {
    
    public MockRegistroView(RegistroController controller) {
        setLayout(new BorderLayout(10, 10));
        
        JTextField nombreField = new JTextField("Escribe tu nombre");
        JComboBox<ColorJugador> colorBox = new JComboBox<>(ColorJugador.values());
        JButton registrarBtn = new JButton("Registrar");
        
        registrarBtn.addActionListener(e -> {
            String nombre = nombreField.getText();
            ColorJugador color = (ColorJugador) colorBox.getSelectedItem();
            
            
            controller.registrar(nombre, color);
        });
        
     
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorBox);
        
        add(new JLabel("Registrar Jugador (Mock)"), BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(registrarBtn, BorderLayout.SOUTH);
    }
}