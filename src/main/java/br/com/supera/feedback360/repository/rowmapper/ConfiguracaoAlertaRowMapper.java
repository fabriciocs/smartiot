package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.ConfiguracaoAlerta;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ConfiguracaoAlerta}, with proper type conversions.
 */
@Service
public class ConfiguracaoAlertaRowMapper implements BiFunction<Row, String, ConfiguracaoAlerta> {

    private final ColumnConverter converter;

    public ConfiguracaoAlertaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ConfiguracaoAlerta} stored in the database.
     */
    @Override
    public ConfiguracaoAlerta apply(Row row, String prefix) {
        ConfiguracaoAlerta entity = new ConfiguracaoAlerta();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLimite(converter.fromRow(row, prefix + "_limite", BigDecimal.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setSensorId(converter.fromRow(row, prefix + "_sensor_id", Long.class));
        return entity;
    }
}
