/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.common.broker;

/**
 *
 * @author skyro
 */
public interface IMessagePublisher {
    
    void publish(String canal, String msj);
    
}
