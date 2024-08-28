package me.ImJoshh.elytra_physics;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.ConfigData;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ElytraPhysicsMod.MOD_ID)
public class ElytraPhysicsMod
{
    public static final String MOD_ID = "elytra_physics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ElytraPhysicsConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
    }

    @SuppressWarnings("unchecked")
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientStartup(FMLClientSetupEvent event)
        {
            List<String> injectLayersStrings = new ArrayList<>(ElytraPhysicsConfig.LAYER_INJECTORS.get());
            injectLayersStrings.add(ElytraLayer.class.getName());

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
