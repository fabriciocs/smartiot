package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Cliente;
import br.com.supera.smartiot.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
