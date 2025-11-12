/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Nave;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.service.PartidaTimerService;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import com.itson.equipo2.battleship_servidor.infrastructure.service.AIService;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author Cricri
 */
public class PartidaApplicationService implements IMessageHandler {

    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final Gson gson;

    private final AIService aiService; // <-- NUEVA DEPENDENCIA
    private final String IA_PLAYER_ID = "JUGADOR_IA_01"; // <-- ID Fijo para la IA
    
    
    private final PartidaTimerService partidaTimerService; // <-- AÑADIR
    
    
    

    public PartidaApplicationService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher, Gson gson, AIService aiService) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
        this.gson = gson;
        this.aiService = aiService; // <-- Inyectar
        this.partidaTimerService = new PartidaTimerService(); // <-- INICIALIZAR
    }

    @Override
    public void onMessage(EventMessage eventMessage) {
        try {
            String tipo = eventMessage.getEventType();

            if (tipo == null) {
                System.err.println("Comando inválido, no contiene 'type': " + eventMessage);
                return;
            }

            System.out.println("Procesando comando tipo: " + tipo);

            switch (tipo) {

                case "CrearPartidaVsIA":
                    CrearPartidaVsIARequest crearRequest = gson.fromJson(
                            eventMessage.getPayload(),
                            CrearPartidaVsIARequest.class
                    );
                    procesarCrearPartidaVsIA(crearRequest);
                    break;

                case "RegistrarJugador":
                    System.out.println("Lógica de 'RegistrarJugador' iría aquí.");
                    break;

                case "RealizarDisparo":
                    RealizarDisparoRequest disparoRequest = gson.fromJson(
                            eventMessage.getPayload(),
                            RealizarDisparoRequest.class
                    );
                    procesarDisparo(disparoRequest);
                    break;

                case "PosicionarNave":
                    System.out.println("Lógica de 'PosicionarNave' iría aquí.");
                    break;

                default:
                    System.err.println("Tipo de comando desconocido: " + tipo);
            }

        } catch (JsonSyntaxException e) {
            System.err.println("Error de sintaxis JSON al procesar mensaje: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al manejar comando: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void procesarDisparo(RealizarDisparoRequest request) {
        try {
//            
//            // --- 0. DETENER EL TIMER ---
            partidaTimerService.cancelCurrentTimer();
//            
//            // --- 1. OBTENER DATOS ---
//            // (Usando la versión de "una sola partida")
            Partida partida = partidaRepository.getPartida();
//
            if (partida == null) {
                System.err.println("Error: No se encontró la partida en el repositorio.");
                return;
            }
//
//// --- 2. DELEGAR AL DOMINIO ---
//            // Esta respuesta AHORA CONTIENE el turno y estado actualizados
            ResultadoDisparoReponse resultadoResponse = partida.realizarDisparo(
                    request.getJugadorId(),
                    request.getCoordenada()
            );
//
//            // --- 3. GUARDAR ESTADO ---
            partidaRepository.guardar(partida);
//
////            ResultadoDisparoReponse resultadoDTO = new ResultadoDisparoReponse(
////                    request.getCoordenada(),
////                    resultado,
////                    request.getJugadorId()
////            );
            EventMessage eventoResultado = new EventMessage(
                    "DisparoRealizado",
                    gson.toJson(resultadoResponse)
            );
//
////            EventMessage eventoResultado = new EventMessage(
////                    "DisparoRealizado",
////                    gson.toJson(resultadoDTO)
////            );
            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, eventoResultado);
//
            System.out.println("Disparo procesado. Resultado: " + resultadoResponse);

            
            // --- 6. REINICIAR EL TIMER ---
            // Inicia el timer para el jugador actual (sea el mismo o el nuevo)
            if (partida.getEstado() == EstadoPartida.EN_BATALLA) {
                 partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
            }
            
            

        } catch (IllegalStateException e) {
            // Esto es un "error" esperado. Un jugador (IA o Humano) intentó
            // disparar fuera de turno (debido a lag o race conditions).
            // Simplemente lo registramos como información y continuamos.
            System.out.println("INFO: Disparo rechazado (fuera de turno): " + e.getMessage());
            // No publicamos un error al cliente, ya que su UI ya debería
            // reflejar el estado correcto (botón deshabilitado o turno del oponente).
        // --- FIN DE LA CORRECCIÓN ---
        } catch (Exception e) {
            // Errores *inesperados* (ej. base de datos, JSON malformado) 
            // sí deben registrarse como graves.
            System.err.println("Error INESPERADO procesando el disparo: " + e.getMessage());
            e.printStackTrace();
            // (Aquí sí se podría publicar un ErrorResponse al cliente)
        }
    }

    // --- NUEVO MÉTODO ---
    private void procesarCrearPartidaVsIA(CrearPartidaVsIARequest request) {
        // 1. Crear Jugadores
        Jugador jugadorHumano = new Jugador(request.getJugadorHumanoId(), "Humano");
        Jugador jugadorIA = new Jugador(IA_PLAYER_ID, "IA");

        // 2. Crear Partida
        Partida partida = new Partida(jugadorHumano);

        // 3. Posicionar Naves
        List<Nave> navesHumano = request.getNavesHumano().stream()
                .map(dto -> new Nave(dto.getTipo(), dto.getCoordenadas(), dto.getOrientacion()))
                .collect(Collectors.toList());
        partida.posicionarNaves(jugadorHumano.getId(), navesHumano);

        List<Nave> navesIA = request.getNavesIA().stream()
                .map(dto -> new Nave(dto.getTipo(), dto.getCoordenadas(), dto.getOrientacion()))
                .collect(Collectors.toList());

        // 4. Unir IA y posicionar sus naves
        partida.unirseAPartida(jugadorIA, jugadorHumano.getId()); // Inicia el Humano
        partida.posicionarNaves(jugadorIA.getId(), navesIA);

        // 5. Guardar
        partidaRepository.guardar(partida);
        System.out.println("Partida Vs IA creada. Turno de: " + partida.getTurnoActual());

// Se llama al constructor sin el parámetro 'Color'
        JugadorDTO j1DTO = new JugadorDTO(jugadorHumano.getId(), "Humano", null, null);
        JugadorDTO j2DTO = new JugadorDTO(jugadorIA.getId(), "IA", null, null);
        // --- FIN DE LA CORRECCIÓN ---
        
        

        PartidaIniciadaResponse response = new PartidaIniciadaResponse(
                partida.getId().toString(),
                j1DTO,
                j2DTO,
                partida.getEstado(),
                partida.getTurnoActual()
        );

        
        
        
        eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS,
                new EventMessage("PartidaIniciada", gson.toJson(response)));
        
        // --- 7. INICIAR EL TIMER POR PRIMERA VEZ ---
        partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return true;
    }
}
