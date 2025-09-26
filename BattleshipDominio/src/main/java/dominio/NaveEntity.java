/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import enums.EstadoNave;
import enums.OrientacionNave;
import enums.TipoNave;
import java.util.List;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class NaveEntity {
    private TipoNave tipo;
    private EstadoNave estado;
    private List<CoordenadaEntity> coordenadas;
    private OrientacionNave orientacion;

    public NaveEntity() {
    }
    

    public NaveEntity(TipoNave tipo, List<CoordenadaEntity> coordenadas, OrientacionNave orientacion) {
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

    public List<CoordenadaEntity> getCoordenadas() {
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


