/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.factory;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.view.UnirseAPartidaView;
import javax.swing.JPanel;

/**
 *
 * @author PC Gamer
 */
public class UnirseAPartidaViewFactory implements ViewFactory {

    private final RegistroController registroController;

    public UnirseAPartidaViewFactory(RegistroController registroController) {
        this.registroController = registroController;
    }

    @Override
    public JPanel crear(ViewController control) {
        // ViewController para volver atrás y el RegistroController para logica de unirse
        return new UnirseAPartidaView(control, registroController);
    }
}
