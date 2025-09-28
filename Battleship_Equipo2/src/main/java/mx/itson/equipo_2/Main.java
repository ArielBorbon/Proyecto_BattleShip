/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mx.itson.equipo_2;

import java.awt.Color;
import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author Cricri
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Jugador j1 = new Jugador("Sky", Color.BLUE);
        Jugador j2 = new Jugador("Yeye", Color.RED);
        
        PartidaModel pM = new PartidaModel(j1, j2);
        PartidaController pC = new PartidaController(pM);
        pC.realizarDisparo(j1, new CoordenadaDTO(1, 1));
        
        System.out.println(j1.getDisparos());
    }
    
}
