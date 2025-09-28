/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.mapper.TableroMapper;
import mx.itson.equipo_2.models.entitys.Celda;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Nave;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.EstadoCelda;
import mx.itson.equipo_2.models.enums.EstadoNave;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.Subject;
import mx.itson.equipo_2.patterns.observer.TableroObserver;

/**
 *
 * @author Jose Eduardo Aguilar Garcia 
 */
public class TableroModel implements Subject<TableroObserver>{
    
    private final Tablero tablero;
     
    public List<TableroObserver> observadores;
    
    public TableroModel(Tablero tablero) {
        this.tablero = tablero;
        this.observadores = new ArrayList<>();
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

        
        if (celda.getEstado() == EstadoCelda.DISPARADA) {
            return ResultadoDisparo.AGUA; 
        }

        if (celda.getNave() == null) {
            celda.setEstado(EstadoCelda.DISPARADA);
            return ResultadoDisparo.AGUA;
        } else {
            Nave nave = celda.getNave();
            celda.setEstado(EstadoCelda.DISPARADA);

           
            boolean hundida = nave.getCoordenadas().stream()
                .allMatch(coord -> tablero.getCelda(coord.getFila(), coord.getColumna()).getEstado() == EstadoCelda.DISPARADA);

            if (hundida) {
                nave.setEstado(EstadoNave.HUNDIDO);
                return ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;
            } else {
                nave.setEstado(EstadoNave.AVERIADO);
                return ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;
            }
        }
    }

    public boolean todasNavesHundidas() {
        return tablero.getNaves().stream().allMatch(n -> n.getEstado() == EstadoNave.HUNDIDO);
    }

    @Override
    public void addObserver(TableroObserver observer) {
        observadores.add(observer);
    }

    @Override
    public void removeObserver(TableroObserver observer) {
        observadores.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(TableroObserver o : observadores) {
            o.update(TableroMapper.toDTO(this.tablero));
        }
    }
}
