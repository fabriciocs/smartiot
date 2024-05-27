package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Cliente;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cliente}, with proper type conversions.
 */
@Service
public class ClienteRowMapper implements BiFunction<Row, String, Cliente> {

    private final ColumnConverter converter;

    public ClienteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cliente} stored in the database.
     */
    @Override
    public Cliente apply(Row row, String prefix) {
        Cliente entity = new Cliente();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        return entity;
    }
}
