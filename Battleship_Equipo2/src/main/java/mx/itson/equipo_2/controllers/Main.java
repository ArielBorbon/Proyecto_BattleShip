
package mx.itson.equipo_2.controllers;

import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Nave;
import mx.itson.equipo_2.models.enums.OrientacionNave;
import mx.itson.equipo_2.models.enums.TipoNave;
import mx.itson.equipo_2.patterns.mediator.ViewController;
import mx.itson.equipo_2.views.DispararView;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.patterns.Strategy.StrategyTurnoHumano;
import mx.itson.equipo_2.patterns.Strategy.StrategyTurnoIA;
import mx.itson.equipo_2.patterns.mediator.GameMediator;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Jugador jugador1 = new Jugador("Humano");
            Jugador jugador2 = new Jugador("IA");

            colocarNavesDePrueba(jugador1);
            colocarNavesDePrueba(jugador2);

            PartidaModel partidaModel = new PartidaModel(jugador1, jugador2);
            GameMediator mediator = new GameMediator();

            PartidaController partidaController = new PartidaController(partidaModel, mediator);
            DispararView dispararView = new DispararView();

            dispararView.setMediator(mediator);
            dispararView.setJugador(jugador1);
            dispararView.setTableros(partidaModel.getTableroModel1(), partidaModel.getTableroModel2());

            partidaModel.addObserver(dispararView);
            partidaModel.getTableroModel1().addObserver(dispararView);
            partidaModel.getTableroModel2().addObserver(dispararView);

            partidaController.registrarEstrategia(jugador1, new StrategyTurnoHumano());
            partidaController.registrarEstrategia(jugador2, new StrategyTurnoIA(jugador2));

            ViewController viewController = new ViewController();
            viewController.registrarPantalla("disparar", (vc) -> dispararView);
            viewController.cambiarPantalla("disparar");

            partidaController.iniciarPartida();
        });
    }

    private static void colocarNavesDePrueba(Jugador jugador) {
        Tablero tablero = jugador.getTablero();

        List<Coordenada> coordsBarco1 = new ArrayList<>();
        coordsBarco1.add(new Coordenada(0, 0));
        coordsBarco1.add(new Coordenada(0, 1));
        tablero.agregarNave(new Nave(TipoNave.BARCO, coordsBarco1, OrientacionNave.HORIZONTAL));

        List<Coordenada> coordsBarco2 = new ArrayList<>();
        coordsBarco2.add(new Coordenada(2, 3));
        coordsBarco2.add(new Coordenada(3, 3));
        coordsBarco2.add(new Coordenada(4, 3));
        tablero.agregarNave(new Nave(TipoNave.SUBMARINO, coordsBarco2, OrientacionNave.VERTICAL));

        List<Coordenada> coordsBarco3 = new ArrayList<>();
        coordsBarco3.add(new Coordenada(5, 5));
        coordsBarco3.add(new Coordenada(5, 6));
        coordsBarco3.add(new Coordenada(5, 7));
        coordsBarco3.add(new Coordenada(5, 8));
        tablero.agregarNave(new Nave(TipoNave.CRUCERO, coordsBarco3, OrientacionNave.HORIZONTAL));

        System.out.println("Barcos añadidos al jugador " + jugador.getNombre() + ". Total: " + tablero.getNaves().size());
    }
}
