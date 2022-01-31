package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.util.*;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass)
                .sorted(Comparator.comparing(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String configPackage) {
        Reflections reflections = new Reflections(configPackage, TypesAnnotated);

        var configs = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(new Class<?>[0]);
        Arrays.stream(configs)
                .sorted(Comparator.comparing(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    final var parameterCount = method.getParameterCount();
                    try {
                        if (parameterCount == 0) {
                            final var bean = method
                                    .invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance());

                            appComponents.add(bean);
                            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), bean);
                        } else {
                            final var args = Arrays.stream(method.getParameterTypes())
                                    .map(parameter -> appComponents.stream().map(component -> {
                                        try {
                                            return parameter.cast(component);
                                        } catch (Exception e) {
                                            return null;
                                        }
                                    }).filter(Objects::nonNull).findFirst().orElseThrow()).toArray();
                            final var bean = method
                                    .invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args);
                            appComponents.add(bean);
                            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), bean);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
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
        }).filter(Objects::nonNull).findFirst().orElseThrow();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
