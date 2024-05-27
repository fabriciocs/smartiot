package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.SysUser;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SysUser}, with proper type conversions.
 */
@Service
public class SysUserRowMapper implements BiFunction<Row, String, SysUser> {

    private final ColumnConverter converter;

    public SysUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SysUser} stored in the database.
     */
    @Override
    public SysUser apply(Row row, String prefix) {
        SysUser entity = new SysUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPasswordHash(converter.fromRow(row, prefix + "_password_hash", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setRoleId(converter.fromRow(row, prefix + "_role_id", Long.class));
        return entity;
    }
}
