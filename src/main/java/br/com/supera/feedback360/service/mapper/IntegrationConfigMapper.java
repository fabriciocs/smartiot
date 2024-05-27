package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.ExternalSystem;
import br.com.supera.feedback360.domain.IntegrationConfig;
import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
import br.com.supera.feedback360.service.dto.IntegrationConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntegrationConfig} and its DTO {@link IntegrationConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegrationConfigMapper extends EntityMapper<IntegrationConfigDTO, IntegrationConfig> {
    @Mapping(target = "externalSystem", source = "externalSystem", qualifiedByName = "externalSystemName")
    IntegrationConfigDTO toDto(IntegrationConfig s);

    @Named("externalSystemName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ExternalSystemDTO toDtoExternalSystemName(ExternalSystem externalSystem);
}
