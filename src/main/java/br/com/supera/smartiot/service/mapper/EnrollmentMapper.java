package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Enrollment;
import br.com.supera.smartiot.service.dto.EnrollmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enrollment} and its DTO {@link EnrollmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnrollmentMapper extends EntityMapper<EnrollmentDTO, Enrollment> {}
