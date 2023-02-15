package com.cn.xiguaapp.common.r2dbc.test.param;

import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import org.junit.Assert;
import org.junit.Test;

public class QueryParamTest {

    @Test
    public void testPage() {
        QueryParam param = new QueryParam();

        param.doPaging(2, 10);

        param.rePaging(15);//总数只有15条,重新分页到最后一页

        Assert.assertEquals(param.getPageIndex(), 1);

    }

    @Test
    public void testCustomFirstPage(){
        QueryParam param = new QueryParam();
        param.setFirstPageIndex(1);

        param.doPaging(1,10);

        Assert.assertEquals(param.getPageIndex(),0);
        Assert.assertEquals(param.getThinkPageIndex(),1);

        param.doPaging(2,10);

        Assert.assertEquals(param.getPageIndex(),1);

        Assert.assertEquals(param.getThinkPageIndex(),2);

    }
}