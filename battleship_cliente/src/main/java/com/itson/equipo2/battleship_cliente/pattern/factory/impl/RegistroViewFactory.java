/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory.impl;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.view.RegistroView;
import javax.swing.JPanel;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;

/**
 *
 * @author Cricri
 */
public class RegistroViewFactory implements VistaFactory {

    private final RegistroController registroController;

  
    public RegistroViewFactory(RegistroController controller) {
        this.registroController = controller;
    }
    
    @Override
    public JPanel crear(VistaController control) {
  
        return new RegistroView(control, registroController);
    }
}
