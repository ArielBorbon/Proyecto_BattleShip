/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import com.itson.equipo2.battleship_cliente.exceptions.PosicionarNaveException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.PartidaDTO;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.enums.EstadoJugador;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;
import mx.itson.equipo_2.common.enums.TipoNave;
import com.itson.equipo2.battleship_cliente.pattern.observer.IObserver;
import com.itson.equipo2.battleship_cliente.pattern.observer.ISubject;
import java.util.UUID;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 *
 * @author skyro
 */
public class PartidaModel implements ISubject<PartidaModel> {

    private String id;
    private JugadorModel yo;
    private JugadorModel enemigo;
    private boolean enCurso;
    private String turnoDe; // id del jugador con turno actual
    private Integer segundosRestantes;
    private EstadoPartida estado;
    private String idJugador1; //ID del Host
    private String ipServidor;
    private String nombreJugador1;
    private String nombreJugador2;

    private boolean soyHost = false;

    private Map<TipoNave, List<EstadoNave>> navesEnemigas;

    private final transient List<IObserver<PartidaModel>> observers = new ArrayList<>();

    public PartidaModel() {
        this.navesEnemigas = new HashMap<>();
        inicializarNavesEnemigas();
    }

    public PartidaModel(String id, JugadorModel yo, JugadorModel enemigo, boolean enCurso, String turnoDe, Integer segundosRestantes, EstadoPartida estado) {
        this.id = id;
        this.yo = yo;
        this.enemigo = enemigo;
        this.enCurso = enCurso;
        this.turnoDe = turnoDe;
        this.segundosRestantes = segundosRestantes;
        this.estado = estado;

        this.navesEnemigas = new HashMap<>();
        inicializarNavesEnemigas();

    }

