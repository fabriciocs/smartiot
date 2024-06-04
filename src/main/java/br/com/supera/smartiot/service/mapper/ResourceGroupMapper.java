package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ResourceGroup;
import br.com.supera.smartiot.service.dto.ResourceGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceGroup} and its DTO {@link ResourceGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceGroupMapper extends EntityMapper<ResourceGroupDTO, ResourceGroup> {}
