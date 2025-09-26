/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;

import mx.itson.equipo_2.models.enums.EstadoCelda;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class CeldaEntity {

    private EstadoCelda estado;
    private CoordenadaEntity coordenada;
    private NaveEntity nave;

    public CeldaEntity() {
    }
    

    public CeldaEntity(CoordenadaEntity coordenada) {
        this.coordenada = coordenada;
        this.estado = EstadoCelda.NO_DISPARADA;
        this.nave = null;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public CoordenadaEntity getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaEntity coordenada) {
        this.coordenada = coordenada;
    }
    

    public NaveEntity getNave() {
        return nave;
    }

    public void setNave(NaveEntity nave) {
        this.nave = nave;
    }
    

    @Override
    public String toString() {
        return "CeldaEntity{" + "estado=" + estado + ", coordenada=" + coordenada + ", nave=" + nave + '}';
    }
    
    
}


