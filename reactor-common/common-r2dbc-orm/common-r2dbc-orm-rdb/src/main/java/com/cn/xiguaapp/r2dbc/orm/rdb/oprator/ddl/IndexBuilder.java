package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;

/**
 * @author xiguaapp
 * @since 1.0.0
 */
public class IndexBuilder {
    private TableBuilder tableBuilder;

    public IndexBuilder(TableBuilder tableBuilder, RDBTableMetadata table) {
        this.tableBuilder = tableBuilder;
        this.table = table;
    }

    private RDBTableMetadata table;

    private RDBIndexMetadata index = new RDBIndexMetadata();

    public IndexBuilder name(String indexName) {
        index.setName(indexName);
        return this;
    }

    public IndexBuilder unique() {
        index.setUnique(true);
        return this;
    }

    public IndexBuilder column(String column) {
        return column(column, RDBIndexMetadata.IndexSort.asc);
    }

    public IndexBuilder column(String column, String sort) {
        return column(column, RDBIndexMetadata.IndexSort.valueOf(sort));
    }

    public IndexBuilder column(String column, RDBIndexMetadata.IndexSort sort) {
        index.getColumns().add(RDBIndexMetadata.IndexColumn.of(column, sort));
        return this;
    }

    public TableBuilder commit() {
        table.addIndex(index);
        return tableBuilder;
    }

}
