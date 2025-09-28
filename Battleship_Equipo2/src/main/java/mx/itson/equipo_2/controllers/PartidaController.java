/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.controllers;

import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.mapper.CoordenadaMapper;
import mx.itson.equipo_2.models.JugadorModel;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class PartidaController {
 
    
    private final PartidaModel partidaModel;

    public PartidaController(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

//    public PartidaController() {
//        this.partidaModel = new PartidaModel(j1, j2); 
//    }

    public ResultadoDisparo realizarDisparo(Jugador atacante, CoordenadaDTO c) {
        try {
            ResultadoDisparo resultado = partidaModel.realizarDisparo(atacante, CoordenadaMapper.toEntity(c));

        
            JugadorModel oponente = partidaModel.obtenerOponente(atacante);

            if (oponente.getTableroModel().todasNavesHundidas()) {
                System.out.println("¡Partida terminada! Ganador: " + atacante.getNombre());
            }

            return resultado;
        } catch (Exception e) {
            System.err.println("Error al realizar disparo: " + e.getMessage());
            return null; 
        }
    }

}
