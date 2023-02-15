package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 */
@NoArgsConstructor
public class DefaultBatchSqlRequest extends PrepareSqlRequest implements BatchSqlRequest {

    public static DefaultBatchSqlRequest of(String sql, Object... parameter) {
        DefaultBatchSqlRequest sqlRequest = new DefaultBatchSqlRequest();
        sqlRequest.setSql(sql);
        sqlRequest.setParameters(parameter);
        return sqlRequest;
    }

    @Getter
    @Setter
    private List<SqlRequest> batch = new ArrayList<>();

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && batch.isEmpty();
    }

    public synchronized DefaultBatchSqlRequest addBatch(SqlRequest sqlRequest) {
        if (this.getSql() == null) {
            this.setSql(sqlRequest.getSql());
            this.setParameters(sqlRequest.getParameters());
            return this;
        }
        batch.add(sqlRequest);
        return this;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        if (isNotEmpty()) {
            builder.append(super.toString());
        }
        for (SqlRequest request : batch) {
            if (request.isNotEmpty()) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(request.toString());
            }

        }
        return builder.toString();
    }
}
