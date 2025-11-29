package com.itson.equipo2.communication.impl.redis;

import com.itson.equipo2.communication.broker.*;
import com.itson.equipo2.communication.impl.EventDispatcher;
import java.util.concurrent.ExecutorService;
import redis.clients.jedis.Jedis;

/**
 * Proveedor de comunicación específico para Redis. Gestiona la conexión,
 * publicación y suscripción utilizando la librería Jedis.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class RedisProvider implements ICommunicationProvider {

    private final EventDispatcher eventDispatcher;
    private final ExecutorService executorService;

    /**
     * Constructor del proveedor de Redis.
     *
     * * @param eventDispatcher Despachador para manejar eventos entrantes.
     * @param executorService Servicio de ejecución para hilos de suscripción.
     */
    public RedisProvider(EventDispatcher eventDispatcher, ExecutorService executorService) {
        this.eventDispatcher = eventDispatcher;
        this.executorService = executorService;
    }

    /**
     * Establece la conexión con el servidor Redis y valida la conectividad.
     *
     * * @param host Dirección del host de Redis.
     * @throws Exception Si la conexión falla o el servidor no responde PONG.
     */
    @Override
    public void connect(String host) throws Exception {
        // Usamos tu clase utilitaria existente
        RedisConnection.setHost(host);

        // Validación específica de Redis (PING)
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            if (!"PONG".equals(jedis.ping())) {
                throw new Exception("No se recibió PONG del servidor Redis en " + host);
            }
        }
    }

    /**
     * Obtiene una instancia del publicador de Redis.
     *
     * * @return Instancia de RedisPublisher.
     */
    @Override
    public IMessagePublisher getPublisher() {
        return new RedisPublisher(RedisConnection.getJedisPool());
    }

    /**
     * Obtiene una instancia del suscriptor de Redis.
     *
     * * @return Instancia de RedisSubscriber.
     */
    @Override
    public IMessageSubscriber getSubscriber() {
        return new RedisSubscriber(
                RedisConnection.getJedisPool(),
                executorService,
                eventDispatcher
        );
    }

    /**
     * Cierra las conexiones y libera recursos estáticos de Redis.
     */
    @Override
    public void close() {
        RedisConnection.shutdown();
    }
}
