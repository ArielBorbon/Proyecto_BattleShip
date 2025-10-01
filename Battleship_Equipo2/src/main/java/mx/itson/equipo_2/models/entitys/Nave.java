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
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class Nave {
    private TipoNave tipo;
    private EstadoNave estado;
    private List<Coordenada> coordenadas;
    private OrientacionNave orientacion;

    public Nave() {
    }

    public Nave(TipoNave tipo, EstadoNave estado, List<Coordenada> coordenadas, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = estado;
        this.coordenadas = coordenadas;
        this.orientacion = orientacion;
    }
    
    

    public Nave(TipoNave tipo, EstadoNave estado, List<Coordenada> coordenadas, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.estado = estado;
        this.coordenadas = coordenadas;
        this.orientacion = orientacion;
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


