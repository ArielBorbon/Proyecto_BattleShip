package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class DisparoController {

    private final RealizarDisparoService realizarDisparoService;

    public DisparoController(RealizarDisparoService realizarDisparoService) {
        this.realizarDisparoService = realizarDisparoService;
    }

    public void disparar(int columna, int fila) {
        realizarDisparoService.disparar(columna, fila);
    }

}
