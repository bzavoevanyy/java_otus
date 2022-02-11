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
        final var sortedBeanFactories = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        processMethods(sortedBeanFactories);
    }
    private void processMethods(List<Method> sortedBeanFactories) {
        sortedBeanFactories.forEach(method -> {
            try {
                List<Object> args = new ArrayList<>();
                final var parameterTypes = method.getParameterTypes();
                for (var parameterType : parameterTypes) {
                    final Object appComponent = getAppComponent(parameterType);
                    if (appComponent == null) {
                        throw new RuntimeException("Cannot find bean candidate for injection");
                    }
                    args.add(appComponent);
                }
                final var bean = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args.toArray());
                final var oldBean = appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), bean);
                if (oldBean != null) {
                    throw new RuntimeException("Two or more beans with the same name");
                }
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
        for (Object appComponent : appComponents) {
            if (componentClass.isAssignableFrom(appComponent.getClass())) {
                return (C) appComponent;
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
