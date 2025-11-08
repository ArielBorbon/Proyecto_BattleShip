package com.itson.equipo2.battleship_servidor.infrastructure.redis;


/**
 *
 * @author skyro
 */
public class RedisConfig {

    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    public static final String CHANNEL_COMANDOS = "battleship:comandos";
    public static final String CHANNEL_EVENTOS = "battleship:eventos";
}
