/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.utils;

import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import com.itson.equipo2.battleship_cliente.view.DispararView;
import com.itson.equipo2.battleship_cliente.view.PosicionarNaveVista;
import mx.itson.equipo_2.common.broker.IMessagePublisher;

/**
 *
 * @author PC Gamer
 */
public class AppContext {

    // Instancias únicas (gestionadas aquí en lugar de Singletons)
    private static PartidaModel partidaModel;
    private static TableroModel tableroPropio;
    private static TableroModel tableroEnemigo;
    
    private static RealizarDisparoService realizarDisparoService;
    private static DisparoController disparoController;
    private static GameMediator gameMediator;
    private static ViewController viewController;
    private static IMessagePublisher publisher;

    // Previene instanciación
    private AppContext() {}

    /**
     * Inicializa todos los componentes de la aplicación.
     * @param pub El publicador de Redis.
     * @param vc El controlador de vistas (JFrame principal).
     * @param jugadorId ID del jugador local.
     * @param nombreJugador Nombre del jugador local.
     */
    public static void initialize(IMessagePublisher pub, ViewController vc, String jugadorId, String nombreJugador) {
        System.out.println("Inicializando AppContext (Contenedor de Dependencias)...");
        
        // Guardar componentes clave
        publisher = pub;
        viewController = vc;

        // 1. Crear Modelos (Estado centralizado)
        tableroPropio = new TableroModel();
        tableroEnemigo = new TableroModel();
        
        // Esta es la instancia que debemos pasar al servicio
        JugadorModel jugadorLocal = new JugadorModel(jugadorId, nombreJugador, false, tableroPropio, null);
        
        // PartidaModel ahora es una instancia gestionada, no un singleton
        partidaModel = new PartidaModel();
        partidaModel.setYo(jugadorLocal);
        
        System.out.println("Modelos [CREADOS]");

        // 2. Crear Servicios (CORRECCIÓN 1)
        // El constructor de RealizarDisparoService espera un JugadorModel, no un PartidaModel.
        // Le pasamos el jugadorLocal que acabamos de crear.
        realizarDisparoService = new RealizarDisparoService(publisher, jugadorLocal);
        System.out.println("RealizarDisparoService [CREADO]");

        // 3. Crear Controladores (CORRECCIÓN 2)
        // El constructor de DisparoController (el que tiene sentido usar aquí)
        // solo espera el servicio, no el modelo.
        disparoController = new DisparoController(realizarDisparoService);
        System.out.println("DisparoController [CREADO]");
        
        // 4. Crear Mediadores
        gameMediator = new GameMediator();
        // El mediador llama al controlador de disparos
        gameMediator.setPartidaController(disparoController);
        System.out.println("GameMediator [CREADO]");

        // 5. Registrar Vistas en el ViewController
        // La fábrica (lambda) ahora obtiene dependencias desde AppContext
        viewController.registrarPantalla("disparar", (v) -> {
            DispararView dv = new DispararView();
            // Inyectamos dependencias en la vista
            dv.setMediator(AppContext.getGameMediator()); 
            dv.setModels( // Inyección para cumplir con MVC
                AppContext.getPartidaModel(),
                AppContext.getTableroPropio(),
                AppContext.getTableroEnemigo()
            );
            return dv;
        });
        
        viewController.registrarPantalla("posicionar", (v) -> new PosicionarNaveVista());
        
        System.out.println("AppContext [LISTO]");
    }

    // Getters para acceder a las instancias gestionadas
    
    private static void checkInitialized() {
        if (partidaModel == null) {
            throw new IllegalStateException("AppContext no ha sido inicializado. Llama a AppContext.initialize() en tu clase main.");
        }
    }

    public static PartidaModel getPartidaModel() {
        checkInitialized();
        return partidaModel;
    }

    public static TableroModel getTableroPropio() {
        checkInitialized();
        return tableroPropio;
    }

    public static TableroModel getTableroEnemigo() {
        checkInitialized();
        return tableroEnemigo;
    }
    
    public static DisparoController getDisparoController() {
        checkInitialized();
        return disparoController;
    }

    public static RealizarDisparoService getRealizarDisparoService() {
        checkInitialized();
        return realizarDisparoService;
    }

    public static GameMediator getGameMediator() {
        checkInitialized();
        return gameMediator;
    }
    
    public static ViewController getViewController() {
        if (viewController == null) throw new IllegalStateException("ViewController no inicializado.");
        return viewController;
    }
    
    public static IMessagePublisher getPublisher() {
        if (publisher == null) throw new IllegalStateException("Publisher no inicializado.");
        return publisher;
    }
}