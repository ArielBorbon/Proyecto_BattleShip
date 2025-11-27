package com.itson.equipo2.communication.broker;

/**
 * Define el contrato para cualquier proveedor de comunicaciones. Permite
 * cambiar entre Redis, Sockets, etc., sin romper el NetworkService.
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
     */
    IMessagePublisher getPublisher();

    /**
     * Obtiene una instancia lista para usar del suscriptor.
     */
    IMessageSubscriber getSubscriber();

    /**
     * Cierra los recursos globales del proveedor.
     */
    void close();
}
