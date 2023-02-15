package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;

import java.util.List;
import java.util.Optional;

public interface SyncQuery<T> extends DSLQuery<SyncQuery<T>> {

    List<T> fetch();

    Optional<T> fetchOne();

    int count();

}
