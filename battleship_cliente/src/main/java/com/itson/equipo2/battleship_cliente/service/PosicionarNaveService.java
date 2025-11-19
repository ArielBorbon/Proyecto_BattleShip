package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.NaveModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;
import com.itson.equipo2.communication.dto.EventMessage;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.OrientacionNave;

/**
 * Servicio encargado de gestionar la comunicación con el servidor para el
 * posicionamiento de la flota del jugador.
 */
public class PosicionarNaveService {

    /**
     * Interfaz para publicar mensajes en el Broker de comunicación.
     */
    private final IMessagePublisher publisher;

    /**
     * Referencia al modelo de datos de la partida.
     */
    private final PartidaModel partidaModel;

    /**
     * Instancia de Gson para serialización y deserialización de objetos.
     */
    private final Gson gson = new Gson();

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el servicio.
     *
     * @param publisher La implementación del publicador de mensajes.
     * @param partidaModel El modelo de datos de la partida.
     */
    public PosicionarNaveService(IMessagePublisher publisher, PartidaModel partidaModel) {
        this.publisher = publisher;
        this.partidaModel = partidaModel;
    }

    // --- MÉTODOS PÚBLICOS ---
    /**
     * Convierte la flota posicionada del jugador a DTOs y la envía al servidor
     * en un mensaje de tipo "PosicionarFlota".
     * <p>
     * **NOTA**: El código incluye un envío adicional con un retraso de 3
     * segundos para el "enemigo". Esto parece ser un *mock* o simulación para
     * asegurar que la partida avance en un entorno de desarrollo.
     * </p>
     */
    public void confirmarPosicionamiento() {
        // 1. Obtener la flota del jugador
        TableroModel tablero = partidaModel.getYo().getTablero();
        List<NaveModel> navesPosicionadas = tablero.getNavesPosicionadas();

        // 2. Mapear las NaveModel locales a NaveDTOs para la comunicación
        List<NaveDTO> naves = navesPosicionadas.stream().map((n) -> new NaveDTO(
                n.getTipo(),
                EstadoNave.SIN_DANIOS,
                new CoordenadaDTO(
                        n.getFila(),
                        n.getColumna()),
                n.isEsHorizontal() ? OrientacionNave.HORIZONTAL : OrientacionNave.VERTICAL)
        ).collect(Collectors.toList());

        // 3. Crear el Request para el jugador local
        PosicionarFlotaRequest reqYo = new PosicionarFlotaRequest(partidaModel.getYo().getId(), naves);
        String payloadYo = gson.toJson(reqYo);
        EventMessage messageYo = new EventMessage("PosicionarFlota", payloadYo);

        // 4. Publicar el mensaje del jugador local
        publisher.publish("battleship:comandos", messageYo);

        // -----------------------------------------------------------
        // 5. [SIMULACIÓN/MOCK] Crear y enviar el Request del enemigo con un delay
        // -----------------------------------------------------------
        PosicionarFlotaRequest reqEnemigo = new PosicionarFlotaRequest(partidaModel.getEnemigo().getId(), naves);
        String payloadEnemigo = gson.toJson(reqEnemigo);
        EventMessage messageEnemigo = new EventMessage("PosicionarFlota", payloadEnemigo);

        new Thread(() -> {
            try {
                // Introduce un delay para simular que el enemigo también está posicionando.
                Thread.sleep(3000);
                publisher.publish("battleship:comandos", messageEnemigo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // -----------------------------------------------------------
    }
}
