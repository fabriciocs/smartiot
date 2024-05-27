package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.NotificationSettings;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link NotificationSettings}, with proper type conversions.
 */
@Service
public class NotificationSettingsRowMapper implements BiFunction<Row, String, NotificationSettings> {

    private final ColumnConverter converter;

    public NotificationSettingsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link NotificationSettings} stored in the database.
     */
    @Override
    public NotificationSettings apply(Row row, String prefix) {
        NotificationSettings entity = new NotificationSettings();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPreferences(converter.fromRow(row, prefix + "_preferences", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
