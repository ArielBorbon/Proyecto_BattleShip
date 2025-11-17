/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class TableroModel {

    private String idJugador;
    private CeldaModel[][] celdas;
    public static final int TAMANIO = 10;

    public TableroModel(String idJugador) {
        this.idJugador = idJugador;
        this.celdas = new CeldaModel[TAMANIO][TAMANIO];
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                // Usa el nuevo constructor limpio
                this.celdas[f][c] = new CeldaModel(f, c);
            }
        }
    }

    public void actualizarCelda(CoordenadaDTO coord, ResultadoDisparo resultado, List<CoordenadaDTO> coordsHundidas) {

        if (resultado == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO && coordsHundidas != null) {
            // HUNDIDO: Marcar TODAS las celdas de la nave como HUNDIDO
            for (CoordenadaDTO c : coordsHundidas) {
                CeldaModel celda = this.getCelda(c.getFila(), c.getColumna());
                celda.setEstadoDisparo(EstadoCelda.DISPARADA);
                celda.setEstadoNave(EstadoNave.HUNDIDO); // <-- GUARDAR ESTADO
            }
        } else {
            // AGUA o IMPACTO simple
            CeldaModel celda = this.getCelda(coord.getFila(), coord.getColumna());
            celda.setEstadoDisparo(EstadoCelda.DISPARADA);

            if (resultado == ResultadoDisparo.AGUA) {
                // No hacemos nada, estadoNave sigue siendo 'null'
            } else {
                // IMPACTO_SIN_HUNDIMIENTO
                celda.setEstadoNave(EstadoNave.AVERIADO); // <-- GUARDAR ESTADO
            }
        }

        // ¡BORRA ESTO! La notificación la hace PartidaModel
        // notifyObservers(dto); 
    }

    public TableroModel(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel getCelda(int fila, int columna) {
        return celdas[fila][columna];
    }

    @Override
    public String toString() {
        return "TableroModel{" + "celdas=" + celdas + '}';
    }

    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugaodr(String idJugaodr) {
        this.idJugador = idJugaodr;
    }

    /**
     * Añade la nave al modelo de celdas. (Llamado por PosicionarController)
     */
    public boolean agregarNave(TipoNave tipo, int col, int fila, boolean esHorizontal) {

        // 1. Llama a la lógica de validación interna
        if (!esPosicionValida(tipo, col, fila, esHorizontal)) {
            return false; // Falla la validación
        }

        // 2. Si es válido, AHORA SÍ modificamos el estado
        // NOTA: Usamos 'tipo.getTamanio() - 1' basado en el bug de ancho que resolvimos.
        int tamanio = (tipo.getTamanio() > 1) ? tipo.getTamanio() - 1 : 1;

        for (int i = 0; i < tamanio; i++) {
            int c = col;
            int f = fila;

            if (esHorizontal) {
                c += i;
            } else {
                f += i;
            }

            // (Aunque 'esPosicionValida' ya lo comprobó, es buena práctica
            // asegurarnos de no escribir fuera de los límites).
            if (esCoordenadaValida(f, c)) {
                this.celdas[f][c].setTipoNave(tipo);
                this.celdas[f][c].setEstadoNave(EstadoNave.SIN_DANIOS);
            }
        }

        return true; // Éxito
    }

    private boolean esPosicionValida(TipoNave tipo, int col, int fila, boolean esHorizontal) {

        // NOTA: Usamos 'tipo.getTamanio() - 1' basado en el bug de ancho que resolvimos.
        // Si tu TipoNave.PORTAAVIONES tiene getTamanio() = 5,
        // y debe ocupar 4 celdas, este cálculo (5-1 = 4) es correcto.
        // Si debe ocupar 5, cambia 'tamanio' a 'tipo.getTamanio()'.
        int tamanio = (tipo.getTamanio() > 1) ? tipo.getTamanio() - 1 : 1;

        // Creamos una lista temporal de las celdas que la nave ocuparía
        java.util.List<CeldaModel> celdasNave = new java.util.ArrayList<>();

        for (int i = 0; i < tamanio; i++) {
            int c = col;
            int f = fila;

            if (esHorizontal) {
                c += i;
            } else {
                f += i;
            }

            // 1. Validar Límites (Extremos)
            if (!esCoordenadaValida(f, c)) {
                return false; // La nave se sale del tablero
            }

            // 2. Validar Colisión (Superposición)
            CeldaModel celda = this.celdas[f][c];
            if (celda.tieneNave()) { //
                return false; // Ya hay otra nave en esta celda
            }
            celdasNave.add(celda);
        }

        // 3. Validar Adyacencias (Regla de Battleship)
        // Solo si las celdas principales están bien, revisamos sus 8 vecinos
        for (CeldaModel celdaNave : celdasNave) {
            if (!sonAdyacenciasValidas(celdaNave, celdasNave)) {
                return false; // Hay una nave pegada (que no es esta misma)
            }
        }

        return true; // Pasó todas las validaciones
    }

    /**
     * Revisa los 8 vecinos de una celda para la regla de adyacencia. Devuelve
     * 'false' si un vecino tiene una nave Y no es parte de la nave que estamos
     * intentando colocar.
     */
    private boolean sonAdyacenciasValidas(CeldaModel celda, java.util.List<CeldaModel> celdasNaveActual) {
        int f = celda.getFila(); //
        int c = celda.getColumna(); //

        // Definir los 8 vecinos (arriba-izq, arriba, arriba-der, ... etc)
        int[] filasVecinas = {f - 1, f - 1, f - 1, f, f + 1, f + 1, f + 1, f};
        int[] colsVecinas = {c - 1, c, c + 1, c + 1, c + 1, c, c - 1, c - 1};

        for (int i = 0; i < 8; i++) {
            int vf = filasVecinas[i];
            int vc = colsVecinas[i];

            // Si el vecino está dentro de los límites del tablero
            if (esCoordenadaValida(vf, vc)) {
                CeldaModel vecino = this.celdas[vf][vc];

                // Si el vecino TIENE una nave...
                if (vecino.tieneNave()) {
                    // ...y esa nave NO es parte de la que estamos colocando,
                    // entonces es una colisión de adyacencia.
                    if (!celdasNaveActual.contains(vecino)) {
                        return false;
                    }
                }
            }
        }
        return true; // No se encontraron naves adyacentes
    }

    /**
     * Helper simple para checar límites de coordenadas.
     */
    private boolean esCoordenadaValida(int fila, int col) {
        return fila >= 0 && fila < TAMANIO && col >= 0 && col < TAMANIO;
    }

}
