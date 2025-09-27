/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;


import java.util.List;
import mx.itson.equipo_2.models.enums.EstadoNave;
import mx.itson.equipo_2.models.enums.OrientacionNave;
import mx.itson.equipo_2.models.enums.TipoNave;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class Nave {
    private TipoNave tipo;
    private EstadoNave estado;
    private List<Coordenada> coordenadas;
    private OrientacionNave orientacion;

    public Nave() {
    }
    

    public Nave(TipoNave tipo, List<Coordenada> coordenadas, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.coordenadas = coordenadas;
        this.orientacion = orientacion;
        this.estado = EstadoNave.SIN_DANIOS;
    }

    public TipoNave getTipo() {
        return tipo;
    }

    public EstadoNave getEstado() {
        return estado;
    }

    public void setEstado(EstadoNave estado) {
        this.estado = estado;
    }

    public List<Coordenada> getCoordenadas() {
        return coordenadas;
    }

    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    @Override
    public String toString() {
        return "NaveEntity{" + "tipo=" + tipo + ", estado=" + estado + ", coordenadas=" + coordenadas + ", orientacion=" + orientacion + '}';
    }
    
    
}


