package me.ImJoshh.elytra_physics.config;


import me.ImJoshh.elytra_physics.ElytraPhysics;
import me.ImJoshh.elytra_physics.ElytraPhysicsForge;
import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import me.ImJoshh.elytra_physics.config.ui.ElytraPhysicsConfigScreen;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

//@EventBusSubscriber(modid = ElytraPhysicsNeoForge.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unchecked")
public class ForgeConfig
{
    /// static NeoForge config stuff ///

    private static final Map<String, Object> DEFAULT_CONFIG = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsForge.class);
    public static final Map<String, ForgeConfigSpec.ConfigValue<?>> VALUE_ACCESSORS = new HashMap<>();
    public static final PlatformConfigBridge CONFIG_BRIDGE;
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        for (ConfigField<?> field : ElytraPhysicsConfig.Spec.get())
        {
            ForgeConfigSpec.ConfigValue<?> configField = resolveConfigField(BUILDER, field);
            VALUE_ACCESSORS.put(field.KEY, configField);

        }

        SPEC = BUILDER.build();
    }

    private static <T> ForgeConfigSpec.ConfigValue<?> resolveConfigField(ForgeConfigSpec.Builder builder, ConfigField<T> field) {
        if (field instanceof ListConfigField<T> listField) {
            return builder.defineListAllowEmpty(
                    field.KEY,
                    () -> getFromDefaultConfig(field.KEY, ArrayList.class),
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

        ElytraPhysicsForge.LOGGER.debug("retrieved type: " + retrieved.getClass().getName());
        if (!retrieved.getClass().isNestmateOf(type))
            throw new RuntimeException(String.format("Value for '%s' in default config does not match type (%s) in spec", key, type));

        ElytraPhysicsForge.LOGGER.debug("Value '" + key + "' found in default config");

        return type.cast(retrieved);
    }

    static {
        CONFIG_BRIDGE = new PlatformConfigBridge() {
            @Override
            public <T> T getFieldValue(String key) {
                return (T) VALUE_ACCESSORS.get(key).get();
            }

            @Override
            public <T> T getFieldDefaultValue(String key) {
                return (T) VALUE_ACCESSORS.get(key).getDefault();
            }

            @Override
            public void saveConfig(ConfigValue<?, ?>[] entries) {
                for (ConfigValue<?, ?> entry :  entries)
                {
                    ((ForgeConfigSpec.ConfigValue<Object>)VALUE_ACCESSORS.get(entry.getKey())).set(entry.getValue());
                }

                SPEC.save();

                ElytraPhysicsForge.LOGGER.debug("config reloaded, re-caching values");
                ElytraPhysics.getConfig().cacheFields();
            }
        };
    }
}
