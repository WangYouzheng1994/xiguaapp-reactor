package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.JDBCType;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests.prepare;


public class MysqlTableMetaParserTest {

    private SyncSqlExecutor executor;

    private MysqlTableMetadataParser parser;

    @Before
    public void init() {
        RDBSchemaMetadata schema = new MysqlSchemaMetadata("demo");

        executor = new TestSyncSqlExecutor(new Mysql57ConnectionProvider());
        schema.addFeature(executor);
        parser = new MysqlTableMetadataParser(schema);
    }

    @Test
    public void testParse() {
        executor.execute(SqlRequests.of("CREATE TABLE IF NOT EXISTS test_table(" +
                "id varchar(32) primary key," +
                "name varchar(128) not null," +
                "age int" +
                ")"));
        try {
            RDBTableMetadata metaData = parser.parseByName("test_table").orElseThrow(NullPointerException::new);

            //id
            {
                RDBColumnMetadata column = metaData.getColumn("id").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar(32)");
                Assert.assertEquals(column.getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
                // 这里只解析表结构，而不会解析键信息.
                // Assert.assertTrue(column.isPrimaryKey());
            }

            //name
            {
                RDBColumnMetadata column = metaData.getColumn("name").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar(128)");
                Assert.assertEquals(column.getLength(), 128);
                Assert.assertEquals(column.getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
            }

            //age
            {
                RDBColumnMetadata column = metaData.getColumn("age").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);
                Assert.assertEquals(column.getPrecision(), 10);
                Assert.assertEquals(column.getScale(), 0);
                Assert.assertEquals(column.getDataType(), "int");
                Assert.assertEquals(column.getSqlType(), JDBCType.INTEGER);
                Assert.assertEquals(column.getJavaType(), Integer.class);
            }
        } finally {
            executor.execute(prepare("drop table test_table;"));
        }

    }


}