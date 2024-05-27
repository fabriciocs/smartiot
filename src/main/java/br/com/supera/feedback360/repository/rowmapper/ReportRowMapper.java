package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Report;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Report}, with proper type conversions.
 */
@Service
public class ReportRowMapper implements BiFunction<Row, String, Report> {

    private final ColumnConverter converter;

    public ReportRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Report} stored in the database.
     */
    @Override
    public Report apply(Row row, String prefix) {
        Report entity = new Report();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setGeneratedAt(converter.fromRow(row, prefix + "_generated_at", Instant.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setGeneratedById(converter.fromRow(row, prefix + "_generated_by_id", Long.class));
        return entity;
    }
}
