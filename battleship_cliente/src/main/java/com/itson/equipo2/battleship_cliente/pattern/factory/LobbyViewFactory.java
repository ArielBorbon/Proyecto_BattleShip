
package com.itson.equipo2.battleship_cliente.pattern.factory;

import com.itson.equipo2.battleship_cliente.controllers.UnirsePartidaController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.view.LobbyView;
import javax.swing.JPanel;

public class LobbyViewFactory implements ViewFactory {
    
    private final UnirsePartidaController unirseController;

    public LobbyViewFactory(UnirsePartidaController unirseController) {
        this.unirseController = unirseController;
    }

    @Override
    public JPanel crear(ViewController control) {
        return new LobbyView(control, unirseController);
    }
}
