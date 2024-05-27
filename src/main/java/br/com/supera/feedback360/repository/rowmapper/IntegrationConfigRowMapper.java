package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.IntegrationConfig;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IntegrationConfig}, with proper type conversions.
 */
@Service
public class IntegrationConfigRowMapper implements BiFunction<Row, String, IntegrationConfig> {

    private final ColumnConverter converter;

    public IntegrationConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IntegrationConfig} stored in the database.
     */
    @Override
    public IntegrationConfig apply(Row row, String prefix) {
        IntegrationConfig entity = new IntegrationConfig();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setServiceName(converter.fromRow(row, prefix + "_service_name", String.class));
        entity.setConfigData(converter.fromRow(row, prefix + "_config_data", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setExternalSystemId(converter.fromRow(row, prefix + "_external_system_id", Long.class));
        return entity;
    }
}
