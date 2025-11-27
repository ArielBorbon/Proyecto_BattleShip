package com.itson.equipo2.communication.service;

import com.itson.equipo2.communication.broker.ICommunicationProvider;
import com.itson.equipo2.communication.broker.IMessageSubscriber;

public class NetworkService {

    private IMessageSubscriber currentSubscriber;
    private final ICommunicationProvider communicationProvider;
    private final String eventChannel;

    // Inyección de Dependencia: Le pasamos CUALQUIER proveedor
    public NetworkService(ICommunicationProvider communicationProvider, String eventChannel) {
        this.communicationProvider = communicationProvider;
        this.eventChannel = eventChannel;
    }

    /**
     * Inicializa la escucha en la configuración actual del proveedor.
     */
    public void inicializar() {
        iniciarSuscripcion();
    }

    /**
     * Cambia la conexión a una nueva IP y reinicia los servicios.
     */
    public void conectarAServidor(String ip) throws Exception {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP vacía");
        }

        System.out.println("NetworkService: Solicitando conexión a " + ip + "...");

        // 1. Polimorfismo: El proveedor sabe cómo conectarse (sea Redis o Sockets)
        communicationProvider.connect(ip);

        System.out.println("Conexión establecida exitosamente.");

        // 2. Reiniciamos la suscripción en el nuevo canal/host
        reiniciarSuscripcion();
    }

    private void reiniciarSuscripcion() {
        if (currentSubscriber != null) {
            System.out.println("Deteniendo suscriptor anterior...");
            try {
                currentSubscriber.unsubscribe();
            } catch (Exception e) {
                System.err.println("Error al desuscribir: " + e.getMessage());
            }
            currentSubscriber = null;
        }
        iniciarSuscripcion();
    }

    private void iniciarSuscripcion() {
        // La fábrica nos da un suscriptor nuevo configurado
        currentSubscriber = communicationProvider.getSubscriber();

        if (currentSubscriber != null) {
            currentSubscriber.subscribe(eventChannel);
            System.out.println("Suscriptor activo en el canal: " + eventChannel);
        }
    }
}
        