/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.repository;

/**
 *
 * @author Cricri
 */
public interface IRepository<T> {

    T obtener();

    void guardar(T t);
    
    void eliminar();
}
