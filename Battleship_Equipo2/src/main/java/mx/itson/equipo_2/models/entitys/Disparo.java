
package mx.itson.equipo_2.models.entitys;

import mx.itson.equipo_2.models.enums.ResultadoDisparo;



/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class Disparo {
  
    private ResultadoDisparo resultado;
    private Coordenada coordenada;

    public Disparo() {
    }
    

    public Disparo(ResultadoDisparo resultado, Coordenada coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    @Override
    public String toString() {
        return "DisparoEntity{" + "resultado=" + resultado + ", coordenada=" + coordenada + '}';
    }
    
    
    
}


