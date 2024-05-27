package br.com.supera.feedback360.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QuestionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("question_text", table, columnPrefix + "_question_text"));
        columns.add(Column.aliased("question_type", table, columnPrefix + "_question_type"));

        columns.add(Column.aliased("feedback_form_id", table, columnPrefix + "_feedback_form_id"));
        return columns;
    }
}
