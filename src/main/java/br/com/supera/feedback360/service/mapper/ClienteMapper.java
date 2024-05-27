package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.Cliente;
import br.com.supera.feedback360.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
