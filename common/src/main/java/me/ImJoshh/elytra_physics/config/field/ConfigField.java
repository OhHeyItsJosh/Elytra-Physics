package me.ImJoshh.elytra_physics.config.field;

public class ConfigField<T> {
    public final String KEY;
    private final Class<T> clazz;

    public ConfigField(String key, Class<T> fieldType) {
        this.KEY = key;
        this.clazz = fieldType;
    }

    public Class<T> getFieldType() {
        return this.clazz;
    }
}
