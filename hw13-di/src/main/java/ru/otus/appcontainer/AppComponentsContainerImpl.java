package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(String configPackage) {
        Reflections reflections = new Reflections(configPackage, TypesAnnotated);

        var configs = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(new Class<?>[0]);
        processConfig(configs);
    }
    private void processConfig(Class<?>... configs) {
        Arrays.stream(configs)
                .sorted(Comparator.comparing(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }
    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final var sortedAppComponents = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        processMethods(sortedAppComponents);
    }
    private void processMethods(List<Method> sortedAppComponents) {
        sortedAppComponents.forEach(method -> {
            try {
                List<Object> args = new ArrayList<>();
                final var parameterTypes = method.getParameterTypes();
                for (var parameterType : parameterTypes) {
                    args.add(getAppComponent(parameterType));
                }
                final var bean = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args.toArray());
                final var oldBean = appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), bean);
                appComponents.remove(oldBean);
                appComponents.add(bean);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Error creating bean: %s", e.getMessage()));
            }
        });
    }
    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return appComponents.stream().map(component -> {
            try {
                return componentClass.cast(component);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
