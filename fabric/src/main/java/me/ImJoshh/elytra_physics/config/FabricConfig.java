package me.ImJoshh.elytra_physics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class FabricConfig
{
    private static ModConfig loadedConfig;
    private static ModConfig defaultConfig;

    public static void init()
    {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        File configFile = configDir.resolve("elytra-physics-config.json").toFile();

        Map<String, Object> defaultConfigJson = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsClientMod.class);

        if (defaultConfigJson == null)
        {
            ElytraPhysicsClientMod.LOGGER.error("Default config file could not be found in mod Jar");
            return;
        }

        defaultConfig = ModConfig.fromJSON(defaultConfigJson);

        if (!configFile.exists())
        {
            // set loaded config to default config
            loadedConfig = defaultConfig.copy(configFile);
            loadedConfig.saveConfig();
        }
        else {
            loadedConfig = ModConfig.fromFile(configFile);
        }
    }

    public static Object getConfigValue(String key)
    {
//        ElytraPhysicsClientMod.LOGGER.info("loaded: " + loadedConfig.get(key));
//        ElytraPhysicsClientMod.LOGGER.info("default: " + defaultConfig.get(key));
        Object fromLoaded = loadedConfig.get(key);
        if (fromLoaded != null)
            return fromLoaded;

        Object fromDefault = defaultConfig.get(key);
        if (fromDefault != null)
        {
            loadedConfig.add(key, fromDefault);
            loadedConfig.saveConfig();

            return fromDefault;
        }

        return null;
    }
}
