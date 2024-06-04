package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Consumer;
import br.com.supera.smartiot.service.dto.ConsumerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consumer} and its DTO {@link ConsumerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsumerMapper extends EntityMapper<ConsumerDTO, Consumer> {}
