/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author CISCO
 */
public class NaveDTO {

    private TipoNave tipo;
    private EstadoNave estado;
    private CoordenadaDTO coordenadaInicial;
    private OrientacionNave orientacion;

    public NaveDTO() {
    }

    public NaveDTO(TipoNave tipo, EstadoNave estado, CoordenadaDTO coordenadaInicial, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = estado;
        this.coordenadaInicial = coordenadaInicial;
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

    public CoordenadaDTO getCoordenadaInicial() {
        return coordenadaInicial;
    }

    public void setCoordenadaInicial(CoordenadaDTO coordenadas) {
        this.coordenadaInicial = coordenadas;
    }

    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(OrientacionNave orientacion) {
        this.orientacion = orientacion;
    }

    @Override
    public String toString() {
        return "NaveDTO{" + "tipo=" + tipo + ", estado=" + estado + ", coordenada=" + coordenadaInicial + ", orientacion=" + orientacion + '}';
    }
}
