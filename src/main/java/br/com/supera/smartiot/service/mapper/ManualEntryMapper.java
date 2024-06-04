package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ManualEntry;
import br.com.supera.smartiot.service.dto.ManualEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManualEntry} and its DTO {@link ManualEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualEntryMapper extends EntityMapper<ManualEntryDTO, ManualEntry> {}
