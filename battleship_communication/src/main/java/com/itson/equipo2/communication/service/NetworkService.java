package com.itson.equipo2.communication.service;

import com.itson.equipo2.communication.broker.ICommunicationProvider;
import com.itson.equipo2.communication.broker.IMessageSubscriber;

/**
 * Servicio de alto nivel encargado de gestionar la conectividad de red y el
 * ciclo de vida de la suscripción a eventos.
 *
 * Abstrae la lógica de conexión (IP), desconexión y reinicio del suscriptor,
 * delegando los detalles técnicos al {@code ICommunicationProvider}.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class NetworkService {

    private IMessageSubscriber currentSubscriber;
    private final ICommunicationProvider communicationProvider;
    private final String eventChannel;

    private String currentIp = null;

    /**
     * Constructor del servicio de red.
     *
     * @param communicationProvider El proveedor de comunicación (ej.
     * RedisProvider).
     * @param eventChannel El nombre del canal principal de eventos a escuchar.
     */
    public NetworkService(ICommunicationProvider communicationProvider, String eventChannel) {
        this.communicationProvider = communicationProvider;
        this.eventChannel = eventChannel;
    }

    /**
     * Inicializa la escucha en la configuración actual del proveedor. Llama
     * internamente a iniciar la suscripción.
     */
    public void inicializar() {
        iniciarSuscripcion();
    }

    /**
     * Cambia la conexión a una nueva IP y reinicia los servicios de
     * suscripción. Gestiona la validación de la IP y evita reconexiones
     * redundantes.
     *
     * @param ip La nueva dirección IP del servidor.
     * @throws Exception Si la IP es inválida o falla la conexión con el
     * proveedor.
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

        // Delega la conexión al proveedor (ej. RedisProvider validará con PING)
        communicationProvider.connect(ip);

        this.currentIp = ip;

        System.out.println("Conexión establecida exitosamente.");
        reiniciarSuscripcion();
    }

    /**
     * Método interno para reiniciar la suscripción. Detiene el suscriptor
     * actual (si existe) y crea uno nuevo.
     */
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

    /**
     * Método interno para obtener un nuevo suscriptor del proveedor e iniciar
     * la escucha en el canal configurado.
     */
    private void iniciarSuscripcion() {
        currentSubscriber = communicationProvider.getSubscriber();

        if (currentSubscriber != null) {
            currentSubscriber.subscribe(eventChannel);
            System.out.println("Suscriptor activo en el canal: " + eventChannel);
        }
    }
}
