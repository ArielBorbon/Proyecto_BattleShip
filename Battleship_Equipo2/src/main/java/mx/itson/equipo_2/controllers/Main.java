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

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.EstadoPartida;

public class Main {

    public static void main(String[] args) {
        // SwingUtilities.invokeLater garantiza que si se cree la interfaz grafica
        SwingUtilities.invokeLater(() -> {

            // creamos los jugadores
            Jugador jugador1 = new Jugador("Ariel");
            Jugador jugador2 = new Jugador("IA");

            //metemos unas naves de prueba
            // (fase de colocacion)
            colocarNavesDePrueba(jugador1);
            colocarNavesDePrueba(jugador2);

            PartidaModel partidaModel = new PartidaModel(jugador1, jugador2);
            // comenzamos partida
            partidaModel.getPartida().setEstado(EstadoPartida.EN_BATALLA); // Necesitarás un setter en Partida

            // creamos vista y controlador
            DispararView dispararView = new DispararView();

            // conectamos modelo y vista con controlador
            // ya con todo conectado todo lo movemos con el controlador
            PartidaController partidaController = new PartidaController(partidaModel, dispararView, jugador1, jugador2);
            
            partidaController.iniciarPartida(); //iniciar partida despues de crear el controlador

            // sacamos el gestionador de pantallas
            ViewController viewController = new ViewController();

            // factorizamos la pantalla de disparo.
            viewController.registrarPantalla("disparar", (vc) -> dispararView);
            viewController.cambiarPantalla("disparar");

            System.out.println("¡Partida iniciada! Turno de: " + partidaModel.getJugadorEnTurno().getNombre());

            if (partidaModel.partidaFinalizada()) {
                JOptionPane.showMessageDialog(null, "¡Partida terminada! Ganador: " + partidaModel.getJugadorEnTurno().getNombre());
                System.out.println("GANADOR: " + partidaModel.getJugadorEnTurno().getNombre());
            }

        });
    }

    /**
     * Metodo para poner unas naves de prueba en el main que tenemos orita
     */
    private static void colocarNavesDePrueba(Jugador jugador) {
        Tablero tablero = jugador.getTablero();

        //metemos barcos de diferentes tamaños
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
    }
}
