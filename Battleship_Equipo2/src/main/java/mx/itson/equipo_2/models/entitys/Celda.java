
package mx.itson.equipo_2.models.entitys;

import mx.itson.equipo_2.models.enums.EstadoCelda;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
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

    public Celda(EstadoCelda estado, Coordenada coordenada, Nave nave) {
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


