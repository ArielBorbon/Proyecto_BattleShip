/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import mx.itson.equipo_2.common.enums.EstadoCelda;

/**
 * Clase DTO que representa una celda individual dentro del tablero. Sabe si
 * contiene parte de una nave y si ha sido atacada.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class CeldaDTO {

    private EstadoCelda estado;
    private CoordenadaDTO coordenada;
    private NaveDTO nave;

    /**
     * Constructor vacío por defecto.
     */
    public CeldaDTO() {
    }

    /**
     * Constructor para inicializar una celda.
     *
     * @param estado Estado de la celda (AGUA, TOCADO, OCUPADO, etc.).
     * @param coordenada Posición de la celda en el tablero.
     * @param nave Referencia a la nave que ocupa la celda (puede ser null).
     */
    public CeldaDTO(EstadoCelda estado, CoordenadaDTO coordenada, NaveDTO nave) {
        this.estado = estado;
        this.coordenada = coordenada;
        this.nave = nave;
    }

    /**
     * Obtiene el estado de la celda.
     *
     * @return Enum EstadoCelda.
     */
    public EstadoCelda getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la celda.
     *
     * @param estado Enum EstadoCelda.
     */
    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la coordenada de la celda.
     *
     * @return Objeto CoordenadaDTO.
     */
    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    /**
     * Establece la coordenada de la celda.
     *
     * @param coordenada Objeto CoordenadaDTO.
     */
    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    /**
     * Obtiene la nave que ocupa esta celda.
     *
     * @return Objeto NaveDTO o null si está vacía.
     */
    public NaveDTO getNave() {
        return nave;
    }

    /**
     * Establece la nave que ocupa esta celda.
     *
     * @param nave Objeto NaveDTO.
     */
    public void setNave(NaveDTO nave) {
        this.nave = nave;
    }

    /**
     * Representación en cadena de la celda.
     *
     * @return String con los detalles de la celda.
     */
    @Override
    public String toString() {
        return "CeldaDTO{" + "estado=" + estado + ", coordenada=" + coordenada + ", nave=" + nave + '}';
    }
}
