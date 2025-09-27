/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.patterns.mediator;

import mx.itson.equipo_2.patterns.factory.ViewFactory;

/**
 *
 * @author skyro
 */
public interface ViewManager {
    void registrarPantalla(String nombre, ViewFactory factory);
    void cambiarPantalla(String nombre);
}
