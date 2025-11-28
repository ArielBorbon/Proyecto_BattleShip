package com.itson.equipo2.communication.service;

import com.itson.equipo2.communication.broker.ICommunicationProvider;
import com.itson.equipo2.communication.broker.IMessageSubscriber;

public class NetworkService {

    private IMessageSubscriber currentSubscriber;
    private final ICommunicationProvider communicationProvider;
    private final String eventChannel;

    private String currentIp = null;

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
        if (currentIp != null && currentIp.equals(ip)) {
            System.out.println("NetworkService: Ya estamos conectados a " + ip + ". Omitiendo reconexión.");
            return;
        }

        System.out.println("NetworkService: Solicitando conexión a " + ip + "...");

        communicationProvider.connect(ip);

        this.currentIp = ip;

        System.out.println("Conexión establecida exitosamente.");
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
        currentSubscriber = communicationProvider.getSubscriber();

        if (currentSubscriber != null) {
            currentSubscriber.subscribe(eventChannel);
            System.out.println("Suscriptor activo en el canal: " + eventChannel);
        }
    }
}
