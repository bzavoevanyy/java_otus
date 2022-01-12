package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        final var columns = entityClassMetaData.getAllFields().stream().map(Field::getName)
                .toList();
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(),
                List.of(id), rs -> {
                    T result = null;
                    try {
                        if (rs.next()) {
                            var argsList = new ArrayList<>();
                            for (String column : columns) {
                                argsList.add(rs.getObject(column));
                            }
                            result = entityClassMetaData.getConstructor().newInstance(argsList.toArray());
                        }
                    } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                    return result;
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        final var columns = entityClassMetaData.getAllFields().stream().map(Field::getName)
                .toList();

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(),
                List.of(), rs -> {
                    List<T> resultList = new ArrayList<>();
                    try {
                        while (rs.next()) {
                            var argsList = new ArrayList<>();
                            for (String column : columns) {
                                argsList.add(rs.getObject(column));
                            }
                            resultList.add(entityClassMetaData.getConstructor().newInstance(argsList.toArray()));
                        }
                    } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                    return resultList;
                }).orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                params.add(field.get(client));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        final var fields = entityClassMetaData.getFieldsWithoutId();
        fields.add(entityClassMetaData.getIdField());
        for (Field field : fields) {
            try {
                params.add(field.get(client));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }
}
