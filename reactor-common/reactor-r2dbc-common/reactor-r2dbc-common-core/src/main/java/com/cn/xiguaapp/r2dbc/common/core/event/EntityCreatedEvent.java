package com.cn.xiguaapp.r2dbc.common.core.event;

import com.cn.xiguaapp.r2dbc.orm.event.DefaultAsyncEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @see com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEntityEvent
 * @since 1.0 17:19
 */
@AllArgsConstructor
@Getter
public class EntityCreatedEvent <T> extends DefaultAsyncEvent implements Serializable {
    private final List<T> entity;
    private final Class<T> entityType;
}
