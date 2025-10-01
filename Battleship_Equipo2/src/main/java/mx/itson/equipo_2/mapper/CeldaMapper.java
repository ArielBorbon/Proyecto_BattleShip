/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.mapper;

import mx.itson.equipo_2.dto.CeldaDTO;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.models.entitys.Celda;
import mx.itson.equipo_2.models.entitys.Coordenada;

/**
 *
 * @author sonic
 */
public class CeldaMapper {
    
    public static Celda toEntity(CeldaDTO dto) {
        return new Celda(
                dto.getEstado(),
                new Coordenada(dto.getCoordenada().getFila(), dto.getCoordenada().getColumna()),
                NaveMapper.toEntity(dto.getNave())
        );
    }
    
    public static CeldaDTO toDTO(Celda c) {
        return new CeldaDTO(
                c.getEstado(),
                new CoordenadaDTO(c.getCoordenada().getFila(), c.getCoordenada().getColumna()),
                NaveMapper.toDTO(c.getNave())
                
        );
    }
    
    public static Celda[][] toEntityMatrix(CeldaDTO[][] dtoMatrix) {
        int filas = dtoMatrix.length;
        int columnas = dtoMatrix[0].length;
        Celda[][] result = new Celda[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                result[i][j] = toEntity(dtoMatrix[i][j]);
            }
        }
        return result;
    }

    // Convierte una matriz de entidad a DTO
    public static CeldaDTO[][] toDTOMatrix(Celda[][] entityMatrix) {
        int filas = entityMatrix.length;
        int columnas = entityMatrix[0].length;
        CeldaDTO[][] result = new CeldaDTO[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                result[i][j] = toDTO(entityMatrix[i][j]);
            }
        }
        return result;
    }
}
