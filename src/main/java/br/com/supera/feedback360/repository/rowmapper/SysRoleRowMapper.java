package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.SysRole;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SysRole}, with proper type conversions.
 */
@Service
public class SysRoleRowMapper implements BiFunction<Row, String, SysRole> {

    private final ColumnConverter converter;

    public SysRoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SysRole} stored in the database.
     */
    @Override
    public SysRole apply(Row row, String prefix) {
        SysRole entity = new SysRole();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRoleName(converter.fromRow(row, prefix + "_role_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
