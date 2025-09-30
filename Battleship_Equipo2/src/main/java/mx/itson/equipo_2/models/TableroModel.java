/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.models.entitys.Celda;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Nave;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.EstadoCelda;
import mx.itson.equipo_2.models.enums.EstadoNave;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.TableroObserver;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class TableroModel {

    private final Tablero tablero;
    private final List<TableroObserver> observers = new ArrayList<>();

    public TableroModel(Tablero tablero) {
        this.tablero = tablero;
    }

    // --- NUEVO: Método para agregar observadores ---
    public void addObserver(TableroObserver observer) {
        this.observers.add(observer);
    }
    
        public void removeObserver(TableroObserver observer) {
        observers.remove(observer);
    }


    private void notifyObservers(int fila, int columna, ResultadoDisparo resultado) {
        for (TableroObserver observer : observers) {
            // Pasamos 'this' para que el observador sepa qué tablero cambió
            observer.onCeldaDisparada(this, fila, columna, resultado);
        }
    }

    public boolean validarCoordenada(Coordenada c) {
        return c.getFila() >= 0 && c.getFila() < Tablero.TAMANIO
                && c.getColumna() >= 0 && c.getColumna() < Tablero.TAMANIO;
    }

    public Celda obtenerCelda(Coordenada c) {
        if (!validarCoordenada(c)) {
            throw new IllegalArgumentException("Coordenada fuera de rango: " + c);
        }
        return tablero.getCelda(c.getFila(), c.getColumna());
    }

    public ResultadoDisparo recibirDisparo(Coordenada c) {
        Celda celda = obtenerCelda(c);

        // 1. Validación: Lanzar excepción si la celda ya fue disparada.
        // El controlador se encargará de capturar esta excepción y notificar al usuario.
        if (celda.getEstado() == EstadoCelda.DISPARADA) {
            throw new IllegalStateException("Esta celda ya ha sido disparada.");
        }

        // Variable para almacenar el resultado final.
        ResultadoDisparo resultado;

        // 2. Lógica del juego: Determinar el resultado del disparo.
        if (celda.getNave() == null) {
            celda.setEstado(EstadoCelda.DISPARADA);
            resultado = ResultadoDisparo.AGUA;
        } else {
            Nave nave = celda.getNave();
            celda.setEstado(EstadoCelda.DISPARADA);

            boolean hundida = nave.getCoordenadas().stream()
                    .allMatch(coord -> tablero.getCelda(coord.getFila(), coord.getColumna()).getEstado() == EstadoCelda.DISPARADA);

            if (hundida) {
                nave.setEstado(EstadoNave.HUNDIDO);
                resultado = ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;
            } else {
                nave.setEstado(EstadoNave.AVERIADO);
                resultado = ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;
            }
        }

        // 3. Notificación: Informar a todos los observadores sobre el cambio.
        // Esta es la parte clave del patrón Observer.
        notifyObservers(c.getFila(), c.getColumna(), resultado);

        // 4. Devolver el resultado.
        return resultado;
    }

    private ResultadoDisparo determinarResultadoAnterior(Celda celda) {
        if (celda.getNave() == null) {
            return ResultadoDisparo.AGUA;
        } else {
            return celda.getNave().getEstado() == EstadoNave.HUNDIDO
                    ? ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO
                    : ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;
        }
    }

    public boolean todasNavesHundidas() {
        return tablero.getNaves().stream().allMatch(n -> n.getEstado() == EstadoNave.HUNDIDO);
    }

    public Tablero getTablero() {
        return tablero;
    }

    public List<TableroObserver> getObservers() {
        return observers;
    }

}
