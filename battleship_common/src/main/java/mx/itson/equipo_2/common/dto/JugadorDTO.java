/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import java.util.List;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 * Clase DTO que representa a un jugador en la partida. Contiene su
 * identificación, tablero y el historial de sus disparos.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class JugadorDTO {

    private String id;
    private String nombre;
    private ColorJugador color;
    private TableroDTO tablero;
    private List<DisparoDTO> disparos;

    /**
     * Constructor vacío por defecto.
     */
    public JugadorDTO() {
    }

    /**
     * Constructor completo para inicializar un jugador.
     *
     * @param id Identificador único del jugador.
     * @param nombre Nombre visible del jugador.
     * @param color Color asociado al jugador.
     * @param tablero Estado actual del tablero del jugador.
     * @param disparos Lista de disparos realizados por el jugador.
     */
    public JugadorDTO(String id, String nombre, ColorJugador color, TableroDTO tablero, List<DisparoDTO> disparos) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.tablero = tablero;
        this.disparos = disparos;
    }

    /**
     * Obtiene el ID del jugador.
     *
     * @return String con el ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del jugador.
     *
     * @param id String con el ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Constructor alternativo sin ID ni color (útil para pruebas o datos
     * parciales).
     *
     * @param nombre Nombre del jugador.
     * @param tablero Tablero del jugador.
     * @param disparos Lista de disparos.
     */
    public JugadorDTO(String nombre, TableroDTO tablero, List<DisparoDTO> disparos) {
        this.nombre = nombre;
        // this.color = color;
        this.tablero = tablero;
        this.disparos = disparos;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return String con el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre String con el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el color del jugador.
     *
     * @return Enum ColorJugador.
     */
    public ColorJugador getColor() {
        return color;
    }

    /**
     * Establece el color del jugador.
     *
     * @param color Enum ColorJugador.
     */
    public void setColor(ColorJugador color) {
        this.color = color;
    }

    /**
     * Obtiene el tablero del jugador.
     *
     * @return Objeto TableroDTO.
     */
    public TableroDTO getTablero() {
        return tablero;
    }

    /**
     * Establece el tablero del jugador.
     *
     * @param tablero Objeto TableroDTO.
     */
    public void setTablero(TableroDTO tablero) {
        this.tablero = tablero;
    }

    /**
     * Obtiene la lista de disparos realizados.
     *
     * @return Lista de DisparoDTO.
     */
    public List<DisparoDTO> getDisparos() {
        return disparos;
    }

    /**
     * Establece la lista de disparos realizados.
     *
     * @param disparos Lista de DisparoDTO.
     */
    public void setDisparos(List<DisparoDTO> disparos) {
        this.disparos = disparos;
    }

    /**
     * Representación en cadena del jugador.
     *
     * @return String con los detalles del jugador.
     */
    @Override
    public String toString() {
        return "JugadorDTO{" + "id=" + id + ", nombre=" + nombre + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }
}
