/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author Cricri
 */
public class Nave {

    private final TipoNave tipo;
    private EstadoNave estado;

    private List<Coordenada> coordenadas;
    private OrientacionNave orientacion;

    public Nave(TipoNave tipo) {
        this.tipo = tipo;
        this.estado = EstadoNave.SIN_DANIOS;
    }

    public Nave(TipoNave tipo, Coordenada coordenadaInicial, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = EstadoNave.SIN_DANIOS; // Asumimos estado inicial
        this.coordenadas = new ArrayList<>();
        this.orientacion = orientacion; // <-- Guarda la orientación

        // Lógica para calcular las coordenadas (la misma que en el cliente)
        // NOTA: Ajusta 'tamanio' si sigues usando el bug de ancho (tipo.getTamanio() - 1)
        int tamanio = tipo.getTamanio();
//        int tamanio = (tipo.getTamanio() > 1) ? tipo.getTamanio() - 1 : 1; // Usar el tamaño real del Enum

        for (int i = 0; i < tamanio; i++) {
            int c = coordenadaInicial.getColumna();
            int f = coordenadaInicial.getFila();

            // Asumiendo que 'orientacion' es un Enum (HORIZONTAL/VERTICAL)
            if (orientacion == OrientacionNave.HORIZONTAL) {
                c += i;
            } else {
                f += i;
            }

            // Crea el objeto de Dominio 'Coordenada', no el DTO
            this.coordenadas.add(new Coordenada(f, c));
        }
    }

    public List<Coordenada> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<Coordenada> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(OrientacionNave orientacion) {
        this.orientacion = orientacion;
    }

    public TipoNave getTipo() {
        return tipo;
    }

    public EstadoNave getEstado() {
        return estado;
    }

    public void actualizarEstado(List<EstadoCelda> estadosDeMisCeldas) {
        int celdasImpactadas = 0;

        // 1. Iterar sobre la lista de estados que nos pasó el Tablero
        for (EstadoCelda estado : estadosDeMisCeldas) {

            // 2. Verificar si esa celda ha sido disparada
            if (estado == EstadoCelda.DISPARADA) {
                celdasImpactadas++;
            }
        }

        // 3. Actualizar el estado de la nave basado en los impactos
        if (celdasImpactadas == 0) {
            this.estado = EstadoNave.SIN_DANIOS;
        } else if (celdasImpactadas < this.coordenadas.size()) {
            // (Asegúrate de tener este Enum)
            this.estado = EstadoNave.AVERIADO;
        } else {
            // (Asegúrate de tener este Enum)
            this.estado = EstadoNave.HUNDIDO;
        }
        System.out.println("Total impactos: " + celdasImpactadas + "/" + getTipo().getTamanio());
    }

    public boolean estaHundida() {
        // --- ARREGLO DEL BUG ---
        // Antes comparaba con SIN_DANIOS, lo cual era incorrecto.
        return this.estado == EstadoNave.HUNDIDO;
    }

    @Override
    public String toString() {
        return "Nave{" + "tipo=" + tipo + ", estado=" + estado + ", coordenadas=" + coordenadas + ", orientacion=" + orientacion + '}';
    }

}
