/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.itson.equipo2.battleship_cliente;

import com.google.gson.Gson; 
import com.itson.equipo2.battleship_cliente.controllers.AbandonarController;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.handler.DisparoRealizadoHandler;
import com.itson.equipo2.battleship_cliente.handler.ExceptionHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaIniciadaHandler;
import com.itson.equipo2.battleship_cliente.handler.TurnoTickHandler;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.service.PosicionarNaveService;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import com.itson.equipo2.battleship_cliente.view.PosicionarNaveVista;
import java.util.ArrayList;
import java.util.List; 
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.dto.EventMessage; 
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.CoordenadaDTO; 
import mx.itson.equipo_2.common.dto.NaveDTO; 
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest; 
import mx.itson.equipo_2.common.enums.ColorJugador;
import mx.itson.equipo_2.common.enums.OrientacionNave; 
import mx.itson.equipo_2.common.enums.TipoNave; 
import redis.clients.jedis.JedisPool;

import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.handler.JugadorRegistradoHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaFinalizadaHandler;
import com.itson.equipo2.battleship_cliente.service.AbandonarPartidaService;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import com.itson.equipo2.battleship_cliente.view.LobbyViewFactory;
import com.itson.equipo2.battleship_cliente.view.MenuPrincipalViewFactory;
import com.itson.equipo2.battleship_cliente.view.RegistroViewFactory;


/**
 *
 * @author skyro
 */
public class Battleship_clienteV2 {

    
    private static final String JUGADOR_IA_ID = "IA-123"; 

    public static void main(String[] args) {
        System.out.println("Iniciando Cliente Battleship (Arquitectura Limpia)...");

      
        JedisPool jedisPool = RedisConnection.getJedisPool();
        IMessagePublisher publisher = new RedisPublisher(jedisPool);
        ExecutorService executor = RedisConnection.getSubscriberExecutor();

        PartidaModel partidaModel = new PartidaModel();

        
        TableroModel miTablero = new TableroModel((String) null);
        TableroModel tableroEnemigo = new TableroModel(JUGADOR_IA_ID);
     
        JugadorModel jugadorModel = new JugadorModel();
        jugadorModel.setTablero(miTablero);
        partidaModel.setYo(jugadorModel);

     
        JugadorModel jugadorModelEnemigo = new JugadorModel(JUGADOR_IA_ID, "IA", ColorJugador.ROJO, true, tableroEnemigo, new ArrayList<>());
        partidaModel.setEnemigo(jugadorModelEnemigo);

    
        ViewController viewController = new ViewController();

  
        RealizarDisparoService disparoService = new RealizarDisparoService(publisher, jugadorModel);
        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(publisher, partidaModel);
        AbandonarPartidaService abandonarService = new AbandonarPartidaService(publisher, jugadorModel);

     
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(publisher);
        RegistroController registroController = new RegistroController(registrarJugadorService, partidaModel);
        

        
        DisparoController disparoController = new DisparoController(disparoService);
        PosicionarController posicionarController = new PosicionarController(posicionarNaveService, partidaModel);
        AbandonarController abandonarController = new AbandonarController(abandonarService);

        GameMediator gameMediator = new GameMediator();
        gameMediator.setPartidaController(disparoController);
        gameMediator.setAbandonarController(abandonarController);

      
        DispararView dispararView = new DispararView();
        dispararView.setMediator(gameMediator);

        PosicionarNaveVista posicionarNaveVista = new PosicionarNaveVista(posicionarController);

        dispararView.setModels(partidaModel, miTablero, tableroEnemigo);

        partidaModel.addObserver(dispararView);
        partidaModel.addObserver(posicionarNaveVista);

       
        viewController.registrarPantalla("disparar", dispararView);
        viewController.registrarPantalla("posicionar", posicionarNaveVista);

        viewController.registrarPantalla("menu", new MenuPrincipalViewFactory());
        viewController.registrarPantalla("registro", new RegistroViewFactory(registroController));
        viewController.registrarPantalla("lobby", new LobbyViewFactory());
        
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("EXCEPTION", new ExceptionHandler(viewController));
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler(partidaModel));
        eventDispatcher.subscribe("PartidaFinalizada", new PartidaFinalizadaHandler(viewController, partidaModel));


        eventDispatcher.subscribe(
                "JugadorRegistrado",
                new JugadorRegistradoHandler(viewController, partidaModel)
        );
     
        IMessageSubscriber redisSubscriber = new RedisSubscriber(jedisPool, executor, eventDispatcher);
        redisSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);

   
        SwingUtilities.invokeLater(() -> {
       
            viewController.cambiarPantalla("menu"); 
            
        });
    }

  
    private static void iniciarPartidaVsIA(IMessagePublisher publisher, TableroModel tableroModel) {
        System.out.println("Enviando comando para crear partida vs IA...");

        List<NaveDTO> navesHumano = crearNavesDePrueba();
        List<NaveDTO> navesIA = crearNavesDePrueba(); // La IA usa la misma disposición

        // 1. Poblar nuestro modelo local de tablero ANTES de enviar.
        // tableroModel.posicionarNaves(navesHumano);
        
        // JUGADOR_HUMANO_ID ya no existe, esto fallará
        // CrearPartidaVsIARequest request = new CrearPartidaVsIARequest(
        //         JUGADOR_HUMANO_ID,
        //         ColorJugador.ROJO,
        //         navesHumano,
        //         navesIA
        // );

        // EventMessage message = new EventMessage("CrearPartidaVsIA", new Gson().toJson(request));
        // publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
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