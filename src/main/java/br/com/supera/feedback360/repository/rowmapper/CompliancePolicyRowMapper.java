package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.CompliancePolicy;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CompliancePolicy}, with proper type conversions.
 */
@Service
public class CompliancePolicyRowMapper implements BiFunction<Row, String, CompliancePolicy> {

    private final ColumnConverter converter;

    public CompliancePolicyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CompliancePolicy} stored in the database.
     */
    @Override
    public CompliancePolicy apply(Row row, String prefix) {
        CompliancePolicy entity = new CompliancePolicy();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setRules(converter.fromRow(row, prefix + "_rules", String.class));
        return entity;
    }
}
