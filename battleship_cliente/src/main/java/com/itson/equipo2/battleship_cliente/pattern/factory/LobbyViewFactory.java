/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory;


import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.battleship_cliente.view.LobbyView;
import javax.swing.JPanel;

public class LobbyViewFactory implements ViewFactory {

    public LobbyViewFactory() {
   
    }
    
    @Override
    public JPanel crear(ViewController control) {
        return new LobbyView(control);
    }
}
