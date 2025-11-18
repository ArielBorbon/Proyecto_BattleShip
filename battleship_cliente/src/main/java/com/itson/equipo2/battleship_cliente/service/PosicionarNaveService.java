/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author skyro
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
                n.isEsHorizontal() == true ? OrientacionNave.HORIZONTAL : OrientacionNave.VERTICAL)
        ).collect(Collectors.toList());

        PosicionarFlotaRequest req = new PosicionarFlotaRequest(partidaModel.getYo().getId(), naves);
        PosicionarFlotaRequest req2 = new PosicionarFlotaRequest(partidaModel.getEnemigo().getId(), naves);
        String payload = gson.toJson(req);
        String payload2 = gson.toJson(req2);

        EventMessage message = new EventMessage("PosicionarFlota", payload);
        EventMessage message2 = new EventMessage("PosicionarFlota", payload2);

        publisher.publish("battleship:comandos", message);

        new Thread(() -> {
            try {
                Thread.sleep(3000); // delay de 3 segundos
                 publisher.publish("battleship:comandos", message2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
