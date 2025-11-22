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

    private final IMessagePublisher publisher;
    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public PosicionarNaveService(IMessagePublisher publisher, PartidaModel partidaModel) {
        this.publisher = publisher;
        this.partidaModel = partidaModel;
    }

    public void confirmarPosicionamiento() {
        TableroModel tablero = partidaModel.getYo().getTablero();
        List<NaveModel> navesPosicionadas = tablero.getNavesPosicionadas();

        List<NaveDTO> naves = navesPosicionadas.stream().map((n) -> new NaveDTO(
                n.getTipo(),
                EstadoNave.SIN_DANIOS,
                new CoordenadaDTO(
                        n.getFila(),
                        n.getColumna()),
                n.isEsHorizontal() ? OrientacionNave.HORIZONTAL : OrientacionNave.VERTICAL)
        ).collect(Collectors.toList());

        PosicionarFlotaRequest reqYo = new PosicionarFlotaRequest(partidaModel.getYo().getId(), naves);
        String payloadYo = gson.toJson(reqYo);
        EventMessage messageYo = new EventMessage("PosicionarFlota", payloadYo);

        System.out.println("Enviando flota posicionada al servidor...");

        publisher.publish("battleship:comandos", messageYo);

    }
}
