package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.DadoSensor;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DadoSensor} and its DTO {@link DadoSensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DadoSensorMapper extends EntityMapper<DadoSensorDTO, DadoSensor> {}
