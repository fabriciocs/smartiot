package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Transmitter;
import br.com.supera.smartiot.service.dto.TransmitterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transmitter} and its DTO {@link TransmitterDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransmitterMapper extends EntityMapper<TransmitterDTO, Transmitter> {}
