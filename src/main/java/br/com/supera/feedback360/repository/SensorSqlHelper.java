package br.com.supera.feedback360.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SensorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nome", table, columnPrefix + "_nome"));
        columns.add(Column.aliased("tipo", table, columnPrefix + "_tipo"));
        columns.add(Column.aliased("configuracao", table, columnPrefix + "_configuracao"));

        columns.add(Column.aliased("cliente_id", table, columnPrefix + "_cliente_id"));
        columns.add(Column.aliased("dado_sensores_id", table, columnPrefix + "_dado_sensores_id"));
        return columns;
    }
}
