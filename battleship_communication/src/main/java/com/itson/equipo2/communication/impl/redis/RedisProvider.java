package com.itson.equipo2.communication.impl.redis;

import com.itson.equipo2.communication.broker.*;
import com.itson.equipo2.communication.impl.EventDispatcher;
import java.util.concurrent.ExecutorService;
import redis.clients.jedis.Jedis;

public class RedisProvider implements ICommunicationProvider {

    private final EventDispatcher eventDispatcher;
    private final ExecutorService executorService;

    // Necesitamos el Dispatcher y el Executor para construir el Suscriptor
    public RedisProvider(EventDispatcher eventDispatcher, ExecutorService executorService) {
        this.eventDispatcher = eventDispatcher;
        this.executorService = executorService;
    }

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

    @Override
    public IMessagePublisher getPublisher() {
        return new RedisPublisher(RedisConnection.getJedisPool());
    }

    @Override
    public IMessageSubscriber getSubscriber() {
        return new RedisSubscriber(
                RedisConnection.getJedisPool(),
                executorService,
                eventDispatcher
        );
    }

    @Override
    public void close() {
        RedisConnection.shutdown();
    }
}
