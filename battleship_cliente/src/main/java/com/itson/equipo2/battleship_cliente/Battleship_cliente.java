/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.itson.equipo2.battleship_cliente;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.communication.RedisConfig;
import com.itson.equipo2.battleship_cliente.communication.RedisConnection;
import com.itson.equipo2.battleship_cliente.communication.RedisPublisher;
import com.itson.equipo2.battleship_cliente.communication.RedisSubscriber;
import com.itson.equipo2.battleship_cliente.handler.DisparoRealizadoHandler;
import com.itson.equipo2.battleship_cliente.handler.ExceptionHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaIniciadaHandler;
import com.itson.equipo2.battleship_cliente.handler.TurnoTickHandler;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.communication.EventDispatcher;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.utils.AppContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;
import mx.itson.equipo_2.common.message.EventMessage;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author skyro
 */
public class Battleship_cliente {

    private static final String JUGADOR_HUMANO_ID = "JUGADOR_1";

    public static void main(String[] args) {
        System.out.println("Iniciando Battleship Cliente...");

        // 1. Conexión y Broker
        JedisPool pool = RedisConnection.getJedisPool();
        ExecutorService executor = RedisConnection.getSubscriberExecutor();
        RedisPublisher publisher = new RedisPublisher(pool);
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        RedisSubscriber subscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        // 2. Configurar Vistas y ViewController
        ViewController viewController = new ViewController();

        // 3. Inicializar AppContext
        // Inyectamos el publisher y el viewController
        AppContext.initialize(publisher, viewController, JUGADOR_HUMANO_ID, "Humano");
        
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler());
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler());
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, AppContext.getPartidaModel(), AppContext.getTableroPropio(), AppContext.getTableroEnemigo()));

        // 6. Suscribirse
        subscriber.subscribe(RedisConfig.CHANNEL_EVENTOS);
        System.out.println("Suscrito a " + RedisConfig.CHANNEL_EVENTOS);

        // 7. Iniciar la partida (enviar comando)
        iniciarPartidaVsIA(publisher);
    }
    
    private static void iniciarPartidaVsIA(IMessagePublisher publisher) {
        System.out.println("Enviando comando para crear partida vs IA...");
        
        List<NaveDTO> navesHumano = crearNavesDePrueba();
        List<NaveDTO> navesIA = crearNavesDePrueba(); // La IA usa la misma disposición
        
        // 1. Poblar nuestro modelo local de tablero ANTES de enviar.
        TableroModel tableroPropio = AppContext.getTableroPropio();
        tableroPropio.posicionarNaves(navesHumano);
        
        
        
        
        CrearPartidaVsIARequest request = new CrearPartidaVsIARequest(
                JUGADOR_HUMANO_ID,
                navesHumano,
                navesIA
        );
        
        EventMessage message = new EventMessage("CrearPartidaVsIA", new Gson().toJson(request));
        publisher.publish(RedisConfig.CHANNEL_COMANDOS, message);
    }
    
    private static List<NaveDTO> crearNavesDePrueba() {
        List<NaveDTO> naves = new ArrayList<>();

        List<CoordenadaDTO> coordsBarco1 = new ArrayList<>();
        coordsBarco1.add(new CoordenadaDTO(0, 0));
        coordsBarco1.add(new CoordenadaDTO(0, 1));
        naves.add(new NaveDTO(TipoNave.BARCO, null, coordsBarco1, OrientacionNave.HORIZONTAL));

        List<CoordenadaDTO> coordsBarco2 = new ArrayList<>();
        coordsBarco2.add(new CoordenadaDTO(2, 3));
        coordsBarco2.add(new CoordenadaDTO(3, 3));
        coordsBarco2.add(new CoordenadaDTO(4, 3));
        naves.add(new NaveDTO(TipoNave.SUBMARINO, null, coordsBarco2, OrientacionNave.VERTICAL));

        List<CoordenadaDTO> coordsBarco3 = new ArrayList<>();
        coordsBarco3.add(new CoordenadaDTO(5, 5));
        coordsBarco3.add(new CoordenadaDTO(5, 6));
        coordsBarco3.add(new CoordenadaDTO(5, 7));
        coordsBarco3.add(new CoordenadaDTO(5, 8));
        naves.add(new NaveDTO(TipoNave.CRUCERO, null, coordsBarco3, OrientacionNave.HORIZONTAL));
        
        return naves;
    }
}