    @Override
    public void addObserver(IObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(PartidaModel model) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (IObserver<PartidaModel> observer : observers) {
                observer.onChange(model);
            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JugadorModel getYo() {
        return yo;
    }

    public void setYo(JugadorModel yo) {
        this.yo = yo;
    }

    public JugadorModel getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(JugadorModel enemigo) {
        this.enemigo = enemigo;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public String getTurnoDe() {
        return turnoDe;
    }

    public void setTurnoDe(String turnoDe) {
        this.turnoDe = turnoDe;
    }

    public Integer getSegundosRestantes() {
        return segundosRestantes;
    }

    public void setSegundosRestantes(Integer segundosRestantes) {
        this.segundosRestantes = segundosRestantes;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public String getIdJugador1() {
        return idJugador1;
    }

    public void setIdJugador1(String idJugador1) {
        this.idJugador1 = idJugador1;
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }
    
    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public void setNombreJugador1(String nombreJugador1) {
        this.nombreJugador1 = nombreJugador1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public void setNombreJugador2(String nombreJugador2) {
        this.nombreJugador2 = nombreJugador2;
    }

    public Map<TipoNave, List<EstadoNave>> getNavesEnemigas() {
        return navesEnemigas;
    }

    public void setNavesEnemigas(Map<TipoNave, List<EstadoNave>> navesEnemigas) {
        this.navesEnemigas = navesEnemigas;
    }

    public void setSoyHost(boolean soyHost) {
        this.soyHost = soyHost;
    }

    public Map<TipoNave, List<EstadoNave>> getEstadoMisNaves() {
        if (this.getTableroPropio() != null) {
            return this.getTableroPropio().calcularEstadoNaves();
        }
        return Collections.emptyMap();
    }

    public Map<TipoNave, List<EstadoNave>> getEstadoNavesEnemigas() {
        return this.navesEnemigas;
    }

    public TableroModel getTableroPropio() {
        if (this.yo.getTablero() == null) {
            this.yo.setTablero(new TableroModel(this.yo.getId()));
        }
        return this.yo.getTablero();
    }

    @Override
    public String toString() {
        return "PartidaModel{" + "id=" + id + ", yo=" + yo + ", enemigo=" + enemigo + ", enCurso=" + enCurso + ", turnoDe=" + turnoDe + ", segundosRestantes=" + segundosRestantes + ", estado=" + estado + ", idJugador1=" + idJugador1 + ", nombreJugador1=" + nombreJugador1 + ", nombreJugador2=" + nombreJugador2 + ", soyHost=" + soyHost + ", navesEnemigas=" + navesEnemigas + ", observers=" + observers + '}';
    }

    public String getNombreJugadorEnTurno() {
        return obtenerNombrePorId(this.turnoDe);
    }

    public void sincronizarDatosSala(PartidaDTO dto) {
        this.estado = dto.getEstado();

        if (dto.getJugador1() != null) {
            this.nombreJugador1 = dto.getJugador1().getNombre();
            this.idJugador1 = dto.getJugador1().getId();
        }

        if (dto.getJugador2() != null) {
            this.nombreJugador2 = dto.getJugador2().getNombre();
            if (isSoyHost()) {
                actualizarEnemigo(dto.getJugador2());
            }
        } else {
            this.nombreJugador2 = "Esperando...";
            this.enemigo = null;
        }

        if (this.yo != null && this.yo.getId() != null && this.idJugador1 != null) {
            if (this.yo.getId().equals(this.idJugador1)) {
                if (dto.getJugador2() != null) {
                    actualizarEnemigo(dto.getJugador2());
                }
            } else if (dto.getJugador2() != null && this.yo.getId().equals(dto.getJugador2().getId())) {
                actualizarEnemigo(dto.getJugador1());
            }
        }
        notifyObservers(this);
    }

    private void actualizarEnemigo(JugadorDTO enemigoDTO) {
        if (this.enemigo == null) {
            this.enemigo = new JugadorModel();
        }
        this.enemigo.setId(enemigoDTO.getId());
        this.enemigo.setNombre(enemigoDTO.getNombre());
        this.enemigo.setColor(enemigoDTO.getColor());
        this.enemigo.setEstado(EstadoJugador.EN_BATALLA); // Ya estamos jugando

        // IMPORTANTE: Asegurar que el enemigo tenga un tablero (vacío/oculto)
        if (this.enemigo.getTablero() == null) {
            this.enemigo.setTablero(new TableroModel(enemigoDTO.getId()));
        }
    }

    public void procesarResultadoDisparo(ResultadoDisparoReponse response) {
        TableroModel tableroAfectado;

        boolean fuiYo = response.getJugadorId().equals(this.getYo().getId());

        if (fuiYo) {
            if (this.getEnemigo() != null) {
                tableroAfectado = this.getEnemigo().getTablero();
            } else {
                System.err.println("Error Crítico: Enemigo es NULL al procesar disparo.");
                return;
            }

            // Actualizar Marcador si hubo hundimiento
            if (response.getResultado() == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO) {
                int tamanioNave = response.getCoordenadasBarcoHundido().size();
                registrarHundimientoEnemigo(tamanioNave);
            }

        } else {
            tableroAfectado = this.getYo().getTablero();
        }

        if (tableroAfectado != null) {
            tableroAfectado.actualizarCelda(
                    response.getCoordenada(),
                    response.getResultado(),
                    response.getCoordenadasBarcoHundido()
            );
        }

        this.setTurnoDe(response.getTurnoActual());
        this.setEstado(response.getEstadoPartida());

        notifyObservers(this);
    }

    public void iniciarPartida(PartidaIniciadaResponse response) {
        this.setId(response.getPartidaId());

        JugadorDTO yoDTO = response.getYo(getYo().getId());
        if (yoDTO != null) {
            this.getYo().setColor(yoDTO.getColor());
            this.getYo().setNombre(yoDTO.getNombre());
        }

        JugadorDTO enemigoDTO = response.getEnemigo(getYo().getId());
        if (enemigoDTO != null) {
            actualizarEnemigo(enemigoDTO);
        }

        inicializarNavesEnemigas();

        this.setTurnoDe(response.getTurnoActual());
        this.setEstado(response.getEstado());

        notifyObservers(this);
    }

    public void actualizarTick(TurnoTickResponse response) {

        this.turnoDe = response.getJugadorEnTurnoId();
        this.segundosRestantes = response.getTiempoRestante();	//asd
        this.estado = response.getEstadoPartida();
        this.notifyObservers(this);
    }

    public boolean isSoyHost() {
        return yo != null && yo.getId() != null && idJugador1 != null && yo.getId().equals(idJugador1);
    }

    public void intentarPosicionarNavePropia(TipoNave tipo, int fila, int col, boolean esHorizontal) throws PosicionarNaveException {

        TableroModel tableroPropio = this.getTableroPropio();
        tableroPropio.agregarNave(tipo, fila, col, esHorizontal); //

        notifyObservers(this);

    }

    private void inicializarNavesEnemigas() {
        navesEnemigas.clear();
        for (TipoNave tipo : TipoNave.values()) {
            List<EstadoNave> listaEstados = new ArrayList<>();
            for (int i = 0; i < tipo.getCantidadInicial(); i++) {
                listaEstados.add(EstadoNave.SIN_DANIOS);
            }
            navesEnemigas.put(tipo, listaEstados);
        }
    }

    private void registrarHundimientoEnemigo(int tamanioBarco) {
        TipoNave tipoHundido = null;

        // Buscamos qué TipoNave corresponde a ese tamaño
        for (TipoNave tipo : TipoNave.values()) {
            if (tipo.getTamanio() == tamanioBarco) {
                tipoHundido = tipo;
                break;
            }
        }

        if (tipoHundido != null && navesEnemigas.containsKey(tipoHundido)) {
            List<EstadoNave> estados = navesEnemigas.get(tipoHundido);
            for (int i = 0; i < estados.size(); i++) {
                if (estados.get(i) != EstadoNave.HUNDIDO) {
                    estados.set(i, EstadoNave.HUNDIDO);
                    System.out.println("¡Marcador actualizado! Nave enemiga hundida: " + tipoHundido);
                    break;
                }
            }
        }
    }

    public void reiniciarPartida() {
        this.estado = EstadoPartida.CONFIGURACION;
        this.id = null;
        this.idJugador1 = null;
        this.turnoDe = null;
        this.nombreJugador2 = "Esperando...";
        this.segundosRestantes = 30;

        if (this.yo != null) {
            this.yo.setTablero(new TableroModel(this.yo.getId()));
            this.yo.setEstado(EstadoJugador.POSICIONANDO);
            this.yo.setDisparos(new ArrayList<>());
        }

        this.enemigo = null;

        this.navesEnemigas.clear();
        inicializarNavesEnemigas();

        notifyObservers(this);
    }

    public String obtenerNombrePorId(String idBusqueda) {
        if (idBusqueda == null) {
            return "Desconocido";
        }

        // 1. Verificar si soy YO
        if (this.yo != null && this.yo.getId() != null && this.yo.getId().equals(idBusqueda)) {
            return this.yo.getNombre() != null ? this.yo.getNombre() : "Tú";
        }

        // 2. Verificar si es el ENEMIGO
        if (this.enemigo != null && this.enemigo.getId() != null && this.enemigo.getId().equals(idBusqueda)) {
            return this.enemigo.getNombre() != null ? this.enemigo.getNombre() : "Enemigo";
        }

        return "Jugador Desconocido";
    }
    
    
     public void registrarJugador(String nombre, ColorJugador color) {
        System.out.println("RegistroLocal: Guardando datos...");

        JugadorModel yo = getYo();
        if (yo == null) {
            yo = new JugadorModel();
            setYo(yo);
        }
        yo.setId(UUID.randomUUID().toString());
        yo.setNombre(nombre);
        yo.setColor(color);

        
    }

}
