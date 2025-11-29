package com.itson.equipo2.communication.broker;

/**
 * Define el contrato para cualquier proveedor de comunicaciones. Permite
 * cambiar entre Redis, Sockets, etc., sin romper el NetworkService.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public interface ICommunicationProvider {

    /**
     * Configura la conexión al host especificado.
     *
     * @param host Dirección IP o Hostname.
     * @throws Exception Si falla la conexión o validación.
     */
    void connect(String host) throws Exception;

    /**
     * Obtiene una instancia lista para usar del publicador.
     *
     * @return El publicador de mensajes.
     */
    IMessagePublisher getPublisher();

    /**
     * Obtiene una instancia lista para usar del suscriptor.
     *
     * @return El suscriptor de mensajes.
     */
    IMessageSubscriber getSubscriber();

    /**
     * Cierra los recursos globales del proveedor.
     */
    void close();
}
