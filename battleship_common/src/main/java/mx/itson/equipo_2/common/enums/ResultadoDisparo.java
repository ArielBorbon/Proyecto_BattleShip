/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre    00000252116
 * @author Sebastián Bórquez Huerta          00000252115
 * @author Alberto Jiménez García            00000252595
 * @author José Eduardo Aguilar García       00000252049
 * @author José Luis Islas Molina            00000252574
 */
public enum ResultadoDisparo {
    AGUA("Has fallado :("),
    IMPACTO_SIN_HUNDIMIENTO("¡Has impactado!"),
    IMPACTO_CON_HUNDIMIENTO("¡Hundiste una nave!");
    
    private String mensaje;

    private ResultadoDisparo(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
    
    
}
