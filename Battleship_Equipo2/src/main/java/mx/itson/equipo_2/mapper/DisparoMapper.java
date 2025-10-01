
package mx.itson.equipo_2.mapper;

import mx.itson.equipo_2.dto.DisparoDTO;
import mx.itson.equipo_2.models.entitys.Disparo;

/**
 *
 * @author skyro
 */
public class DisparoMapper {
    
    public static DisparoDTO toDto(Disparo disparo) {
        return new DisparoDTO(disparo.getResultado(), CoordenadaMapper.toDTO(disparo.getCoordenada()));
    }
}
