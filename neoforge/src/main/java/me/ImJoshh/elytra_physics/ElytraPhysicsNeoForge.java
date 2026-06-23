package me.ImJoshh.elytra_physics;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import me.ImJoshh.elytra_physics.config.NeoForgeConfig;
import me.ImJoshh.elytra_physics.config.ui.ElytraPhysicsConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(ElytraPhysics.MOD_ID)
public final class ElytraPhysicsNeoForge {

    public static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsNeoForge(final IEventBus eventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfig.SPEC);

        LOGGER.info("LOADER SETUP RUN");

        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> new ElytraPhysicsConfigScreen());
        }
    }

    @EventBusSubscriber(modid = ElytraPhysics.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientEvents {
        // cache config values whenever config is loaded or reloaded
        @SubscribeEvent
        private static void onConfigLoad(ModConfigEvent.Loading event) {
            LOGGER.debug("config loaded, caching values");
            ElytraPhysics.setConfig(new ElytraPhysicsConfig(NeoForgeConfig.CONFIG_BRIDGE));
        }

        @SubscribeEvent
        private static void onConfigReload(ModConfigEvent.Reloading event) {
            LOGGER.debug("config reloaded, re-caching values");
            ElytraPhysics.getConfig().cacheFields();
        }
    }
}
