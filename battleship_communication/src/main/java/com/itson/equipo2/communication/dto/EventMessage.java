package com.itson.equipo2.communication.dto;

/**
 * DTO que representa un mensaje de evento en el sistema de comunicación. Es la
 * estructura base para la serialización/deserialización JSON.
 */
public class EventMessage {

    /**
     * El tipo de evento que identifica la acción (ej. "NavesPosicionadas").
     */
    private String eventType;

    /**
     * La carga útil del mensaje, generalmente un objeto DTO serializado en
     * JSON.
     */
    private String payload;

    /**
     * Constructor vacío requerido para la deserialización de Gson.
     */
    public EventMessage() {
    }

    /**
     * Constructor para crear un mensaje de evento completo.
     *
     * @param eventType El tipo de evento.
     * @param payload La carga útil del mensaje.
     */
    public EventMessage(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    // --- Getters y Setters ---
    /**
     * Obtiene el tipo de evento.
     *
     * @return El tipo de evento.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Establece el tipo de evento.
     *
     * @param eventType El tipo de evento.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Obtiene la carga útil (payload).
     *
     * @return La carga útil.
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Establece la carga útil (payload).
     *
     * @param payload La carga útil.
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

}
