package br.com.supera.feedback360.repository.rowmapper;

import br.com.supera.feedback360.domain.Question;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Question}, with proper type conversions.
 */
@Service
public class QuestionRowMapper implements BiFunction<Row, String, Question> {

    private final ColumnConverter converter;

    public QuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Question} stored in the database.
     */
    @Override
    public Question apply(Row row, String prefix) {
        Question entity = new Question();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuestionText(converter.fromRow(row, prefix + "_question_text", String.class));
        entity.setQuestionType(converter.fromRow(row, prefix + "_question_type", String.class));
        entity.setFeedbackFormId(converter.fromRow(row, prefix + "_feedback_form_id", Long.class));
        return entity;
    }
}
