package br.com.supera.feedback360.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReportSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("generated_at", table, columnPrefix + "_generated_at"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));

        columns.add(Column.aliased("generated_by_id", table, columnPrefix + "_generated_by_id"));
        return columns;
    }
}
