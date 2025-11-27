
package com.itson.equipo2.battleship_cliente.pattern.factory.impl;

import com.itson.equipo2.battleship_cliente.controllers.ConfiguracionController;
import com.itson.equipo2.battleship_cliente.controllers.UnirsePartidaController;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.view.LobbyView;
import javax.swing.JPanel;
import com.itson.equipo2.battleship_cliente.pattern.factory.VistaFactory;

public class LobbyViewFactory implements VistaFactory {
    
    private final UnirsePartidaController unirseController;
    private final ConfiguracionController configController;

    public LobbyViewFactory(UnirsePartidaController unirseController, ConfiguracionController configController) {
        this.unirseController = unirseController;
        this.configController = configController;
    }

    @Override
    public JPanel crear(VistaController control) {
        return new LobbyView(control, unirseController, configController);
    }
}
