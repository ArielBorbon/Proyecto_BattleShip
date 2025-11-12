/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.itson.equipo2.battleship_cliente;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.communication.RedisConfig;
import com.itson.equipo2.battleship_cliente.communication.RedisConnection;
import com.itson.equipo2.battleship_cliente.communication.RedisPublisher;
import com.itson.equipo2.battleship_cliente.communication.RedisSubscriber;
import com.itson.equipo2.battleship_cliente.communication.handler.DisparoRealizadoHandler;
import com.itson.equipo2.battleship_cliente.communication.handler.ExceptionHandler;
import com.itson.equipo2.battleship_cliente.communication.handler.PartidaIniciadaHandler;
import com.itson.equipo2.battleship_cliente.communication.handler.TurnoTickHandler;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
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
        RedisSubscriber subscriber = new RedisSubscriber(pool, executor);

        // 2. Configurar Vistas y ViewController
        ViewController viewController = new ViewController();

        // 3. Inicializar AppContext
        // Inyectamos el publisher y el viewController
        AppContext.initialize(publisher, viewController, JUGADOR_HUMANO_ID, "Humano");

        // 4. Configurar Handlers de Eventos
        List<IMessageHandler> handlers = new ArrayList<>();
        handlers.add(new ExceptionHandler(AppContext.getViewController()));
        
        // Los handlers ahora obtienen sus dependencias de AppContext
        handlers.add(new PartidaIniciadaHandler(
                AppContext.getViewController(), 
                AppContext.getPartidaModel(), 
                AppContext.getTableroPropio(), 
                AppContext.getTableroEnemigo()
        ));
        
        // CORRECCIÓN 3: DisparoRealizadoHandler usa un constructor vacío
        handlers.add(new DisparoRealizadoHandler());
        handlers.add(new TurnoTickHandler()); // <-- AÑADIR NUEVO HANDLER


        // ... (El resto del rootHandler sigue igual)
        IMessageHandler rootHandler = new IMessageHandler() {
            @Override
            public boolean canHandle(EventMessage message) {
                return handlers.stream().anyMatch(h -> h.canHandle(message));
            }
            @Override
            public void onMessage(EventMessage message) {
                for (IMessageHandler handler : handlers) {
                    if (handler.canHandle(message)) {
                        try {
                            handler.onMessage(message);
                        } catch (Exception e) {
                            System.err.println("Error en handler " + handler.getClass().getSimpleName() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        };


        // 6. Suscribirse
        subscriber.subscribe(RedisConfig.CHANNEL_EVENTOS, rootHandler);
        System.out.println("Suscrito a " + RedisConfig.CHANNEL_EVENTOS);

        // 7. Iniciar la partida (enviar comando)
        iniciarPartidaVsIA(publisher);
    }
    
    // ... (el resto de la clase, 'iniciarPartidaVsIA' y 'crearNavesDePrueba', sigue igual)
    // ...
    private static void iniciarPartidaVsIA(IMessagePublisher publisher) {
        System.out.println("Enviando comando para crear partida vs IA...");
        
        List<NaveDTO> navesHumano = crearNavesDePrueba();
        List<NaveDTO> navesIA = crearNavesDePrueba(); // La IA usa la misma disposición
        
        // --- INICIO DE LA CORRECCIÓN ---
        // 1. Poblar nuestro modelo local de tablero ANTES de enviar.
        TableroModel tableroPropio = AppContext.getTableroPropio();
        tableroPropio.posicionarNaves(navesHumano);
        // --- FIN DE LA CORRECCIÓN ---
        
        
        
        
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