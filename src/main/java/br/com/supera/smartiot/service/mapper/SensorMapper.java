package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import br.com.supera.smartiot.domain.DadoSensor;
import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import br.com.supera.smartiot.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sensor} and its DTO {@link SensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {
    @Mapping(target = "configuracaoAlertas", source = "configuracaoAlertas", qualifiedByName = "configuracaoAlertaEmail")
    @Mapping(target = "dadoSensores", source = "dadoSensores", qualifiedByName = "dadoSensorTimestamp")
    SensorDTO toDto(Sensor s);

    @Named("configuracaoAlertaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ConfiguracaoAlertaDTO toDtoConfiguracaoAlertaEmail(ConfiguracaoAlerta configuracaoAlerta);

    @Named("dadoSensorTimestamp")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "timestamp", source = "timestamp")
    DadoSensorDTO toDtoDadoSensorTimestamp(DadoSensor dadoSensor);
}
