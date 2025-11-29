/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;
import mx.itson.equipo_2.common.enums.AccionPartida;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 * Clase DTO para manejar la solicitud de registro de un jugador, ya sea para
 * crear una nueva partida o unirse a una existente.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class RegistrarJugadorRequest implements Serializable {

    private String nombre;
    private AccionPartida accion; // CREAR o UNIRSE
    private ColorJugador color;
    private String jugadorId;

    /**
     * Constructor vacío por defecto.
     */
    public RegistrarJugadorRequest() {
    }

    /**
     * Constructor para inicializar una solicitud básica sin ID previo.
     *
     * @param nombre Nombre del jugador.
     * @param color Color de preferencia seleccionado.
     * @param accion Acción a realizar (CREAR o UNIRSE).
     */
    public RegistrarJugadorRequest(String nombre, ColorJugador color, AccionPartida accion) {
        this.nombre = nombre;
        this.color = color;
        this.accion = accion;
    }

    /**
     * Constructor completo para inicializar todos los campos.
     *
     * @param nombre Nombre del jugador.
     * @param accion Acción a realizar.
     * @param color Color del jugador.
     * @param jugadorId Identificador del jugador si ya existe.
     */
    public RegistrarJugadorRequest(String nombre, AccionPartida accion, ColorJugador color, String jugadorId) {
        this.nombre = nombre;
        this.accion = accion;
        this.color = color;
        this.jugadorId = jugadorId;
    }

    /**
     * Constructor simple solo con nombre.
     *
     * @param nombre Nombre del jugador.
     */
    public RegistrarJugadorRequest(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del jugador.
     *
     * @return El ID del jugador.
     */
    public String getJugadorId() {
        return jugadorId;
    }

    /**
     * Establece el ID del jugador.
     *
     * @param jugadorId El ID del jugador.
     */
    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre El nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la acción de la partida (CREAR o UNIRSE).
     *
     * @return Enum AccionPartida.
     */
    public AccionPartida getAccion() {
        return accion;
    }

    /**
     * Establece la acción de la partida.
     *
     * @param accion Enum AccionPartida.
     */
    public void setAccion(AccionPartida accion) {
        this.accion = accion;
    }

    /**
     * Obtiene el color elegido por el jugador.
     *
     * @return Enum ColorJugador.
     */
    public ColorJugador getColor() {
        return color;
    }

    /**
     * Establece el color elegido por el jugador.
     *
     * @param color Enum ColorJugador.
     */
    public void setColor(ColorJugador color) {
        this.color = color;
    }
}
