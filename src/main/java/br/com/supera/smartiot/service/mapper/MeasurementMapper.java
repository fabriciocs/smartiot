package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Enrollment;
import br.com.supera.smartiot.domain.Measurement;
import br.com.supera.smartiot.service.dto.EnrollmentDTO;
import br.com.supera.smartiot.service.dto.MeasurementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Measurement} and its DTO {@link MeasurementDTO}.
 */
@Mapper(componentModel = "spring")
public interface MeasurementMapper extends EntityMapper<MeasurementDTO, Measurement> {
    @Mapping(target = "enrollment", source = "enrollment", qualifiedByName = "enrollmentId")
    MeasurementDTO toDto(Measurement s);

    @Named("enrollmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnrollmentDTO toDtoEnrollmentId(Enrollment enrollment);
}
