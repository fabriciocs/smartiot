package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Concentrator;
import br.com.supera.smartiot.service.dto.ConcentratorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Concentrator} and its DTO {@link ConcentratorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConcentratorMapper extends EntityMapper<ConcentratorDTO, Concentrator> {}
