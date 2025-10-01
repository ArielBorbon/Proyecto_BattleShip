/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

import java.util.List;

/**
 *
 * @author skyro
 * @author sonic
 */
public class TableroDTO {

    private CeldaDTO[][] celdas;
    private List<NaveDTO> naves;

    public TableroDTO() {
    }

    public TableroDTO(CeldaDTO[][] celdas, List<NaveDTO> naves) {
        this.celdas = celdas;
        this.naves = naves;
    }

    public CeldaDTO[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(CeldaDTO[][] celdas) {
        this.celdas = celdas;
    }

    public List<NaveDTO> getNaves() {
        return naves;
    }

    public void setNaves(List<NaveDTO> naves) {
        this.naves = naves;
    }

    @Override
    public String toString() {
        return "TableroDTO{" + "celdas=" + celdas + ", naves=" + naves + '}';
    }
}
