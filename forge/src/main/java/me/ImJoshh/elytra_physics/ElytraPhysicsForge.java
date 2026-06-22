package me.ImJoshh.elytra_physics;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import me.ImJoshh.elytra_physics.config.ForgeConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;


@Mod(ElytraPhysics.MOD_ID)
public final class ElytraPhysicsForge {

    public static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfig.SPEC);
        LOGGER.info("LOADER SETUP RUN");

    }

    @Mod.EventBusSubscriber(modid = ElytraPhysics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientEvents {
        @SubscribeEvent
        public static void onConfigLoad(ModConfigEvent.Loading event) {
            LOGGER.debug("config loaded, caching values");
            ElytraPhysics.setConfig(new ElytraPhysicsConfig(ForgeConfig.VALUE_PROVIDER));
        }

        @SubscribeEvent
        public static void onConfigReload(ModConfigEvent.Reloading event) {
            LOGGER.debug("config reloaded, re-caching values");
            ElytraPhysics.getConfig().cacheFields();
        }
    }
}
