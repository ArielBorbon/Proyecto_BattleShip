/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.patterns.observer;

import mx.itson.equipo_2.dto.DisparoDTO;
import mx.itson.equipo_2.models.TableroModel;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */

public interface TableroObserver {

    void onDisparo(TableroModel model, DisparoDTO disparo);
}
