package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Alert;
import br.com.supera.smartiot.domain.Consumer;
import br.com.supera.smartiot.service.dto.AlertDTO;
import br.com.supera.smartiot.service.dto.ConsumerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {
    @Mapping(target = "consumer", source = "consumer", qualifiedByName = "consumerId")
    AlertDTO toDto(Alert s);

    @Named("consumerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsumerDTO toDtoConsumerId(Consumer consumer);
}
