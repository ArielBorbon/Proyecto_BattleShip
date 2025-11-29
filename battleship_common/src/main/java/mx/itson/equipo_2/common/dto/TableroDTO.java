/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Clase DTO que representa el tablero de juego de un jugador. Contiene la
 * matriz de celdas y la lista de naves desplegadas.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class TableroDTO implements Serializable {

    private CeldaDTO[][] celdas;
    private List<NaveDTO> naves;

    /**
     * Constructor vacío por defecto.
     */
    public TableroDTO() {
    }

    /**
     * Constructor para inicializar el tablero.
     *
     * @param celdas Matriz bidimensional de CeldaDTO.
     * @param naves Lista de NaveDTO presentes en el tablero.
     */
    public TableroDTO(CeldaDTO[][] celdas, List<NaveDTO> naves) {
        this.celdas = celdas;
        this.naves = naves;
    }

    /**
     * Obtiene la matriz de celdas.
     *
     * * @return Arreglo bidimensional de CeldaDTO.
     */
    public CeldaDTO[][] getCeldas() {
        return celdas;
    }

    /**
     * Establece la matriz de celdas.
     *
     * @param celdas Arreglo bidimensional de CeldaDTO.
     */
    public void setCeldas(CeldaDTO[][] celdas) {
        this.celdas = celdas;
    }

    /**
     * Obtiene la lista de naves en el tablero.
     *
     * @return Lista de NaveDTO.
     */
    public List<NaveDTO> getNaves() {
        return naves;
    }

    /**
     * Establece la lista de naves en el tablero.
     *
     * @param naves Lista de NaveDTO.
     */
    public void setNaves(List<NaveDTO> naves) {
        this.naves = naves;
    }

    /**
     * Representación en cadena del tablero.
     *
     * @return String con la información del tablero.
     */
    @Override
    public String toString() {
        return "TableroDTO{" + "celdas=" + celdas + ", naves=" + naves + '}';
    }
}
