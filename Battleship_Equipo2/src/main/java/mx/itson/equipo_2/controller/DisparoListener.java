/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.controller;

import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.exception.DisparoException;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public interface DisparoListener {
 
    ResultadoDisparo disparar(Jugador jugador, CoordenadaDTO coord) throws DisparoException;
}
