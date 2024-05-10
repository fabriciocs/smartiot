package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Cliente;
import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.service.dto.ClienteDTO;
import br.com.supera.smartiot.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "sensores", source = "sensores", qualifiedByName = "sensorNome")
    ClienteDTO toDto(Cliente s);

    @Named("sensorNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    SensorDTO toDtoSensorNome(Sensor sensor);
}
