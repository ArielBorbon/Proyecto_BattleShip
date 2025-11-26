/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory.impl;

import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;
import com.itson.equipo2.battleship_cliente.view.PosicionarNaveVista;
import javax.swing.JPanel;

/**
 *
 * @author skyro
 */
public class PosicionarNaveFactory implements VistaFactory {

    private final PosicionarController posicionarController;

    public PosicionarNaveFactory(PosicionarController posicionarController) {
        this.posicionarController = posicionarController;
    }
    
    
    @Override
    public JPanel crear(VistaController control) {
        return new PosicionarNaveVista(posicionarController);
    }
    
}
