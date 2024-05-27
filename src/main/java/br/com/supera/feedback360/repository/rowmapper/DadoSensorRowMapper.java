package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.DadoSensor;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DadoSensor}, with proper type conversions.
 */
@Service
public class DadoSensorRowMapper implements BiFunction<Row, String, DadoSensor> {

    private final ColumnConverter converter;

    public DadoSensorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DadoSensor} stored in the database.
     */
    @Override
    public DadoSensor apply(Row row, String prefix) {
        DadoSensor entity = new DadoSensor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDados(converter.fromRow(row, prefix + "_dados", String.class));
        entity.setTimestamp(converter.fromRow(row, prefix + "_timestamp", Instant.class));
        return entity;
    }
}
