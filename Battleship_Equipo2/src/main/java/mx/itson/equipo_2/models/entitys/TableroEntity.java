/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
import java.util.ArrayList;
import java.util.List;

public class TableroEntity {

    private CeldaEntity[][] celdas;
    private List<NaveEntity> naves;

    public static final int TAMANIO = 10;

   
    public CeldaEntity[][] getCeldas() {
        return celdas;
    }
    
    public TableroEntity() {
        this.celdas = new CeldaEntity[TAMANIO][TAMANIO];
        this.naves = new ArrayList<>();
        inicializarCeldas();
    }

    private void inicializarCeldas() {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                celdas[fila][col] = new CeldaEntity(new CoordenadaEntity(fila, col));
            }
        }
    }

    public CeldaEntity getCelda(int fila, int columna) {
        return celdas[fila][columna];
    }

    public void agregarNave(NaveEntity nave) {
        naves.add(nave);
        for (CoordenadaEntity coord : nave.getCoordenadas()) {
            celdas[coord.getFila()][coord.getColumna()].setNave(nave);
        }
    }

    public List<NaveEntity> getNaves() {
        return naves;
    }

    @Override
    public String toString() {
        return "TableroEntity{" + "celdas=" + celdas + ", naves=" + naves + '}';
    }
    
}

