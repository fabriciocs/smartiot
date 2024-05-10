package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.Cliente;
import br.com.supera.smartiot.domain.DadoSensor;
import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.service.dto.ClienteDTO;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import br.com.supera.smartiot.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sensor} and its DTO {@link SensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteNome")
    @Mapping(target = "dadoSensores", source = "dadoSensores", qualifiedByName = "dadoSensorTimestamp")
    SensorDTO toDto(Sensor s);

    @Named("clienteNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ClienteDTO toDtoClienteNome(Cliente cliente);

    @Named("dadoSensorTimestamp")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "timestamp", source = "timestamp")
    DadoSensorDTO toDtoDadoSensorTimestamp(DadoSensor dadoSensor);
}
