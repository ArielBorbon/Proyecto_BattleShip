
package com.itson.equipo2.battleship_cliente.pattern.factory;


import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.view.LobbyView;
import javax.swing.JPanel;

public class LobbyViewFactory implements ViewFactory {
    
    private final RegistroController registroController;

    public LobbyViewFactory(RegistroController registroController) {
        this.registroController = registroController;
    }
    
    @Override
    public JPanel crear(ViewController control) {
        return new LobbyView(control, registroController);
    }
}
