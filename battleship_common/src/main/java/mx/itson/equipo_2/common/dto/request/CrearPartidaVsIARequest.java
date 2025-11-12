/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;
import java.util.List;
import mx.itson.equipo_2.common.dto.NaveDTO;

/**
 *
 * @author PC Gamer
 */
public class CrearPartidaVsIARequest implements Serializable {
    private String jugadorHumanoId;
    private List<NaveDTO> navesHumano;
    private List<NaveDTO> navesIA;

    public CrearPartidaVsIARequest(String jugadorHumanoId, List<NaveDTO> navesHumano, List<NaveDTO> navesIA) {
        this.jugadorHumanoId = jugadorHumanoId;
        this.navesHumano = navesHumano;
        this.navesIA = navesIA;
    }

    public String getJugadorHumanoId() {
        return jugadorHumanoId;
    }

    public void setJugadorHumanoId(String jugadorHumanoId) {
        this.jugadorHumanoId = jugadorHumanoId;
    }

    public List<NaveDTO> getNavesHumano() {
        return navesHumano;
    }

    public void setNavesHumano(List<NaveDTO> navesHumano) {
        this.navesHumano = navesHumano;
    }

    public List<NaveDTO> getNavesIA() {
        return navesIA;
    }

    public void setNavesIA(List<NaveDTO> navesIA) {
        this.navesIA = navesIA;
    }
    



}
