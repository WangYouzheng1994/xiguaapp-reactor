package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.config.GlobalConfig;
import com.cn.xiguaapp.r2dbc.orm.operator.ObjectPropertyOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.LazyEntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.MappingFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.EventResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.SaveResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.UpsertOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.tableMetadata;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.*;

/**
 * @author xiguaapp
 * @param <E>
 */
public abstract class DefaultRepository<E> {

    protected DatabaseOperator operator;

    protected ResultWrapper<E, ?> wrapper;

    private volatile String idColumn;

    @Getter
    protected EntityColumnMapping mapping;

    @Setter
    protected volatile String[] properties;

    protected Supplier<RDBTableMetadata> tableSupplier;

    protected final List<ContextKeyValue> defaultContextKeyValue = new ArrayList<>();

    @Getter
    @Setter
    private ObjectPropertyOperator propertyOperator = GlobalConfig.getPropertyOperator();

    public DefaultRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> supplier, ResultWrapper<E, ?> wrapper) {
        this.operator = operator;
        this.wrapper = wrapper;
        this.tableSupplier = supplier;
        defaultContextKeyValue.add(MappingContextKeys.repository.value(this));
        defaultContextKeyValue.add(ContextKeys.database.value(operator));

    }

    protected RDBTableMetadata getTable() {
        return tableSupplier.get();
    }

    protected ContextKeyValue[] getDefaultContextKeyValue(ContextKeyValue... kv) {
        if (kv.length == 0) {
            return defaultContextKeyValue.toArray(new ContextKeyValue[0]);
        }
        List<ContextKeyValue> keyValues = new ArrayList<>(defaultContextKeyValue);
        keyValues.addAll(Arrays.asList(kv));
        return keyValues.toArray(new ContextKeyValue[0]);
    }

    public String[] getProperties() {
        if (properties == null) {
            properties = mapping.getColumnPropertyMapping()
                    .entrySet()
                    .stream()
                    .filter(kv -> getTable().getColumn(kv.getKey()).isPresent())
                    .map(Map.Entry::getValue)
                    .toArray(String[]::new);
        }
        return properties;
    }

    protected String getIdColumn() {
        if (idColumn == null) {
            this.idColumn = getTable().getColumns().stream()
                    .filter(RDBColumnMetadata::isPrimaryKey)
                    .findFirst()
                    .map(RDBColumnMetadata::getName)
                    .orElseThrow(() -> new UnsupportedOperationException("id column not exists"));
        }
        return idColumn;
    }

    protected void initMapping(Class<E> entityType) {

        this.mapping = LazyEntityColumnMapping.of(() -> getTable()
                .<EntityColumnMapping>findFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(entityType))
                .orElseThrow(() -> new UnsupportedOperationException("unsupported columnPropertyMapping feature")));
        defaultContextKeyValue.add(MappingContextKeys.columnMapping(mapping));
    }

    protected SaveResultOperator doSave(Collection<E> data) {
        RDBTableMetadata table = getTable();
        UpsertOperator upsert = operator.dml().upsert(table.getFullName());
        upsert.columns(getProperties());

        List<String> ignore = new ArrayList<>();

        for (E e : data) {
            upsert.values(Stream.of(getProperties())
                    .map(property -> getInsertColumnValue(e, property, (prop, val) -> ignore.add(prop)))
                    .toArray());
        }
        upsert.ignoreUpdate(ignore.toArray(new String[0]));
        return EventResultOperator.create(
                upsert::execute,
                SaveResultOperator.class,
                table,
                MappingEventTypes.save_before,
                MappingEventTypes.save_after,
                getDefaultContextKeyValue(MappingContextKeys.instance(data),
                        type("batch"),
                        tableMetadata(table),
                        upsert(upsert))
        );
    }

    protected InsertResultOperator doInsert(E data) {
        RDBTableMetadata table = getTable();
        InsertOperator insert = operator.dml().insert(table.getFullName());

        for (Map.Entry<String, String> entry : mapping.getColumnPropertyMapping().entrySet()) {
            String column = entry.getKey();
            String property = entry.getValue();

            insert.value(column, getInsertColumnValue(data, property));
        }

        return EventResultOperator.create(
                insert::execute,
                InsertResultOperator.class,
                table,
                MappingEventTypes.insert_before,
                MappingEventTypes.insert_after,
                getDefaultContextKeyValue(
                        MappingContextKeys.instance(data),
                        type("single"),
                        tableMetadata(table),
                        insert(insert))
        );

    }

    private Object getInsertColumnValue(E data, String property, BiConsumer<String, Object> whenDefaultValue) {
        Object value = propertyOperator.getProperty(data, property).orElse(null);
        if (value == null) {
            value = mapping.getColumnByProperty(property)
                    .flatMap(RDBColumnMetadata::generateDefaultValue)
                    .orElse(null);
            if (value != null) {
                whenDefaultValue.accept(property, value);
                //回填
                propertyOperator.setProperty(data, property, value);
            }
        }
        return value;
    }

    private Object getInsertColumnValue(E data, String property) {

        return getInsertColumnValue(data, property, (prop, val) -> {
        });
    }

    protected InsertResultOperator doInsert(Collection<E> batch) {
        RDBTableMetadata table = getTable();
        InsertOperator insert = operator.dml().insert(table.getFullName());

        insert.columns(getProperties());

        for (E e : batch) {
            insert.values(Stream.of(getProperties())
                    .map(property -> getInsertColumnValue(e, property))
                    .toArray());
        }


        return EventResultOperator.create(
                insert::execute,
                InsertResultOperator.class,
                table,
                MappingEventTypes.insert_before,
                MappingEventTypes.insert_after,
                getDefaultContextKeyValue(
                        instance(batch),
                        type("batch"),
                        tableMetadata(table),
                        insert(insert))
        );
    }

}
