/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

import java.util.List;
import mx.itson.equipo_2.models.enums.EstadoNave;
import mx.itson.equipo_2.models.enums.OrientacionNave;
import mx.itson.equipo_2.models.enums.TipoNave;

/**
 *
 * @author skyro
 * @author sonic
 */
public class NaveDTO {
    
    private TipoNave tipo;
    private EstadoNave estado;
    private List<CoordenadaDTO> coordenadas;
    private OrientacionNave orientacion;

    public NaveDTO() {
    }

    public NaveDTO(TipoNave tipo, EstadoNave estado, List<CoordenadaDTO> coordenadas, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = estado;
        this.coordenadas = coordenadas;
        this.orientacion = orientacion;
    }

    public TipoNave getTipo() {
        return tipo;
    }

    public void setTipo(TipoNave tipo) {
        this.tipo = tipo;
    }

    public EstadoNave getEstado() {
        return estado;
    }

    public void setEstado(EstadoNave estado) {
        this.estado = estado;
    }

    public List<CoordenadaDTO> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<CoordenadaDTO> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(OrientacionNave orientacion) {
        this.orientacion = orientacion;
    }

    @Override
    public String toString() {
        return "NaveDTO{" + "tipo=" + tipo + ", estado=" + estado + ", coordenadas=" + coordenadas + ", orientacion=" + orientacion + '}';
    }
}
