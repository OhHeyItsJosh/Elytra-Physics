package me.ImJoshh.elytra_physics.config;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

public class FabricConfig
{
    private static ConfigFile loadedConfig;
    private static ConfigFile defaultConfig;

    public static final PlatformConfigBridge CONFIG_BRIDGE;

    public static void init()
    {
        Map<String, Object> defaultConfigJson = DefaultConfig.getDefaultConfigJSON(ElytraPhysicsClientMod.class);

        if (defaultConfigJson == null)
        {
            throw new RuntimeException("Default config file could not be found in mod Jar");
        }

        defaultConfig = ConfigFile.fromJSON(defaultConfigJson);

        loadConfigFromDisk();
    }

    private static void loadConfigFromDisk() {
        assert (defaultConfig != null);

        Path configDir = FabricLoader.getInstance().getConfigDir();
        File configFile = configDir.resolve("elytra-physics-config.json").toFile();

        if (!configFile.exists())
        {
            // set loaded config to default config
            loadedConfig = defaultConfig.copy(configFile);
            save(loadedConfig);
        }
        else {
            loadedConfig = ConfigFile.fromFile(configFile);
        }
    }

    public static <T> T getConfigValue(String key)
    {
//        ElytraPhysicsClientMod.LOGGER.info("loaded: " + loadedConfig.get(key));
//        ElytraPhysicsClientMod.LOGGER.info("default: " + defaultConfig.get(key));
        T fromLoaded = loadedConfig.get(key);
        if (fromLoaded != null)
            return fromLoaded;

        T fromDefault = defaultConfig.get(key);
        if (fromDefault != null)
        {
            loadedConfig.set(key, fromDefault);
            save(loadedConfig);

            return fromDefault;
        }

        return null;
    }

    public static <T> T getDefaultValue(String key) {
        return defaultConfig.get(key);
    }

    public static void attemptSaveOperation(Consumer<ConfigFile> updateFields) {
        ConfigFile configCopy = loadedConfig.copy();
        updateFields.accept(configCopy);

        boolean success = save(configCopy);
        if (success) {
            loadedConfig = configCopy;
            ElytraPhysics.getConfig().cacheFields();
        }

    }

    private static boolean save(ConfigFile file) {
        try {
            file.saveConfig();
            ElytraPhysicsClientMod.LOGGER.info("Successfully saved config to file " + loadedConfig.getFilePath());

            return true;

        }
        catch (IOException e) {
            ElytraPhysicsClientMod.LOGGER.error("Config file could not be saved");
            e.printStackTrace();

            return false;
        }
    }

    static {
        CONFIG_BRIDGE = new PlatformConfigBridge() {
            @Override
            public <T> T getFieldValue(String key) {
                return FabricConfig.getConfigValue(key);
            }

            @Override
            public <T> T getFieldDefaultValue(String key) {
                return FabricConfig.getDefaultValue(key);
            }

            @Override
            public void saveConfig(ConfigValue<?, ?>[] entries) {
                FabricConfig.attemptSaveOperation((configFile) -> {
                    for (ConfigValue<?, ?> entry : entries)
                    {
                        configFile.set(entry.getKey(), entry.getValue());
                    }
                });
            }
        };
    }
}
