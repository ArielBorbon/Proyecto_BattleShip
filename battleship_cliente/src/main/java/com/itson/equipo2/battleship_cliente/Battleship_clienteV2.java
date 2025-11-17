/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.itson.equipo2.battleship_cliente;
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
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.enums.ColorJugador;
import redis.clients.jedis.JedisPool;
import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.pattern.factory.LobbyViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.MenuPrincipalViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.RegistroViewFactory;


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

   
        RegistroController registroController = new RegistroController(partidaModel); 
   

        DisparoController disparoController = new DisparoController(disparoService);
        PosicionarController posicionarController = new PosicionarController(posicionarNaveService, partidaModel);

        GameMediator gameMediator = new GameMediator();
        gameMediator.setPartidaController(disparoController);


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
      
        IMessageSubscriber redisSubscriber = new RedisSubscriber(jedisPool, executor, eventDispatcher);
        redisSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);

     
        SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("menu"); 
        });
    }

}