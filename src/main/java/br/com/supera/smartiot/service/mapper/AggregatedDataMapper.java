package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.AggregatedData;
import br.com.supera.smartiot.service.dto.AggregatedDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AggregatedData} and its DTO {@link AggregatedDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface AggregatedDataMapper extends EntityMapper<AggregatedDataDTO, AggregatedData> {}
