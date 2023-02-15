package com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.NullValue;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JdbcSqlExecutorHelper {


    @SneakyThrows
    public static List<String> getResultColumns(ResultSetMetaData metaData) {

        int count = metaData.getColumnCount();
        //获取到执行sql后返回的列信息
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            columns.add(metaData.getColumnLabel(i));
        }

        return columns;
    }


    protected static void preparedStatementParameter(PreparedStatement statement, Object[] parameter) throws SQLException {
        if (parameter == null || parameter.length == 0) {
            return;
        }
        int index = 1;
        //预编译参数
        for (Object object : parameter) {
            if (object == null) {
                statement.setNull(index++, Types.NULL);
            } else if (object instanceof NullValue) {
                statement.setNull(index++, ((NullValue) object).getDataType().getSqlType().getVendorTypeNumber());
            } else if (object instanceof Date) {
                statement.setTimestamp(index++, new Timestamp(((Date) object).getTime()));
            } else if (object instanceof byte[]) {
                statement.setBlob(index++, new ByteArrayInputStream((byte[]) object));
            } else
                statement.setObject(index++, object);
        }
    }


}
