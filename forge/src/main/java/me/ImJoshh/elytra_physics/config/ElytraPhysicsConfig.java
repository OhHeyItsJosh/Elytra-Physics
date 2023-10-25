package me.ImJoshh.elytra_physics.config;

import me.ImJoshh.elytra_physics.ElytraPhysicsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(modid = ElytraPhysicsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElytraPhysicsConfig
{
    private static final Map<String, Object> DEFAULT_CONFIG = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsMod.class);
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

//    public static List<? extends String> layerInjectors;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAYER_INJECTORS = BUILDER
            .comment("Classpaths of modded elytra layers")
            .defineListAllowEmpty(ConfigKeys.LAYER_INJECTORS,
                                  ObjectUtils.defaultIfNull(ElytraPhysicsConfig.getFromDefaultConfig(ConfigKeys.LAYER_INJECTORS, ArrayList.class), List.of()),
                                  (injector) -> injector instanceof String);

    public static final ForgeConfigSpec SPEC = BUILDER.build();


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    }

    private static <T> T getFromDefaultConfig(String key, Class<T> type)
    {
        if (DEFAULT_CONFIG == null)
            return null;

        Object retrieved = DEFAULT_CONFIG.get(key);

        ElytraPhysicsMod.LOGGER.debug("retrieved type: " + retrieved.getClass().getName());
        if (!retrieved.getClass().isNestmateOf(type))
            return null;

        ElytraPhysicsMod.LOGGER.debug("Value '" + key + "' found in default config");

        return type.cast(retrieved);
    }
}
