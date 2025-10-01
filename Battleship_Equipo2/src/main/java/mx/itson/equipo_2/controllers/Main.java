/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
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

            // 1️⃣ PRIMERO: Crear los jugadores.
            Jugador jugador1 = new Jugador("Humano");
            Jugador jugador2 = new Jugador("IA");

            // 2️⃣ SEGUNDO: Configurar COMPLETAMENTE a los jugadores con sus barcos.
            colocarNavesDePrueba(jugador1);
            colocarNavesDePrueba(jugador2);

            // 3️⃣ TERCERO: Crear el Modelo de la Partida, AHORA que los jugadores están listos.
            PartidaModel partidaModel = new PartidaModel(jugador1, jugador2);
            GameMediator mediator = new GameMediator();

            // 4. Crear Controlador (solo ve al Modelo y al Mediador)
            PartidaController partidaController = new PartidaController(partidaModel, mediator);
            // 5. Crear Vista
            DispararView dispararView = new DispararView();

            // 6. Conectar todas las piezas
            dispararView.setMediator(mediator);
            dispararView.setJugador(jugador1);
            // La vista recibe los modelos de tablero que ya tienen los barcos dentro
            dispararView.setTableros(partidaModel.getTableroModel1(), partidaModel.getTableroModel2());

            // Suscribir la Vista a los cambios del Modelo
            partidaModel.addObserver(dispararView);
            partidaModel.getTableroModel1().addObserver(dispararView);
            partidaModel.getTableroModel2().addObserver(dispararView);

            // 7. Crear y registrar Estrategias en el Controlador
            partidaController.registrarEstrategia(jugador1, new StrategyTurnoHumano());
            partidaController.registrarEstrategia(jugador2, new StrategyTurnoIA(jugador2));

            // 8. Mostrar la UI
            ViewController viewController = new ViewController();
            viewController.registrarPantalla("disparar", (vc) -> dispararView);
            viewController.cambiarPantalla("disparar");

            // 9. Iniciar el juego
            partidaController.iniciarPartida();
        });
    }

    private static void colocarNavesDePrueba(Jugador jugador) {
        Tablero tablero = jugador.getTablero();

        // Barco 1
        List<Coordenada> coordsBarco1 = new ArrayList<>();
        coordsBarco1.add(new Coordenada(0, 0));
        coordsBarco1.add(new Coordenada(0, 1));
        tablero.agregarNave(new Nave(TipoNave.BARCO, coordsBarco1, OrientacionNave.HORIZONTAL));

        // Barco 2
        List<Coordenada> coordsBarco2 = new ArrayList<>();
        coordsBarco2.add(new Coordenada(2, 3));
        coordsBarco2.add(new Coordenada(3, 3));
        coordsBarco2.add(new Coordenada(4, 3));
        tablero.agregarNave(new Nave(TipoNave.SUBMARINO, coordsBarco2, OrientacionNave.VERTICAL));

        // Barco 3
        List<Coordenada> coordsBarco3 = new ArrayList<>();
        coordsBarco3.add(new Coordenada(5, 5));
        coordsBarco3.add(new Coordenada(5, 6));
        coordsBarco3.add(new Coordenada(5, 7));
        coordsBarco3.add(new Coordenada(5, 8));
        tablero.agregarNave(new Nave(TipoNave.CRUCERO, coordsBarco3, OrientacionNave.HORIZONTAL));

        // Añadimos un 'println' para depurar y confirmar que los barcos se añaden
        System.out.println("Barcos añadidos al jugador " + jugador.getNombre() + ". Total: " + tablero.getNaves().size());
    }
}
