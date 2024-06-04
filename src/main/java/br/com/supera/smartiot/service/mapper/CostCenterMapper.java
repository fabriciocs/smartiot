package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.CostCenter;
import br.com.supera.smartiot.service.dto.CostCenterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CostCenter} and its DTO {@link CostCenterDTO}.
 */
@Mapper(componentModel = "spring")
public interface CostCenterMapper extends EntityMapper<CostCenterDTO, CostCenter> {}
