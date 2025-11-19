package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 * Handler (Manejador) de mensajes del Broker para el evento
 * "NavesPosicionadas".
 * <p>
 * Este evento se recibe cuando el servidor confirma que todos los jugadores han
 * posicionado sus flotas, indicando el fin de la fase de preparación.
 * </p>
 */
public class NavesPosicionadasHandler implements IMessageHandler {

    /**
     * Referencia al controlador principal para cambiar de pantalla.
     */
    private final ViewController viewController;

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el manejador.
     *
     * @param viewController El controlador principal de vistas.
     */
    public NavesPosicionadasHandler(ViewController viewController) {
        this.viewController = viewController;
    }

    // --- MÉTODOS DE LA INTERFAZ IMessageHandler ---
    /**
     * Determina si este manejador puede procesar el mensaje recibido.
     *
     * @param message El mensaje del Broker.
     * @return {@code true} si el tipo de evento es "NavesPosicionadas",
     * {@code false} en caso contrario.
     */
    @Override
    public boolean canHandle(EventMessage message) {
        return "NavesPosicionadas".equals(message.getEventType());
    }

    /**
     * Ejecuta la acción cuando se recibe el mensaje "NavesPosicionadas".
     * <p>
     * La acción es cambiar la vista a la pantalla de espera de posicionamiento
     * (el juego aún no comienza, solo se sale de la fase de preparación).
     * </p>
     *
     * @param message El mensaje del Broker.
     */
    @Override
    public void onMessage(EventMessage message) {
        // Mueve la UI a la siguiente pantalla de la partida.
        viewController.cambiarPantalla("esperandoPosicionamiento");
    }
}
