package ru.otus.jdbc.mapper;

import ru.otus.core.annotations.*;
import ru.otus.jdbc.mapper.exceptions.MappingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        final var ids = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class)).count();
        if (ids != 1) {
            throw new MappingException("Could not find id field or too many id fields");
        }
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return this.clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        var types = getAllFields().stream().map(Field::getType).toList();
        var args = types.toArray(new Class[0]);
        try {
            return this.clazz.getConstructor(args);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new MappingException();
        }

    }

    @Override
    public Field getIdField() {
        return getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow(MappingException::new);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(this.clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(field -> !field.isAnnotationPresent(Id.class))
                .peek(field -> field.setAccessible(true)).toList();
    }
}
