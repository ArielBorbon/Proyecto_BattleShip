package com.itson.equipo2.battleship_servidor.application.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.PosicionarNaveService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;

/**
 * Handler (Manejador) de mensajes del Broker para el comando "PosicionarFlota".
 * <p>
 * Recibe el mensaje desde el cliente, deserializa la solicitud y notifica al
 * {@code PosicionarNaveService} para que aplique los cambios en el dominio.
 * </p>
 */
public class PosicionarNaveHandler implements IMessageHandler {

    /**
     * Servicio de lógica de negocio para el posicionamiento de naves.
     */
    private final PosicionarNaveService service;

    /**
     * Instancia de Gson para la deserialización de la carga útil del mensaje.
     */
    private final Gson gson = new Gson();

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el manejador con el servicio de posicionamiento.
     *
     * @param service El servicio de aplicación que contiene la lógica para
     * posicionar naves.
     */
    public PosicionarNaveHandler(PosicionarNaveService service) {
        this.service = service;
    }

    // --- MÉTODOS DE LA INTERFAZ IMessageHandler ---
    /**
     * Determina si este manejador puede procesar el mensaje.
     *
     * @param message El mensaje del Broker.
     * @return {@code true} si el tipo de evento es "PosicionarFlota",
     * {@code false} en caso contrario.
     */
    @Override
    public boolean canHandle(EventMessage message) {
        return "PosicionarFlota".equals(message.getEventType());
    }

    /**
     * Procesa el mensaje: deserializa la solicitud y delega la acción al
     * servicio.
     *
     * @param message El mensaje del Broker.
     */
    @Override
    public void onMessage(EventMessage message) {
        // 1. Deserializa el JSON del payload a un objeto PosicionarFlotaRequest.
        PosicionarFlotaRequest req = gson.fromJson(message.getPayload(), PosicionarFlotaRequest.class);

        // 2. Delega la lógica de dominio al servicio.
        service.posicionarNaves(req);
    }

}
