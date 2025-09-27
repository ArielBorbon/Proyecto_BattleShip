/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.patterns.factory;

import javax.swing.JPanel;
import mx.itson.equipo_2.patterns.mediator.ViewController;

/**
 *
 * @author skyro
 */
public interface ViewFactory {
    JPanel crear(ViewController control);
}
