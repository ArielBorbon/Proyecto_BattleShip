/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.mapper;

import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.models.entitys.Coordenada;

/**
 *
 * @author skyro
 * @author sonic
 */
public class CoordenadaMapper {

    public static Coordenada toEntity(CoordenadaDTO dto) {
        return new Coordenada(dto.getFila(), dto.getColumna());
    }

    public static CoordenadaDTO toDTO(Coordenada c) {
        return new CoordenadaDTO(c.getFila(), c.getColumna());
    }

    public static List<Coordenada> toEntityList(List<CoordenadaDTO> dL) {
        return dL.stream()
                .map(CoordenadaMapper::toEntity)
                .collect(Collectors.toList());
    }
    
    public static List<CoordenadaDTO> toDTOList(List<Coordenada> cL) {
        return cL.stream()
                .map(CoordenadaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
