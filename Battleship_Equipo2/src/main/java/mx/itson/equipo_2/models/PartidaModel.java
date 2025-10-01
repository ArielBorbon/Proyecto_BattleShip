
package mx.itson.equipo_2.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Partida;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.PartidaObserver;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class PartidaModel {

    private final Partida partida;
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final TableroModel tableroModel1;
    private final TableroModel tableroModel2;
    private String ultimoError;



    private int tiempoRestante;
    private final int DURACION_TURNO = 30; 

    private List<PartidaObserver> observers;

    public PartidaModel(Jugador j1, Jugador j2) {
        this.partida = new Partida(j1, j2);
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.tableroModel1 = new TableroModel(j1.getTablero());
        this.tableroModel2 = new TableroModel(j2.getTablero());
        this.observers = new ArrayList<>();
    }
    

    public void iniciarTurno() {
        this.tiempoRestante = DURACION_TURNO;
        notifyObservers(); 
    }
    
    public void repetirTurno() {
        this.tiempoRestante = DURACION_TURNO;
        notifyObservers(); 
    }

    public void pasarTurno() {
        if (partidaFinalizada()) return;
        partida.cambiarTurno();
       
    }
    
    public void decrementarTiempo() {
        if (tiempoRestante > 0) {
            tiempoRestante--;
            notifyObservers(); 
        }
    }



    public void addObserver(PartidaObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(PartidaObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (PartidaObserver to : observers) {
            to.onChange(this);
        }
    }

    public boolean verificarTurno(Jugador j) {
        return partida.getJugadorEnTurno().equals(j);
    }







    
    public Jugador obtenerOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? jugador2 : jugador1;
    }

    
    public TableroModel obtenerTableroOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? tableroModel2 : tableroModel1;
    }

    public ResultadoDisparo realizarDisparo(Jugador atacante, Coordenada coord) throws IllegalStateException {
        if (!verificarTurno(atacante)) {
            throw new IllegalStateException("No es tu turno");
        }

        TableroModel oponenteTablero = obtenerTableroOponente(atacante);

        if (!oponenteTablero.validarCoordenada(coord)) {
            throw new IllegalArgumentException("Coordenada inválida: " + coord);
        }

        ResultadoDisparo resultado = oponenteTablero.recibirDisparo(coord);
        Disparo disparo = new Disparo(resultado, coord);
        atacante.agregarDisparo(disparo);

        return resultado;
    }



    public boolean partidaFinalizada() {

        if (tableroModel1.todasNavesHundidas() || tableroModel2.todasNavesHundidas()) {
            
            return true;
        }else{
            return false;
        }
        
        
        
    }

    public Jugador getJugadorEnTurno() {
        return partida.getJugadorEnTurno();
    }

    public void setJugadorEnTurno(Jugador jugador) {
        partida.setJugadorEnTurno(jugador);
    }

    public Partida getPartida() {
        return partida;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public TableroModel getTableroModel1() {
        return tableroModel1;
    }

    public TableroModel getTableroModel2() {
        return tableroModel2;
    }


    public int getTiempoRestante() {
        return tiempoRestante;
    }

    
    public String getUltimoError() {
        String error = ultimoError;
        ultimoError = null; 
        return error;
    }


    public void setUltimoError(String mensaje) {
        this.ultimoError = mensaje;
        notifyObservers();
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }


    
    
    
    
    

}
