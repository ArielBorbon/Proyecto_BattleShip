
package mx.itson.equipo_2.models.entitys;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */

public class Tablero {

    private Celda[][] celdas;
    private List<Nave> naves;

    public static final int TAMANIO = 10;

   
    public Celda[][] getCeldas() {
        return celdas;
    }
    
    public Tablero() {
        this.celdas = new Celda[TAMANIO][TAMANIO];
        this.naves = new ArrayList<>();
        inicializarCeldas();
    }

    public Tablero(Celda[][] celdas, List<Nave> naves) {
        this.celdas = celdas;
        this.naves = naves;
    }
    
    

    private void inicializarCeldas() {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                celdas[fila][col] = new Celda(new Coordenada(fila, col));
            }
        }
    }

    public Celda getCelda(int fila, int columna) {
        return celdas[fila][columna];
    }

    public void agregarNave(Nave nave) {
        naves.add(nave);
        for (Coordenada coord : nave.getCoordenadas()) {
            celdas[coord.getFila()][coord.getColumna()].setNave(nave);
        }
    }

    public List<Nave> getNaves() {
        return naves;
    }

    @Override
    public String toString() {
        return "TableroEntity{" + "celdas=" + celdas + ", naves=" + naves + '}';
    }
    
}

