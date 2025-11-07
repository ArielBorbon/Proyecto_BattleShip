/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

/**
 *
 * @author Cricri
 */
public class RedisConfig {
   
    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    public static final String CHANNEL_COMANDOS = "battleship:comandos";
    public static final String CHANNEL_EVENTOS = "battleship:eventos";
 }
