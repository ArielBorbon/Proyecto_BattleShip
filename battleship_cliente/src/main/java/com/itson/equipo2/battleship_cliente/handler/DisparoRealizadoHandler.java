/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class DisparoRealizadoHandler implements IMessageHandler {

    private final Gson gson = new Gson();

    // 1. Declaramos la dependencia como final
    private final PartidaModel partidaModel;

    // 2. Inyectamos la dependencia por el constructor
    public DisparoRealizadoHandler(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "DisparoRealizado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Handler 'DisparoRealizado' activado.");

        // 3. Deserializamos el mensaje
        ResultadoDisparoReponse response = gson.fromJson(message.getPayload(), ResultadoDisparoReponse.class);

        // 4. Accedemos a los tableros a través de la jerarquía del PartidaModel (MVC Correcto)
        //    (Ya no usamos AppContext)
        TableroModel tableroPropio = partidaModel.getYo().getTablero();
        TableroModel tableroEnemigo = partidaModel.getEnemigo().getTablero();

        TableroModel tableroAfectado;

        // 5. Lógica para determinar a quién actualizar
        // Si el ID del que disparó soy YO, entonces actualizo el tablero ENEMIGO (mis disparos)
        if (response.getJugadorId().equals(partidaModel.getYo().getId())) {
            System.out.println("Actualizando tablero ENEMIGO (Mi disparo).");
            tableroAfectado = tableroEnemigo;
        } else {
            // Si disparó otro, actualizo MI tablero (sus disparos hacia mí)
            System.out.println("Actualizando tablero PROPIO (Disparo enemigo).");
            tableroAfectado = tableroPropio;
        }

        // 6. Actualizar el modelo
        if (tableroAfectado != null) {
            // Asegúrate de que tu TableroModel tenga este método implementado
            tableroAfectado.actualizarCelda(
                    response.getCoordenada(),
                    response.getResultado(),
                    response.getCoordenadasBarcoHundido()
            );
        } else {
            System.err.println("Error: Tablero afectado es nulo.");
        }

        // 7. Actualizar estado de la partida
        // Swing UI debe actualizarse en el hilo de eventos, si este handler corre en background,
        // el notifyObservers final disparará el repaint.
        partidaModel.setTurnoDe(response.getTurnoActual());
        partidaModel.setEstado(response.getEstadoPartida());
    }
}
