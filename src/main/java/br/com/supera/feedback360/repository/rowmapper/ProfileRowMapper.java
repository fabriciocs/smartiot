package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Profile;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Profile}, with proper type conversions.
 */
@Service
public class ProfileRowMapper implements BiFunction<Row, String, Profile> {

    private final ColumnConverter converter;

    public ProfileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Profile} stored in the database.
     */
    @Override
    public Profile apply(Row row, String prefix) {
        Profile entity = new Profile();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPhoneNumber(converter.fromRow(row, prefix + "_phone_number", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setProfilePicture(converter.fromRow(row, prefix + "_profile_picture", String.class));
        entity.setPreferences(converter.fromRow(row, prefix + "_preferences", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
