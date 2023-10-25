package me.ImJoshh.elytra_physics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class ElytraPhysicsConfigManager
{
    private static ModConfig loadedConfig;
    private static ModConfig defaultConfig;

    public static void init()
    {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        File configFile = configDir.resolve("elytra-physics-config.json").toFile();

        Map<String, Object> defaultConfigJson = getDefaultConfigJSON();

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



    static Map<String, Object> getDefaultConfigJSON()
    {
        try {
            InputStream stream = ElytraPhysicsClientMod.class.getClassLoader().getResourceAsStream("defaultConfig.json");
            if (stream == null)
                return null;

            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                builder.append(line);
            }

            bufferedReader.close();
            reader.close();
            stream.close();

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(builder.toString(), Map.class);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
