package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Repeater;
import br.com.supera.smartiot.service.dto.RepeaterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Repeater} and its DTO {@link RepeaterDTO}.
 */
@Mapper(componentModel = "spring")
public interface RepeaterMapper extends EntityMapper<RepeaterDTO, Repeater> {}
