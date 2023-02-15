package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.oracle;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.JDBCType;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests.prepare;


public class OracleTableMetaParserTest {

    private SyncSqlExecutor executor;

    private OracleTableMetadataParser parser;

    @Before
    public void init() {
        executor = new TestSyncSqlExecutor(new OracleConnectionProvider());
        OracleSchemaMetadata schema = new OracleSchemaMetadata("SYSTEM");
        schema.addFeature(executor);

        parser = new OracleTableMetadataParser(schema);
    }

    @Test
    public void testParse() {

        try {
            executor.execute(SqlRequests.of("CREATE TABLE test_table(" +
                    "id varchar2(32) primary key," +
                    "name varchar2(128) not null," +
                    "age number(10)" +
                    ")"));
            RDBTableMetadata metaData = parser.parseByName("test_table").orElseThrow(NullPointerException::new);

            //id
            {
                RDBColumnMetadata column = metaData.getColumn("id").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar2(32)");
                Assert.assertEquals(column.getType().getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
                // 这里只解析表结构，而不会解析键信息.
                // Assert.assertTrue(column.isPrimaryKey());
            }

            //name
            {
                RDBColumnMetadata column = metaData.getColumn("name").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar2(128)");
                Assert.assertEquals(column.getLength(), 128);
                Assert.assertEquals(column.getType().getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
            }

            //age
            {
                RDBColumnMetadata column = metaData.getColumn("age").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);
                Assert.assertEquals(column.getPrecision(), 10);
                Assert.assertEquals(column.getScale(), 0);
                Assert.assertEquals(column.getDataType(), "number(10,0)");
                Assert.assertEquals(column.getType().getSqlType(), JDBCType.NUMERIC);
                Assert.assertEquals(column.getJavaType(), BigDecimal.class);
            }
        } finally {
            executor.execute(prepare("drop table test_table"));
        }

    }


}