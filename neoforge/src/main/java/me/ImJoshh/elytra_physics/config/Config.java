package me.ImJoshh.elytra_physics.config;


import me.ImJoshh.elytra_physics.ElytraPhysicsNeoForge;
import me.ImJoshh.elytra_physics.config.ConfigKeys;
import me.ImJoshh.elytra_physics.config.DefaultConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@EventBusSubscriber(modid = ElytraPhysicsNeoForge.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final Map<String, Object> DEFAULT_CONFIG = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsNeoForge.class);
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> LAYER_INJECTORS = BUILDER
            .comment("Classpaths of modded elytra layers")
            .defineListAllowEmpty(
                    ConfigKeys.LAYER_INJECTORS,
                    ObjectUtils.defaultIfNull(Config.getFromDefaultConfig(ConfigKeys.LAYER_INJECTORS, ArrayList.class), List.of()),
                    (injector) -> injector instanceof String);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static <T> T getFromDefaultConfig(String key, Class<T> type)
    {
        if (DEFAULT_CONFIG == null)
            return null;

        Object retrieved = DEFAULT_CONFIG.get(key);

        ElytraPhysicsNeoForge.LOGGER.debug("retrieved type: " + retrieved.getClass().getName());
        if (!retrieved.getClass().isNestmateOf(type))
            return null;

        ElytraPhysicsNeoForge.LOGGER.debug("Value '" + key + "' found in default config");

        return type.cast(retrieved);
    }

}
