/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.mapper;

import mx.itson.equipo_2.dto.TableroDTO;
import mx.itson.equipo_2.models.entitys.Tablero;

/**
 *
 * @author skyro
 * @author sonic
 */
public class TableroMapper {
    
    public static Tablero toEntity(TableroDTO dto) {
        return new Tablero(
                CeldaMapper.toEntityMatrix(dto.getCeldas()),
                NaveMapper.toEntityList(dto.getNaves())
        );
    }
    
    public static TableroDTO toDTO(Tablero t) {
        return new TableroDTO(
                CeldaMapper.toDTOMatrix(t.getCeldas()), 
                NaveMapper.toDTOList(t.getNaves())
        );
    }
}
