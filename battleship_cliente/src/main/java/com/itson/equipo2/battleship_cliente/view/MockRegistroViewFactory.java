/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import javax.swing.JPanel;


public class MockRegistroViewFactory implements ViewFactory {
    
    private final RegistroController registroController;
    
    
    public MockRegistroViewFactory(RegistroController controller) {
        this.registroController = controller;
    }
    
    @Override 
    public JPanel crear(ViewController vc) {
      
        return new MockRegistroView(registroController); 
    }
}