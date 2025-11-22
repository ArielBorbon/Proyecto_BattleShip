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
import com.itson.equipo2.battleship_cliente.controllers.SalaController;
import com.itson.equipo2.battleship_cliente.handler.JugadorRegistradoHandler;
import com.itson.equipo2.battleship_cliente.handler.NavesPosicionadasHandler;
import com.itson.equipo2.battleship_cliente.handler.PartidaActualizadaHandler;
import com.itson.equipo2.battleship_cliente.pattern.factory.LobbyViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.MenuPrincipalViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.RegistroViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.UnirseAPartidaViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import com.itson.equipo2.battleship_cliente.service.SalaService;
import com.itson.equipo2.battleship_cliente.view.SalaPartidaView;
import javax.swing.JPanel;
import mx.itson.equipo_2.common.enums.EstadoJugador;

public class Battleship_clienteV2 {

    private static final String JUGADOR_IA_ID = "IA-123";

    public static void main(String[] args) {
        System.out.println("Iniciando Cliente Battleship (Arquitectura Limpia)...");

        // 1. Infraestructura
        JedisPool jedisPool = RedisConnection.getJedisPool();
        IMessagePublisher publisher = new RedisPublisher(jedisPool);
        ExecutorService executor = RedisConnection.getSubscriberExecutor();

        // 2. Modelos
        PartidaModel partidaModel = new PartidaModel();
        TableroModel miTablero = new TableroModel((String) null);
        TableroModel tableroEnemigo = new TableroModel(JUGADOR_IA_ID);

        JugadorModel jugadorModel = new JugadorModel();
        jugadorModel.setTablero(miTablero);
        partidaModel.setYo(jugadorModel);

        JugadorModel jugadorModelEnemigo = new JugadorModel(JUGADOR_IA_ID, "IA", ColorJugador.ROJO, EstadoJugador.LISTO, tableroEnemigo, new ArrayList<>());
        partidaModel.setEnemigo(jugadorModelEnemigo);

        // 3. Servicios
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(publisher);
        SalaService salaService = new SalaService(publisher);
        RealizarDisparoService disparoService = new RealizarDisparoService(publisher, jugadorModel);
        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(publisher, partidaModel);

        // 4. Controlador Principal de Vistas
        ViewController viewController = new ViewController();

        // 5. Controladores de Negocio
        RegistroController registroController = new RegistroController(partidaModel, registrarJugadorService);
        DisparoController disparoController = new DisparoController(disparoService);
        PosicionarController posicionarController = new PosicionarController(posicionarNaveService, partidaModel);
        SalaController salaController = new SalaController(salaService, viewController);

        // 6. Mediadores
        GameMediator gameMediator = new GameMediator();
        gameMediator.setPartidaController(disparoController);

        // 7. Vistas
        DispararView dispararView = new DispararView();
        dispararView.setMediator(gameMediator);

        PosicionarNaveVista posicionarNaveVista = new PosicionarNaveVista(posicionarController);

        // Inyectamos el controlador en lugar del publisher
        SalaPartidaView salaPartidaView = new SalaPartidaView(salaController);

        // Configuración de Vistas y Observadores
        dispararView.setModels(partidaModel, miTablero, tableroEnemigo);
        partidaModel.addObserver(dispararView);
        partidaModel.addObserver(posicionarNaveVista);
        // Importante: Agregar SalaPartidaView como observador para que reaccione a cambios de jugadores
        partidaModel.addObserver(salaPartidaView);

        // 8. Registro de Pantallas
        viewController.registrarPantalla("disparar", dispararView);
        viewController.registrarPantalla("posicionar", posicionarNaveVista);
        viewController.registrarPantalla("menu", new MenuPrincipalViewFactory());
        viewController.registrarPantalla("registro", new RegistroViewFactory(registroController));
        viewController.registrarPantalla("lobby", new LobbyViewFactory(registroController));
        viewController.registrarPantalla("unirse", new UnirseAPartidaViewFactory(registroController));

        // Registro manual de SalaPartida usando clase anónima o Factory
        viewController.registrarPantalla("salaPartida", new ViewFactory() {
            @Override
            public JPanel crear(ViewController control) {
                return salaPartidaView;
            }
        });

        // 9. Configuración de Eventos (Handlers)
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("EXCEPTION", new ExceptionHandler(viewController));
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler(partidaModel));
        eventDispatcher.subscribe("NavesPosicionadas", new NavesPosicionadasHandler(viewController));

        // Handler clave para el flujo de registro - sala
        eventDispatcher.subscribe("JugadorRegistrado", new JugadorRegistradoHandler(viewController, partidaModel));

        // Para actualizar la lista de jugadores en la sala
        eventDispatcher.subscribe("PartidaActualizada", new PartidaActualizadaHandler(partidaModel));

        // 10. Suscripción a Redis
        IMessageSubscriber redisSubscriber = new RedisSubscriber(jedisPool, executor, eventDispatcher);
        redisSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);

        // 11. Arranque
        SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("menu");
        });
    }
}
