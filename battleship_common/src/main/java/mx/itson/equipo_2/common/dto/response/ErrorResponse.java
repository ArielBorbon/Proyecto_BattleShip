/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.time.LocalDate;

/**
 * Clase de respuesta genérica para comunicar errores ocurridos en el servidor o
 * lógica de juego.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class ErrorResponse {

    private String mensaje;

    /**
     * Constructor vacío por defecto.
     */
    public ErrorResponse() {
    }

    /**
     * Constructor que recibe el mensaje de error.
     *
     * @param mensaje Descripción del error.
     */
    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Constructor alternativo que acepta una fecha (aunque no se almacena en
     * este ejemplo, podría usarse para logueo o extensión futura).
     *
     * @param mensaje Descripción del error.
     * @param fecha Fecha del error.
     */
    public ErrorResponse(String mensaje, LocalDate fecha) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el mensaje de error.
     *
     * @return String con el mensaje.
     */
    public String getMensaje() {
        return mensaje;
    }
}
