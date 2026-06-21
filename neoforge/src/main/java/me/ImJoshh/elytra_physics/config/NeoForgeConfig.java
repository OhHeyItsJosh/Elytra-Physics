package me.ImJoshh.elytra_physics.config;


import me.ImJoshh.elytra_physics.ElytraPhysicsNeoForge;
import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;

//@EventBusSubscriber(modid = ElytraPhysicsNeoForge.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unchecked")
public class NeoForgeConfig
{
    /// static NeoForge config stuff ///

    private static final Map<String, Object> DEFAULT_CONFIG = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsNeoForge.class);
    public static final Map<String, ModConfigSpec.ConfigValue<?>> VALUE_ACCESSORS = new HashMap<>();
    public static final PlatformConfigValueProvider VALUE_PROVIDER;
    public static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        for (ConfigField<?> field : ElytraPhysicsConfig.Spec.get())
        {
            ModConfigSpec.ConfigValue<?> configField = resolveConfigField(BUILDER, field);
            VALUE_ACCESSORS.put(field.KEY, configField);

        }

        SPEC = BUILDER.build();
    }

    private static <T> ModConfigSpec.ConfigValue<?> resolveConfigField(ModConfigSpec.Builder builder, ConfigField<T> field) {
        if (field instanceof ListConfigField<T> listField) {
            return builder.defineListAllowEmpty(
                    field.KEY,
                    () -> getFromDefaultConfig(field.KEY, ArrayList.class),
                    listField.getNewElementSupplier(),
                    (injector) -> field.getFieldType().isInstance(injector));
        }
        // have to manually cast double and boolean as the config is stupid and only accepts primitives
        else if (field.getFieldType().equals(Double.class)) {
            return builder.define(
                    field.KEY,
                    (double) getFromDefaultConfig(field.KEY, field.getFieldType()));
        }
        else if (field.getFieldType().equals(Boolean.class)) {
            return builder.define(
                    field.KEY,
                    (boolean) getFromDefaultConfig(field.KEY, field.getFieldType()));
        }
        else {
            return builder.define(
                    field.KEY,
                    getFromDefaultConfig(field.KEY, field.getFieldType()));
        }
    }

    private static <T> T getFromDefaultConfig(String key, Class<? extends T> type)
    {
        assert (DEFAULT_CONFIG != null);
        Object retrieved = DEFAULT_CONFIG.get(key);

        ElytraPhysicsNeoForge.LOGGER.debug("retrieved type: " + retrieved.getClass().getName());
        if (!retrieved.getClass().isNestmateOf(type))
            throw new RuntimeException(String.format("Value for '%s' in default config does not match type (%s) in spec", key, type));

        ElytraPhysicsNeoForge.LOGGER.debug("Value '" + key + "' found in default config");

        return type.cast(retrieved);
    }

    static {
        VALUE_PROVIDER = new PlatformConfigValueProvider() {
            @Override
            public <T> T getField(String key) {
                return (T) VALUE_ACCESSORS.get(key).get();
            }
        };
    }
}
