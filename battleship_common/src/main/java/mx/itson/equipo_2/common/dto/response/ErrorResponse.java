/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.time.LocalDate;

/**
 *
 * @author skyro
 */
public class ErrorResponse {
    private String mensaje;
    private LocalDate fecha;

    public ErrorResponse() {
    }

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDate.now();
    }

    public ErrorResponse(String mensaje, LocalDate fecha) {
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}

