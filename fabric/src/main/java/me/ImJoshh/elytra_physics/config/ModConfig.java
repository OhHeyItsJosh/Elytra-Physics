package me.ImJoshh.elytra_physics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModConfig
{
    final private Map<String, Object> _data;
    final private File _file;

    private ModConfig(Map<String, Object> data, File file)
    {
        this._data = data;
        this._file = file;
    }

    public boolean hasKey(String key)
    {
        return this._data.containsKey(key);
    }

    public boolean hasLinkedFile()
    {
        return this._file != null;
    }

    public Object get(String key)
    {
        return this._data.get(key);
    }

    public void add(String key, Object value)
    {
        this._data.put(key, value);
    }

    public void saveConfig()
    {
        assert (this.hasLinkedFile());

        try {
            if (!this._file.exists())
            {
                this._file.createNewFile();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(this._data);

            FileWriter writer = new FileWriter(this._file);
            writer.write(jsonString);
            writer.close();

            ElytraPhysicsClientMod.LOGGER.info("Successfully saved config to file " + this._file.getPath());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ModConfig copy(File other)
    {
        return new ModConfig(this._data, other);
    }

    public static ModConfig fromFile(File configFile) {
        Map<String, Object> parsedJson = parseJsonFile(configFile);
        if (parsedJson == null)
        {
            parsedJson = new HashMap<>();
        }

        return new ModConfig(parsedJson, configFile);
    }

    public static ModConfig fromJSON(Map<String, Object> json)
    {
        return new ModConfig(json, null);
    }

    static Map<String, Object> parseJsonFile(File file) {
        ElytraPhysicsClientMod.LOGGER.info("Attempting to parse file " + file.getPath());

        try {
            FileReader reader = new FileReader(file);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<String, Object> decodedJson = gson.fromJson(reader, Map.class);

            reader.close();
            return decodedJson;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
