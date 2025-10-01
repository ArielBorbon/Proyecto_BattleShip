
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
    
    public static Celda[][] toEntityMatrix(CeldaDTO[][] matrizDto) {
        int filas = matrizDto.length;
        int columnas = matrizDto[0].length;
        Celda[][] result = new Celda[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                result[i][j] = toEntity(matrizDto[i][j]);
            }
        }
        return result;
    }

    public static CeldaDTO[][] toDTOMatrix(Celda[][] matrizEntidad) {
        int filas = matrizEntidad.length;
        int columnas = matrizEntidad[0].length;
        CeldaDTO[][] result = new CeldaDTO[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                result[i][j] = toDTO(matrizEntidad[i][j]);
            }
        }
        return result;
    }
}
