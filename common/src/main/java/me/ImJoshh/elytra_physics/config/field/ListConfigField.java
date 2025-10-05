package me.ImJoshh.elytra_physics.config.field;

import java.util.function.Supplier;

public class ListConfigField<T> extends ConfigField<T> {

    private final Supplier<T> newElementSupplier;
    public ListConfigField(String key, Class<T> fieldType, Supplier<T> newElementSupplier) {
        super(key, fieldType);

        this.newElementSupplier = newElementSupplier;
    }

    public Supplier<T> getNewElementSupplier() {
        return newElementSupplier;
    }
}
