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

    private String message;
    private LocalDate timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, LocalDate timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "message=" + message + ", timestamp=" + timestamp + '}';
    }

}
