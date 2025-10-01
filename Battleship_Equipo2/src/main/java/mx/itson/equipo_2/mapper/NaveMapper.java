/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.dto.NaveDTO;
import mx.itson.equipo_2.models.entitys.Nave;

/**
 *
 * @author skyro
 * @author sonic
 */
public class NaveMapper {

    public static Nave toEntity(NaveDTO dto) {
        return new Nave(
                dto.getTipo(),
                dto.getEstado(),
                CoordenadaMapper.toEntityList(dto.getCoordenadas()),
                dto.getOrientacion()
        );
    }

    public static NaveDTO toDTO(Nave n) {
        return new NaveDTO(
                n.getTipo(),
                n.getEstado(),
                CoordenadaMapper.toDTOList(n.getCoordenadas()),
                n.getOrientacion()
        );
    }
    
    public static List<Nave> toEntityList(List<NaveDTO> dtoList) {
        return dtoList == null ? new ArrayList<>()
                : dtoList.stream().map(NaveMapper::toEntity).collect(Collectors.toList());
    }

    public static List<NaveDTO> toDTOList(List<Nave> entityList) {
        return entityList == null ? new ArrayList<>()
                : entityList.stream().map(NaveMapper::toDTO).collect(Collectors.toList());
    }
}
