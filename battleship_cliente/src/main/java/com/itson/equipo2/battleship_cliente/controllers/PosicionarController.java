/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.service.PosicionarNaveService;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class PosicionarController {

    private final PosicionarNaveService posicionarNaveService;
    private final PartidaModel partidaModel;

    public PosicionarController(PosicionarNaveService posicionarNaveService, PartidaModel partidaModel) {
        this.posicionarNaveService = posicionarNaveService;
        this.partidaModel = partidaModel;
    }

    public boolean intentarPosicionarNave(TipoNave tipo, int col, int fila, boolean esHorizontal) {

        return this.partidaModel.intentarPosicionarNavePropia(tipo, col, fila, esHorizontal);
    }
}
