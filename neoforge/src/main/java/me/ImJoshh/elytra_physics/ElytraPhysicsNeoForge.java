package me.ImJoshh.elytra_physics;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.Config;
import me.ImJoshh.elytra_physics.config.ConfigData;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


@Mod(ElytraPhysicsNeoForge.MOD_ID)
public final class ElytraPhysicsNeoForge {

    public static final String MOD_ID = "elytra_physics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsNeoForge(final IEventBus eventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            List<String> injectLayersStrings = new ArrayList<>(Config.LAYER_INJECTORS.get());
            injectLayersStrings.add(WingsLayer.class.getName());

            for (String injectLayerString : injectLayersStrings)
            {
                try {
                    Class<RenderLayer<?, ?>> clazz = (Class<RenderLayer<?, ?>>) Class.forName(injectLayerString);
                    ConfigData.addLayerToInject(clazz);

                    LOGGER.info("Successfully added class '" + clazz.getName() + "' to layer inject list");
                }
                catch (Exception e) {
                    LOGGER.info("Class '" + injectLayerString + "' not found");
                }
            }
        }
    }
}
