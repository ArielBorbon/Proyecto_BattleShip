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
public class Celda {

    private EstadoCelda estado;
    private Coordenada coordenada;
    private Nave nave;

    public Celda() {
    }
    

    public Celda(Coordenada coordenada) {
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

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }
    

    public Nave getNave() {
        return nave;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }
    

    @Override
    public String toString() {
        return "CeldaEntity{" + "estado=" + estado + ", coordenada=" + coordenada + ", nave=" + nave + '}';
    }
    
    
}


