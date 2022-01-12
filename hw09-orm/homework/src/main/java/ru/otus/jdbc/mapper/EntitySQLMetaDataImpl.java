package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        final var columns = entityClassMetaData.getAllFields().stream().map(Field::getName).toList();
        final var tableName = entityClassMetaData.getName().toLowerCase();
        return "select " + String.join(",", columns) +
                " from " + tableName;
    }

    @Override
    public String getSelectByIdSql() {
        final var fieldId = entityClassMetaData.getIdField().getName();
        return getSelectAllSql() + " where " + fieldId + " = ?";
    }

    @Override
    public String getInsertSql() {
        final var tableName = entityClassMetaData.getName().toLowerCase();
        final var columns = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).toList();
        final var columnsPlaceHolder = String.join(",", columns.stream().map(e -> "?").toList());
        return "insert into " + tableName +
                String.format(" (%s) ", String.join(",", columns)) +
                String.format("values (%s)", columnsPlaceHolder);
    }

    @Override
    public String getUpdateSql() {
        final var tableName = entityClassMetaData.getName().toLowerCase();
        final var columns = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .map(param -> param + " = ?").toList();

        return "update " + tableName + " set " + String.join(",", columns) +
                " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }
}
