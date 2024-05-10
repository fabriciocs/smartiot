package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import br.com.supera.smartiot.service.dto.ConfiguracaoAlertaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfiguracaoAlerta} and its DTO {@link ConfiguracaoAlertaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfiguracaoAlertaMapper extends EntityMapper<ConfiguracaoAlertaDTO, ConfiguracaoAlerta> {}
