package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Sensor;
import br.com.supera.feedback360.domain.enumeration.TipoSensor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Sensor}, with proper type conversions.
 */
@Service
public class SensorRowMapper implements BiFunction<Row, String, Sensor> {

    private final ColumnConverter converter;

    public SensorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sensor} stored in the database.
     */
    @Override
    public Sensor apply(Row row, String prefix) {
        Sensor entity = new Sensor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", TipoSensor.class));
        entity.setConfiguracao(converter.fromRow(row, prefix + "_configuracao", String.class));
        entity.setClienteId(converter.fromRow(row, prefix + "_cliente_id", Long.class));
        entity.setDadoSensoresId(converter.fromRow(row, prefix + "_dado_sensores_id", Long.class));
        return entity;
    }
}
