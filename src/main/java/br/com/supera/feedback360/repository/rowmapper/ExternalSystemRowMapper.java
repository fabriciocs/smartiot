package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.ExternalSystem;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ExternalSystem}, with proper type conversions.
 */
@Service
public class ExternalSystemRowMapper implements BiFunction<Row, String, ExternalSystem> {

    private final ColumnConverter converter;

    public ExternalSystemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ExternalSystem} stored in the database.
     */
    @Override
    public ExternalSystem apply(Row row, String prefix) {
        ExternalSystem entity = new ExternalSystem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setApiEndpoint(converter.fromRow(row, prefix + "_api_endpoint", String.class));
        entity.setAuthDetails(converter.fromRow(row, prefix + "_auth_details", String.class));
        return entity;
    }
}
