package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.ExternalSystem;
import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExternalSystem} and its DTO {@link ExternalSystemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalSystemMapper extends EntityMapper<ExternalSystemDTO, ExternalSystem> {}
