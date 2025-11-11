/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pruebas;

import com.itson.equipo2.battleship_cliente.communication.RedisConnection;
import com.itson.equipo2.battleship_cliente.communication.RedisPublisher;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import com.itson.equipo2.battleship_cliente.view.MainFrameView;
import com.itson.equipo2.battleship_cliente.view.PosicionarNaveVista;
import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.PartidaDTO;
import mx.itson.equipo_2.common.dto.TableroDTO;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import redis.clients.jedis.JedisPool;

/**
 * Main de prueba para testear el envío del evento "RealizarDisparo" USANDO UNA
 * CONEXIÓN REAL DE REDIS.
 *
 * REQUISITOS PREVIOS: 1. Un servidor Redis debe estar corriendo en
 * localhost:6379. 2. El Servidor.jar (DDD) debe estar corriendo y suscrito a
 * los canales.
 */
public class TestMainDisparo_ConRedis {

    public static void main(String[] args) {
        System.out.println("Iniciando Main de Prueba (Integración con Redis)...");

        // --- 3. Mockear el Estado de la Partida ---
        // (Este paso sigue siendo vital para saltar a la pantalla correcta)
        System.out.println("Mockeando el estado de la partida...");

        String JUGADOR_LOCAL_ID = "JUGADOR-REAL-1"; // (Usa un ID que tu servidor reconozca si es necesario)
        String JUGADOR_OPONENTE_ID = "JUGADOR-REAL-2";

        JugadorModel jm1 = new JugadorModel(JUGADOR_LOCAL_ID, "Juan", true, new TableroModel(new CeldaModel[10][10]),
                new ArrayList<>());
        JugadorModel jm2 = new JugadorModel(JUGADOR_OPONENTE_ID, "John", true, new TableroModel(new CeldaModel[10][10]),
                new ArrayList<>());

        PartidaModel partidaModel = new PartidaModel(UUID.randomUUID().toString(), jm1, jm2, true, JUGADOR_LOCAL_ID, 30, EstadoPartida.EN_BATALLA);

        JedisPool jedisPool = null;
        // --- 1. Iniciar Conexión REAL a Redis ---
        // Al llamar a getInstance(), se inician el Publisher y Subscriber reales.
        try {
            jedisPool = RedisConnection.getJedisPool();
            System.out.println("Conectado a Redis.");
        } catch (Exception e) {
            System.err.println("¡ERROR! No se pudo conectar a Redis en localhost:6379.");
            System.err.println("Asegúrate de que Redis esté corriendo.");
            return;
        }

        RedisPublisher publisher = new RedisPublisher(jedisPool);
        RealizarDisparoService ts = new RealizarDisparoService(publisher, new JugadorModel("1", "Pepe", true, new TableroModel(new CeldaModel[10][10]), new ArrayList<>()));
        DisparoController dc = new DisparoController(ts);
        GameMediator gm = new GameMediator();
        gm.setPartidaController(dc);
        // --- 2. Iniciar Componentes MVC ---
        DispararView dispararView = new DispararView();
        dispararView.setMediator(gm);
        PosicionarNaveVista view = new PosicionarNaveVista();

        MainFrameView mainFrame = new MainFrameView();
        ViewController viewController = new ViewController();
        viewController.registrarPantalla("disparo", dispararView);
        viewController.registrarPantalla("posicionar", view);
        
        
//        partidaModelactualizarDesdeDTO(partidaDTO);
        // --- 4. Mostrar la Vista de Disparo ---
        viewController.cambiarPantalla("posicionar");
//        viewController.cambiarPantalla("disparo");
    }
}
