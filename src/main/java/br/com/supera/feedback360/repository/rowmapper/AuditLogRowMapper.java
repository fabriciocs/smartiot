package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.AuditLog;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AuditLog}, with proper type conversions.
 */
@Service
public class AuditLogRowMapper implements BiFunction<Row, String, AuditLog> {

    private final ColumnConverter converter;

    public AuditLogRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AuditLog} stored in the database.
     */
    @Override
    public AuditLog apply(Row row, String prefix) {
        AuditLog entity = new AuditLog();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAction(converter.fromRow(row, prefix + "_action", String.class));
        entity.setTimestamp(converter.fromRow(row, prefix + "_timestamp", Instant.class));
        entity.setDetails(converter.fromRow(row, prefix + "_details", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
