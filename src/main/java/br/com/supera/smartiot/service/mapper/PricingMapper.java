package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Pricing;
import br.com.supera.smartiot.service.dto.PricingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pricing} and its DTO {@link PricingDTO}.
 */
@Mapper(componentModel = "spring")
public interface PricingMapper extends EntityMapper<PricingDTO, Pricing> {}
