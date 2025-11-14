/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_cliente;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.handler.DisparoRealizadoHandler;
import com.itson.equipo2.battleship_cliente.handler.ExceptionHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaIniciadaHandler;
import com.itson.equipo2.battleship_cliente.handler.TurnoTickHandler;
import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import java.util.ArrayList;
import java.util.List;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;
import com.itson.equipo2.communication.dto.EventMessage;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import java.awt.Color;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.enums.ColorJugador;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author skyro
 */
public class Battleship_cliente {

    private static final String JUGADOR_HUMANO_ID = "JUGADOR_1";
    private static final String JUGADOR_IA_ID = "IA-123";

    public static void main(String[] args) {
        System.out.println("Iniciando Cliente Battleship (Arquitectura Limpia)...");

        // ---------------------------------------------------------
        // 1. INFRAESTRUCTURA (Lo que no depende de nadie)
        // ---------------------------------------------------------
        // Iniciamos la conexión (Singleton de infraestructura es aceptable si es puramente técnico)
        JedisPool jedisPool = RedisConnection.getJedisPool();
        IMessagePublisher publisher = new RedisPublisher(jedisPool);

        // ---------------------------------------------------------
        // 2. MODELOS (Estado de la aplicación)
        // ---------------------------------------------------------
        // Creamos el modelo raíz. Al hacerlo aquí, tú controlas su ciclo de vida.
        PartidaModel partidaModel = new PartidaModel();
        //TableroModel tableroModel = new TableroModel();
        TableroModel miTablero = new TableroModel(JUGADOR_HUMANO_ID); 
        TableroModel tableroEnemigo = new TableroModel(JUGADOR_IA_ID);
        
        JugadorModel jugadorModel = new JugadorModel(JUGADOR_HUMANO_ID, "Jonh Doe", ColorJugador.AZUL, true, miTablero, new ArrayList<>());
        JugadorModel jugadorModelEnemigo = new JugadorModel(JUGADOR_IA_ID, "IA", ColorJugador.ROJO, true, tableroEnemigo, new ArrayList<>());
        
        partidaModel.setYo(jugadorModel);
        partidaModel.setEnemigo(jugadorModelEnemigo);

        // (Opcional) Si necesitas referencias directas a los tableros para inyectarlas
        // TableroModel miTablero = partidaModel.getJugadorLocal().getTableroModel();
        // ---------------------------------------------------------
        // 3. CONTROLADORES (Lógica que une Vista y Modelo)
        // ---------------------------------------------------------
        // Inyectamos las dependencias en el constructor
        ViewController viewController = new ViewController();

        RealizarDisparoService disparoService = new RealizarDisparoService(publisher, jugadorModel);
        
        // Ejemplo: El controlador de disparo necesita el Modelo para validar y el Publisher para enviar
        DisparoController disparoController = new DisparoController(disparoService);

        GameMediator gameMediator = new GameMediator();
        gameMediator.setPartidaController(disparoController);
        // ---------------------------------------------------------
        // 4. VISTAS (UI)
        // ---------------------------------------------------------
        // La vista principal recibe el controlador que gestionará la navegación
        DispararView dispararView = new DispararView();
        dispararView.setMediator(gameMediator);
        
        iniciarPartidaVsIA(publisher, miTablero);
        
        dispararView.setModels(partidaModel, miTablero, tableroEnemigo);
        
        partidaModel.addObserver(dispararView);
//        miTablero.addObserver(dispararView);
//        tableroEnemigo.addObserver(dispararView);
        // Inicializamos las vistas específicas e inyectamos sus dependencias
        // NOTA: Aquí ya no usamos AppContext dentro de las vistas.
        // Si DispararView necesita el controlador, se lo pasamos.
        viewController.registrarPantalla("disparar", dispararView);
        
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("EXCEPTION", new ExceptionHandler(viewController));
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler(partidaModel));
        
        ExecutorService executor = RedisConnection.getSubscriberExecutor();
        IMessageSubscriber redisSubscriber = new RedisSubscriber(jedisPool, executor, eventDispatcher);
        redisSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);

        
        // ---------------------------------------------------------
        // 5. ARRANQUE
        // ---------------------------------------------------------
    }

    private static void iniciarPartidaVsIA(IMessagePublisher publisher, TableroModel tableroModel) {
        System.out.println("Enviando comando para crear partida vs IA...");

        List<NaveDTO> navesHumano = crearNavesDePrueba();
        List<NaveDTO> navesIA = crearNavesDePrueba(); // La IA usa la misma disposición

        // 1. Poblar nuestro modelo local de tablero ANTES de enviar.
        tableroModel.posicionarNaves(navesHumano);

        CrearPartidaVsIARequest request = new CrearPartidaVsIARequest(
                JUGADOR_HUMANO_ID,
                ColorJugador.ROJO,
                navesHumano,
                navesIA
        );

        EventMessage message = new EventMessage("CrearPartidaVsIA", new Gson().toJson(request));
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
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
