/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 * Clase DTO que representa una nave individual en el tablero. Incluye su tipo,
 * estado de salud, posición inicial y orientación.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class NaveDTO {

    private TipoNave tipo;
    private EstadoNave estado;
    private CoordenadaDTO coordenadaInicial;
    private OrientacionNave orientacion;

    /**
     * Constructor vacío por defecto.
     */
    public NaveDTO() {
    }

    /**
     * Constructor para inicializar una nave.
     *
     * @param tipo Tipo de nave (Portaaviones, Submarino, etc.).
     * @param estado Estado actual de la nave (INTACTA, TOCADA, HUNDIDA).
     * @param coordenadaInicial Coordenada de la proa de la nave.
     * @param orientacion Orientación en el tablero (HORIZONTAL o VERTICAL).
     */
    public NaveDTO(TipoNave tipo, EstadoNave estado, CoordenadaDTO coordenadaInicial, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = estado;
        this.coordenadaInicial = coordenadaInicial;
        this.orientacion = orientacion;
    }

    /**
     * Obtiene el tipo de nave.
     *
     * @return Enum TipoNave.
     */
    public TipoNave getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de nave.
     *
     * @param tipo Enum TipoNave.
     */
    public void setTipo(TipoNave tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el estado de salud de la nave.
     *
     * @return Enum EstadoNave.
     */
    public EstadoNave getEstado() {
        return estado;
    }

    /**
     * Establece el estado de salud de la nave.
     *
     * @param estado Enum EstadoNave.
     */
    public void setEstado(EstadoNave estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la coordenada inicial (proa) de la nave.
     *
     * @return Objeto CoordenadaDTO.
     */
    public CoordenadaDTO getCoordenadaInicial() {
        return coordenadaInicial;
    }

    /**
     * Establece la coordenada inicial de la nave.
     *
     * @param coordenadas Objeto CoordenadaDTO.
     */
    public void setCoordenadaInicial(CoordenadaDTO coordenadas) {
        this.coordenadaInicial = coordenadas;
    }

    /**
     * Obtiene la orientación de la nave.
     *
     * @return Enum OrientacionNave.
     */
    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    /**
     * Establece la orientación de la nave.
     *
     * @param orientacion Enum OrientacionNave.
     */
    public void setOrientacion(OrientacionNave orientacion) {
        this.orientacion = orientacion;
    }

    /**
     * Representación en cadena de la nave.
     *
     * @return String con los detalles de la nave.
     */
    @Override
    public String toString() {
        return "NaveDTO{" + "tipo=" + tipo + ", estado=" + estado + ", coordenada=" + coordenadaInicial + ", orientacion=" + orientacion + '}';
    }
}
