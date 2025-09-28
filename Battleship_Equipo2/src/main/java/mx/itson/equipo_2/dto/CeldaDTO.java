/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

import mx.itson.equipo_2.models.enums.EstadoCelda;

/**
 *
 * @author skyro
 */
public class CeldaDTO {

    private EstadoCelda estado;
    private CoordenadaDTO coordenada;
    private NaveDTO nave;

    public CeldaDTO() {
    }

    public CeldaDTO(EstadoCelda estado, CoordenadaDTO coordenada, NaveDTO nave) {
        this.estado = estado;
        this.coordenada = coordenada;
        this.nave = nave;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    public NaveDTO getNave() {
        return nave;
    }

    public void setNave(NaveDTO nave) {
        this.nave = nave;
    }

    @Override
    public String toString() {
        return "CeldaDTO{" + "estado=" + estado + ", coordenada=" + coordenada + ", nave=" + nave + '}';
    }

}
