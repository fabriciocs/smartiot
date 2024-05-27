package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Analytics;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Analytics}, with proper type conversions.
 */
@Service
public class AnalyticsRowMapper implements BiFunction<Row, String, Analytics> {

    private final ColumnConverter converter;

    public AnalyticsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Analytics} stored in the database.
     */
    @Override
    public Analytics apply(Row row, String prefix) {
        Analytics entity = new Analytics();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setData(converter.fromRow(row, prefix + "_data", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        return entity;
    }
}
