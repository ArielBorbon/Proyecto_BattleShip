
package mx.itson.equipo_2.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.dto.DisparoDTO;
import mx.itson.equipo_2.mapper.CoordenadaMapper;
import mx.itson.equipo_2.models.entitys.Celda;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Nave;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.EstadoCelda;
import mx.itson.equipo_2.models.enums.EstadoNave;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.TableroObserver;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class TableroModel {

    private final Tablero tablero;
    private final List<TableroObserver> observers = new ArrayList<>();

    public TableroModel(Tablero tablero) {
        this.tablero = tablero;
    }

    public void addObserver(TableroObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(TableroObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(DisparoDTO disparo) {
        for (TableroObserver to : observers) {
            to.onDisparo(this, disparo);
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

    public ResultadoDisparo recibirDisparo(Coordenada c) throws IllegalStateException {
        Celda celda = obtenerCelda(c);

        if (celda.getEstado() == EstadoCelda.DISPARADA) {
            throw new IllegalStateException("Esta celda ya ha sido disparada.");
        }

        ResultadoDisparo resultado;
        DisparoDTO dto;

        if (celda.getNave() == null) {
            celda.setEstado(EstadoCelda.DISPARADA);
            resultado = ResultadoDisparo.AGUA;

            dto = new DisparoDTO(resultado, new CoordenadaDTO(c.getFila(), c.getColumna()));

        } else {
            Nave nave = celda.getNave();
            celda.setEstado(EstadoCelda.DISPARADA);

            boolean hundida = nave.getCoordenadas().stream()
                    .allMatch(coord -> tablero.getCelda(coord.getFila(), coord.getColumna()).getEstado() == EstadoCelda.DISPARADA);

            if (hundida) {
                nave.setEstado(EstadoNave.HUNDIDO);
                resultado = ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;

                List<CoordenadaDTO> coordsHundidas = nave.getCoordenadas().stream()
                        .map(CoordenadaMapper::toDTO)
                        .collect(Collectors.toList());

                dto = new DisparoDTO(resultado, new CoordenadaDTO(c.getFila(), c.getColumna()), coordsHundidas);

            } else {
                nave.setEstado(EstadoNave.AVERIADO);
                resultado = ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;

                dto = new DisparoDTO(resultado, new CoordenadaDTO(c.getFila(), c.getColumna()));
            }
        }

        notifyObservers(dto);

        return resultado;
    }
    
    public ResultadoDisparo realizarDisparo(Jugador atacante, Coordenada coord) throws IllegalStateException {
    if (!validarCoordenada(coord)) {
        throw new IllegalArgumentException("Coordenada inválida: " + coord);
    }

    Celda celda = obtenerCelda(coord);

    if (celda.getEstado() == EstadoCelda.DISPARADA) {
        throw new IllegalStateException("Esta celda ya ha sido disparada.");
    }

    ResultadoDisparo resultado;
    DisparoDTO dto;

    if (celda.getNave() == null) {
        celda.setEstado(EstadoCelda.DISPARADA);
        resultado = ResultadoDisparo.AGUA;

        dto = new DisparoDTO(resultado, new CoordenadaDTO(coord.getFila(), coord.getColumna()));

    } else {
        Nave nave = celda.getNave();
        celda.setEstado(EstadoCelda.DISPARADA);

        boolean hundida = nave.getCoordenadas().stream()
                .allMatch(c -> tablero.getCelda(c.getFila(), c.getColumna()).getEstado() == EstadoCelda.DISPARADA);

        if (hundida) {
            nave.setEstado(EstadoNave.HUNDIDO);
            resultado = ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;

            List<CoordenadaDTO> coordsHundidas = nave.getCoordenadas().stream()
                    .map(CoordenadaMapper::toDTO)
                    .collect(Collectors.toList());

            dto = new DisparoDTO(resultado, new CoordenadaDTO(coord.getFila(), coord.getColumna()), coordsHundidas);
        } else {
            nave.setEstado(EstadoNave.AVERIADO);
            resultado = ResultadoDisparo.IMPACTO_SIN_HUNDIMIENTO;

            dto = new DisparoDTO(resultado, new CoordenadaDTO(coord.getFila(), coord.getColumna()));
        }
    }

   
    notifyObservers(dto);

       Disparo disparo = new Disparo(resultado, coord);
    atacante.agregarDisparo(disparo);

    return resultado;
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
