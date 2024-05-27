package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.FeedbackResponse;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FeedbackResponse}, with proper type conversions.
 */
@Service
public class FeedbackResponseRowMapper implements BiFunction<Row, String, FeedbackResponse> {

    private final ColumnConverter converter;

    public FeedbackResponseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FeedbackResponse} stored in the database.
     */
    @Override
    public FeedbackResponse apply(Row row, String prefix) {
        FeedbackResponse entity = new FeedbackResponse();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setResponseData(converter.fromRow(row, prefix + "_response_data", String.class));
        entity.setSubmittedAt(converter.fromRow(row, prefix + "_submitted_at", Instant.class));
        entity.setFormId(converter.fromRow(row, prefix + "_form_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
