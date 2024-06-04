package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Report;
import br.com.supera.smartiot.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
