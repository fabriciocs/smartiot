package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Notification;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Notification}, with proper type conversions.
 */
@Service
public class NotificationRowMapper implements BiFunction<Row, String, Notification> {

    private final ColumnConverter converter;

    public NotificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Notification} stored in the database.
     */
    @Override
    public Notification apply(Row row, String prefix) {
        Notification entity = new Notification();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setMessage(converter.fromRow(row, prefix + "_message", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setRecipientId(converter.fromRow(row, prefix + "_recipient_id", Long.class));
        return entity;
    }
}
