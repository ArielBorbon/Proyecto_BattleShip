/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory.impl;

import com.itson.equipo2.battleship_cliente.controllers.AbandonarController;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import javax.swing.JPanel;

/**
 *
 * @author skyro
 */
public class DispararFactory implements VistaFactory {

    private final DisparoController disparoController;
    private final AbandonarController abandonarController;

    public DispararFactory(DisparoController disparoController, AbandonarController abandonarController) {
        this.disparoController = disparoController;
        this.abandonarController = abandonarController;
    }

    @Override
    public JPanel crear(VistaController control) {
        return new DispararView(disparoController, abandonarController);
    }

}
