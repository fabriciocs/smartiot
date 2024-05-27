package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.DadoSensor;
import br.com.supera.feedback360.service.dto.DadoSensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DadoSensor} and its DTO {@link DadoSensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DadoSensorMapper extends EntityMapper<DadoSensorDTO, DadoSensor> {}
