/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory.impl;

import com.itson.equipo2.battleship_cliente.controllers.SalaController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.view.SalaPartidaView;
import javax.swing.JPanel;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;

/**
 *
 * @author PC Gamer
 */
public class SalaPartidaViewFactory implements VistaFactory {

    private final SalaController salaController;

    public SalaPartidaViewFactory(SalaController salaController) {
        this.salaController = salaController;
    }

    @Override
    public JPanel crear(VistaController control) {
        return new SalaPartidaView(salaController);
    }
}
