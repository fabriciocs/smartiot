package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.Analytics;
import br.com.supera.feedback360.service.dto.AnalyticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Analytics} and its DTO {@link AnalyticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnalyticsMapper extends EntityMapper<AnalyticsDTO, Analytics> {}
