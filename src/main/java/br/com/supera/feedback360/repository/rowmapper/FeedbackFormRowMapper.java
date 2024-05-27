package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.FeedbackForm;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FeedbackForm}, with proper type conversions.
 */
@Service
public class FeedbackFormRowMapper implements BiFunction<Row, String, FeedbackForm> {

    private final ColumnConverter converter;

    public FeedbackFormRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FeedbackForm} stored in the database.
     */
    @Override
    public FeedbackForm apply(Row row, String prefix) {
        FeedbackForm entity = new FeedbackForm();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setCreatorId(converter.fromRow(row, prefix + "_creator_id", Long.class));
        return entity;
    }
}
