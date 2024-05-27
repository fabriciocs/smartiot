package br.com.supera.feedback360.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ConfiguracaoAlertaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("limite", table, columnPrefix + "_limite"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));

        columns.add(Column.aliased("sensor_id", table, columnPrefix + "_sensor_id"));
        return columns;
    }
}
