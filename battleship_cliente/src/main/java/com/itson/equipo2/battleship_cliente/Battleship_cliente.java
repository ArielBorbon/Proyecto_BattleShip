/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.itson.equipo2.battleship_cliente;

// --- Imports de Infraestructura y Broker ---
import com.itson.equipo2.communication.broker.ICommunicationProvider;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.redis.RedisProvider; // Único lugar donde importamos la implementación concreta
import com.itson.equipo2.communication.service.NetworkService;
import mx.itson.equipo_2.common.broker.BrokerConfig;

// --- Imports de Java Util ---
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

// --- Imports de Controladores, Modelos, Handlers y Vistas (Igual que antes) ---
import com.itson.equipo2.battleship_cliente.controllers.*;
import com.itson.equipo2.battleship_cliente.handler.*;
import com.itson.equipo2.battleship_cliente.models.*;
import com.itson.equipo2.battleship_cliente.service.*;
import com.itson.equipo2.battleship_cliente.pattern.factory.impl.*;
import com.itson.equipo2.battleship_cliente.view.SalaPartidaView;

public class Battleship_cliente {

    public static void main(String[] args) {
        System.out.println("Iniciando Cliente Battleship (Arquitectura Desacoplada)...");

        // -----------------------------------------------------------
        // 1. INFRAESTRUCTURA
        // -----------------------------------------------------------
        
        // Creamos el Bus de Eventos (Singleton)
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        
        // Creamos un pool de hilos estándar (Ya no dependemos de RedisConnection aquí)
        ExecutorService executor = Executors.newCachedThreadPool();

        // INYECCIÓN DE DEPENDENCIA:
        // Aquí decidimos usar REDIS. Si cambiamos de tecnología, solo tocamos esta línea.
        ICommunicationProvider provider = new RedisProvider(eventDispatcher, executor);

        // Obtenemos el Publicador a través del proveedor (para los servicios)
        IMessagePublisher publisher = provider.getPublisher();

        // -----------------------------------------------------------
        // 2. MODELOS
        // -----------------------------------------------------------
        PartidaModel partidaModel = new PartidaModel();
        TableroModel miTablero = new TableroModel((String) null);

        JugadorModel jugadorModel = new JugadorModel();
        jugadorModel.setTablero(miTablero);
        partidaModel.setYo(jugadorModel);

        // -----------------------------------------------------------
        // 3. SERVICIOS
        // -----------------------------------------------------------
        NetworkService networkService = new NetworkService(provider, BrokerConfig.CHANNEL_EVENTOS);
        
        // Los demás servicios usan la interfaz 'publisher' obtenida arriba
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(publisher);
        SalaService salaService = new SalaService(publisher);
        RealizarDisparoService disparoService = new RealizarDisparoService(publisher, jugadorModel);
        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(publisher, partidaModel);
        AbandonarPartidaService abandonarService = new AbandonarPartidaService(publisher, jugadorModel);

        // -----------------------------------------------------------
        // 4. CONTROLADORES
        // -----------------------------------------------------------
        VistaController viewController = new VistaController(partidaModel);
        
        ConfiguracionController configController = new ConfiguracionController(networkService, partidaModel);
        RegistroController registroController = new RegistroController(partidaModel);
        UnirsePartidaController unirseController = new UnirsePartidaController(registrarJugadorService, partidaModel);
        DisparoController disparoController = new DisparoController(disparoService);
        PosicionarController posicionarController = new PosicionarController(posicionarNaveService, partidaModel);
        AbandonarController abandonarController = new AbandonarController(abandonarService);
        SalaController salaController = new SalaController(salaService, viewController, abandonarService, partidaModel);

        posicionarController.setViewController(viewController);

        // -----------------------------------------------------------
        // 5. FACTORÍAS Y VISTAS
        // -----------------------------------------------------------
        DispararFactory dispararFactory = new DispararFactory(disparoController, abandonarController);
        PosicionarNaveFactory posicionarNaveFactory = new PosicionarNaveFactory(posicionarController);
        SalaPartidaView salaPartidaView = new SalaPartidaView(salaController);

        // -----------------------------------------------------------
        // 6. REGISTRO DE PANTALLAS
        // -----------------------------------------------------------
        viewController.registrarPantalla("disparar", dispararFactory);
        viewController.registrarPantalla("posicionar", posicionarNaveFactory);
        viewController.registrarPantalla("menu", new MenuPrincipalViewFactory());
        viewController.registrarPantalla("registro", new RegistroViewFactory(registroController));
        viewController.registrarPantalla("lobby", new LobbyViewFactory(unirseController, configController));
        viewController.registrarPantalla("unirse", new UnirseAPartidaViewFactory(unirseController, configController));
        viewController.registrarPantalla("esperandoPosicionamiento", new EsperandoPosicionamientoFactory());
        viewController.registrarPantalla("victoria", new VictoriaViewFactory());
        viewController.registrarPantalla("derrota", new DerrotaViewFactory());
        viewController.registrarPantalla("salaPartida", salaPartidaView);

        // -----------------------------------------------------------
        // 7. SUSCRIPCIÓN DE EVENTOS (HANDLERS)
        // -----------------------------------------------------------
        eventDispatcher.subscribe("DisparoRealizado", new DisparoRealizadoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("EXCEPTION", new ExceptionHandler(viewController));
        eventDispatcher.subscribe("PartidaIniciada", new PartidaIniciadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("TurnoTick", new TurnoTickHandler(partidaModel));
        eventDispatcher.subscribe("PartidaFinalizada", new PartidaFinalizadaHandler(viewController, partidaModel));
        eventDispatcher.subscribe("NavesPosicionadas", new NavesPosicionadasHandler(viewController));
        eventDispatcher.subscribe("JugadorRegistrado", new JugadorUnidoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("JugadorUnido", new JugadorUnidoHandler(viewController, partidaModel));
        eventDispatcher.subscribe("PartidaActualizada", new PartidaActualizadaHandler(partidaModel));
        eventDispatcher.subscribe("InicioPosicionamiento", new PosicionamientoHandler(viewController));
        eventDispatcher.subscribe("PartidaCancelada", new PartidaCanceladaHandler(viewController, partidaModel));

        // -----------------------------------------------------------
        // 8. ARRANQUE
        // -----------------------------------------------------------
        // Inicializar el servicio de red (inicia la suscripción usando el provider)
        try {
             // Inicia en localhost por defecto usando el provider que le pasamos
            networkService.inicializar();
        } catch (Exception e) {
            System.err.println("Error al iniciar servicio de red: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("menu");
        });
        
        // Shutdown Hook: Para cerrar conexiones limpiamente al cerrar la ventana
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación...");
            provider.close();
        }));
    }
}