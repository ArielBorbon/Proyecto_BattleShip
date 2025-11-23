/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author Cricri
 */
public class Tablero {

    public static final int FILAS = 10;
    public static final int COLUMNAS = 10;

    private final Celda[][] celdas;
    private final List<Nave> naves;

    public Tablero() {
        this.celdas = new Celda[FILAS][COLUMNAS];
        this.naves = new ArrayList<>();

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                this.celdas[f][c] = new Celda(new Coordenada(f, c));
            }
        }
    }

    public ResultadoDisparo recibirDisparo(CoordenadaDTO coordenada) {
        int fila = coordenada.getFila();
        int col = coordenada.getColumna();

        Celda celdaAtacada = getCelda(fila, col);
        if (celdaAtacada == null) {
            throw new IndexOutOfBoundsException("Disparo fuera del tablero: Fila=" + fila + ", Col=" + col);
        }

        if (celdaAtacada.getEstado() == EstadoCelda.DISPARADA) {
            return ResultadoDisparo.AGUA; // Ya habías disparado aquí
        }

        // Marcar la celda como disparada
        celdaAtacada.setEstado(EstadoCelda.DISPARADA);
        
        if (celdaAtacada.getNave() == null) {
            return ResultadoDisparo.AGUA; // Le diste al agua
        }

        // Si llegamos aquí, golpeamos una nave.
        Nave nave = celdaAtacada.getNave();

        System.out.println("Nave atacada: " + nave.toString());
        // --- INICIO DE LA MODIFICACIÓN (Lógica de Orquestación) ---
        
        // 1. El Tablero (gestor) recolecta la información
        List<EstadoCelda> estados = new ArrayList<>();
        for (Coordenada coord : nave.getCoordenadas()) {
            estados.add(this.getCelda(coord.getFila(), coord.getColumna()).getEstado());
        }

        // 2. El Tablero "le dice" (Tell) a la Nave que se actualice
        //    pasándole SOLO los datos que necesita.
        nave.actualizarEstado(estados);
        
        // --- FIN DE LA MODIFICACIÓN ---

        // Ahora, 'estaHundida()' devolverá el valor correcto
        if (nave.estaHundida()) {
            System.out.println("¡Nave hundida: " + nave.getTipo().toString() + "!");
            return ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;
        } else {
            return ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;
        }
    }

    public void posicionarNave(Nave nave) {
        
        for (Coordenada coord : nave.getCoordenadas()) {
            Celda celda = getCelda(coord.getFila(), coord.getColumna());
            if (celda == null) {
                // Lanza un error claro en lugar de fallar silenciosamente
                throw new IllegalArgumentException("Intento de posicionar nave fuera de límites en: " + coord);
            }
            
            // Regla 2: ¿La celda ya está ocupada? (Superposición)
            if (celda.getNave() != null) {
                throw new IllegalArgumentException("Intento de posicionar nave sobre otra en: " + coord);
            }
        }
        
        this.naves.add(nave);
        for (Coordenada coord : nave.getCoordenadas()) {
            getCelda(coord.getFila(), coord.getColumna()).setNave(nave);
        }
    }

    
    public Celda getCelda(int fila, int col) {
        if (fila >= 0 && fila < FILAS && col >= 0 && col < COLUMNAS) {
            return celdas[fila][col];
        }
        return null;
    }

    public boolean todasNavesHundidas() {
        if (naves.isEmpty()) {
            return false;
        }
        for (Nave nave : naves) {
            if (!nave.estaHundida()) {
                return false;
            }
        }
        return true;
    }

    public Celda[][] getCeldas() {
        return celdas;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    
}
