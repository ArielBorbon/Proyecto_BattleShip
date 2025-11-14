/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public class TableroModel {

    private String idJugaodr;
    private CeldaModel[][] celdas;
    public static final int TAMANIO = 10;

    public TableroModel(String idJugador) {
        this.idJugaodr = idJugador;
        this.celdas = new CeldaModel[TAMANIO][TAMANIO];
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                this.celdas[f][c] = new CeldaModel(f, c, false, EstadoCelda.NO_DISPARADA);
            }
        }
    }

    public void actualizarCelda(CoordenadaDTO coord, ResultadoDisparo resultado, List<CoordenadaDTO> coordsHundidas) {

        if (resultado == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO && coordsHundidas != null) {
            // HUNDIDO: Marcar TODAS las celdas de la nave como HUNDIDO
            for (CoordenadaDTO c : coordsHundidas) {
                CeldaModel celda = this.getCelda(c.getFila(), c.getColumna());
                celda.setEstado(EstadoCelda.DISPARADA);
                celda.setEstadoNave(EstadoNave.HUNDIDO); // <-- GUARDAR ESTADO
            }
        } else {
            // AGUA o IMPACTO simple
            CeldaModel celda = this.getCelda(coord.getFila(), coord.getColumna());
            celda.setEstado(EstadoCelda.DISPARADA);
            
            if (resultado == ResultadoDisparo.AGUA) {
                // No hacemos nada, estadoNave sigue siendo 'null'
            } else {
                // IMPACTO_SIN_HUNDIMIENTO
                celda.setEstadoNave(EstadoNave.AVERIADO); // <-- GUARDAR ESTADO
            }
        }
        
        // ¡BORRA ESTO! La notificación la hace PartidaModel
        // notifyObservers(dto); 
    }

    public TableroModel(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel getCelda(int fila, int columna) {
        return celdas[fila][columna];
    }

    @Override
    public String toString() {
        return "TableroModel{" + "celdas=" + celdas + '}';
    }

    public void posicionarNaves(List<NaveDTO> naves) {
        for (NaveDTO nave : naves) {
            for (CoordenadaDTO coord : nave.getCoordenadas()) {
                CeldaModel celda = this.getCelda(coord.getFila(), coord.getColumna());
                if (celda != null) {
                    celda.setEstado(EstadoCelda.NO_DISPARADA);
                    celda.setEstadoNave(EstadoNave.SIN_DANIOS);
                    celda.setTipoNave(nave.getTipo());
                }
            }
        }
        System.out.println("TableroModel local poblado con " + naves.size() + " naves.");
    }

}
