/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MockLobbyViewFactory implements ViewFactory {
    
    public MockLobbyViewFactory() { 
     
    }
    
    @Override 
    public JPanel crear(ViewController vc) {
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Lobby (Mock)"));
        panel.add(new JButton("Crear Partida (Implementar)"));
        panel.add(new JButton("Unirse a Partida (Implementar)"));
        return panel;
    }
}
