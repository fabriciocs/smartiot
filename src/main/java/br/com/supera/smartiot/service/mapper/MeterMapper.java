package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Meter;
import br.com.supera.smartiot.service.dto.MeterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Meter} and its DTO {@link MeterDTO}.
 */
@Mapper(componentModel = "spring")
public interface MeterMapper extends EntityMapper<MeterDTO, Meter> {}
