/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.itson.equipo2.battleship_cliente;

import com.itson.equipo2.battleship_cliente.controllers.AbandonarController;
import com.itson.equipo2.battleship_cliente.controllers.ConfiguracionController;

import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.controllers.PosicionarController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.handler.DisparoRealizadoHandler;
import com.itson.equipo2.battleship_cliente.handler.ExceptionHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaIniciadaHandler;
import com.itson.equipo2.battleship_cliente.handler.TurnoTickHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaFinalizadaHandler;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.service.PosicionarNaveService;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import com.itson.equipo2.battleship_cliente.view.PosicionarNaveVista;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingUtilities;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import redis.clients.jedis.JedisPool;
import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.SalaController;
import com.itson.equipo2.battleship_cliente.handler.JugadorUnidoHandler;
import com.itson.equipo2.battleship_cliente.handler.NavesPosicionadasHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaActualizadaHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaCanceladaHandler;
import com.itson.equipo2.battleship_cliente.handler.PosicionamientoHandler;
import com.itson.equipo2.battleship_cliente.pattern.factory.DerrotaViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.LobbyViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.MenuPrincipalViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.RegistroViewFactory;
import com.itson.equipo2.battleship_cliente.service.AbandonarPartidaService;
import com.itson.equipo2.battleship_cliente.pattern.factory.UnirseAPartidaViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.VictoriaViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.communication.impl.NetworkService;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import com.itson.equipo2.battleship_cliente.service.SalaService;
import com.itson.equipo2.battleship_cliente.view.EsperandoPosicionamientoVista;
import com.itson.equipo2.battleship_cliente.view.SalaPartidaView;
import javax.swing.JPanel;

public class Battleship_clienteV2 {


    public static void main(String[] args) {
        System.out.println("Iniciando Cliente Battleship (Arquitectura Limpia)...");

        // 1. Infraestructura
        JedisPool jedisPool = RedisConnection.getJedisPool();
        IMessagePublisher publisher = new RedisPublisher(jedisPool);
        ExecutorService executor = RedisConnection.getSubscriberExecutor();

        // 2. Modelos
        PartidaModel partidaModel = new PartidaModel();
        TableroModel miTablero = new TableroModel((String) null);

        JugadorModel jugadorModel = new JugadorModel();
        jugadorModel.setTablero(miTablero);
        partidaModel.setYo(jugadorModel);


        // 3. Servicios
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(publisher);
        NetworkService networkService = new NetworkService(BrokerConfig.CHANNEL_EVENTOS);
        SalaService salaService = new SalaService(publisher);
        RealizarDisparoService disparoService = new RealizarDisparoService(publisher, jugadorModel);
        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(publisher, partidaModel);
        AbandonarPartidaService abandonarService = new AbandonarPartidaService(publisher, jugadorModel);

        // 4. Controlador Principal de Vistas
        ViewController viewController = new ViewController();

        // 5. Controladores de Negocio
        RegistroController registroController = new RegistroController(partidaModel, registrarJugadorService);
        ConfiguracionController configController = new ConfiguracionController(networkService);
        DisparoController disparoController = new DisparoController(disparoService);
        PosicionarController posicionarController = new PosicionarController(posicionarNaveService, partidaModel);
        AbandonarController abandonarController = new AbandonarController(abandonarService);

        SalaController salaController = new SalaController(salaService, viewController, abandonarService , partidaModel);

        posicionarController.setViewController(viewController);

        // 6. Mediadores
        GameMediator gameMediator = new GameMediator();
        gameMediator.setPartidaController(disparoController);
        gameMediator.setAbandonarController(abandonarController);

        // 7. Vistas
        DispararView dispararView = new DispararView();
        dispararView.setMediator(gameMediator);

        PosicionarNaveVista posicionarNaveVista = new PosicionarNaveVista(posicionarController);

        // Inyectamos el controlador en lugar del publisher
        SalaPartidaView salaPartidaView = new SalaPartidaView(salaController);

        // Configuración de Vistas y Observadores
//        dispararView.setModels(partidaModel, miTablero, tableroEnemigo);
        partidaModel.addObserver(dispararView);
        partidaModel.addObserver(posicionarNaveVista);
        partidaModel.addObserver(salaPartidaView);

        // 8. Registro de Pantallas
        viewController.registrarPantalla("disparar", dispararView);
        viewController.registrarPantalla("posicionar", posicionarNaveVista);
        viewController.registrarPantalla("menu", new MenuPrincipalViewFactory());
        viewController.registrarPantalla("registro", new RegistroViewFactory(registroController));
        viewController.registrarPantalla("lobby", new LobbyViewFactory(registroController));
        viewController.registrarPantalla("unirse", new UnirseAPartidaViewFactory(registroController, configController));
        viewController.registrarPantalla("esperandoPosicionamiento", new EsperandoPosicionamientoVista());
        viewController.registrarPantalla("victoria", new VictoriaViewFactory());
        viewController.registrarPantalla("derrota", new DerrotaViewFactory());
        

        viewController.registrarPantalla("salaPartida", new ViewFactory() {
            @Override
            public JPanel crear(ViewController control) {
                return salaPartidaView;
            }
        });

        // 9. Configuración de Eventos (Handlers)
        
        // Handlers existentes     
        
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("EXCEPTION", new ExceptionHandler(viewController));
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler(partidaModel));
        eventDispatcher.subscribe("PartidaFinalizada", new PartidaFinalizadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("NavesPosicionadas", new NavesPosicionadasHandler(viewController));
        
        eventDispatcher.subscribe("JugadorRegistrado", new JugadorRegistradoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("JugadorUnido", new JugadorUnidoHandler (viewController, partidaModel));
        eventDispatcher.subscribe("PartidaCancelada", new PartidaCanceladaHandler(viewController, partidaModel));

        eventDispatcher.subscribe("PartidaActualizada", new PartidaActualizadaHandler(partidaModel));
        eventDispatcher.subscribe("InicioPosicionamiento", new PosicionamientoHandler(viewController));
        eventDispatcher.subscribe("PartidaCancelada", new PartidaCanceladaHandler(viewController, partidaModel));
        

//        IMessageSubscriber redisSubscriber = new RedisSubscriber(jedisPool, executor, eventDispatcher);
//        redisSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);
        networkService.inicializar(eventDispatcher, executor);

        // 11. Arranque
        SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("menu");
        });
    }
}
