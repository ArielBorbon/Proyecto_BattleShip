/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.communication.broker;

/**
 *
 * @author skyro
 */
public interface IMessageSubscriber {
 
    void subscribe(String channel);

    void unsubscribe();
}
