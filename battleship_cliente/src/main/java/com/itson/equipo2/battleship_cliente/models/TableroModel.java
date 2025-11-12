/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import com.itson.equipo2.battleship_cliente.pattern.observer.TableroObserver;
import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.DisparoDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public class TableroModel {

    private String idJugaodr;
    private CeldaModel[][] celdas;
    private final List<TableroObserver> observers = new ArrayList<>(); // Para el onDisparo
    public static final int TAMANIO = 10;

public TableroModel() {
        // Inicializar celdas vacías
        this.celdas = new CeldaModel[TAMANIO][TAMANIO];
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                this.celdas[f][c] = new CeldaModel(f, c, false, EstadoCelda.NO_DISPARADA);
            }
        }
    }



// --- MÉTODO LLAMADO POR EL HANDLER ---
    public void actualizarCelda(CoordenadaDTO coord, ResultadoDisparo resultado, List<CoordenadaDTO> coordsHundidas) {
        
        if (resultado == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO && coordsHundidas != null) {
            // Marcar todas las celdas del barco hundido
            for (CoordenadaDTO c : coordsHundidas) {
                this.getCelda(c.getFila(), c.getColumna()).setEstado(EstadoCelda.DISPARADA);
                // (Aquí faltaría la lógica para saber si es AGUA, TOCADO o HUNDIDO)
                // Por ahora, solo marcamos la celda como disparada.
                // La vista DispararView se encargará de pintarlo de ROJO/NEGRO
            }
        } else {
             // Marcar solo la celda disparada
             this.getCelda(coord.getFila(), coord.getColumna()).setEstado(EstadoCelda.DISPARADA);
        }
        
        // Notificar a la vista (DispararView) que debe repintar esta celda
        DisparoDTO dto = new DisparoDTO(resultado, coord, coordsHundidas);
        notifyObservers(dto);
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
                    celda.setTieneNave(true);
                }
            }
        }
        System.out.println("TableroModel local poblado con " + naves.size() + " naves.");
    }
    
    
    
    // --- Lógica de Observador (para DispararView) ---
    public void addObserver(TableroObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(TableroObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(DisparoDTO disparo) {
        for (TableroObserver to : observers) {
            to.onDisparo(this, disparo); // Notifica a DispararView
        }
    }

}
