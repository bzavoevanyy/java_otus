package ru.otus.cachehw;


import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final Map<K, V> store = new WeakHashMap<>();
    private List<WeakReference<HwListener<K, V>>> hwListeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        this.store.put(key, value);
        this.hwListeners.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        final var value = this.store.remove(key);
        this.hwListeners.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .forEach(listener -> listener.notify(key, value, "remove"));
    }

    @Override
    public V get(K key) {
        return this.store.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.hwListeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.hwListeners = this.hwListeners.stream().map(WeakReference::get)
                .filter(Objects::nonNull).filter(el -> !el.equals(listener))
                .map(WeakReference::new).toList();
    }
}
