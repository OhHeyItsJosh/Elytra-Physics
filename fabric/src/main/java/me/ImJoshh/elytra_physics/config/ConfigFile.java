package me.ImJoshh.elytra_physics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigFile
{
    final private Map<String, Object> _data;
    final private File _file;

    private ConfigFile(Map<String, Object> data, File file)
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

    public String getFilePath() {
        return this._file.getPath();
    }

    public <T> T get(String key)
    {
        try {
            return (T) this._data.get(key);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(String key, Object value)
    {
        this._data.put(key, value);
    }

    public void saveConfig() throws IOException {
        
        assert (this.hasLinkedFile() && this._file != null);

        if (!this._file.exists())
        {
            this._file.createNewFile();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(this._data);

        FileWriter writer = new FileWriter(this._file);
        writer.write(jsonString);
        writer.close();
    }

    public ConfigFile copy() {
        return this.copy(this._file);
    }

    public ConfigFile copy(File other)
    {
        return new ConfigFile(new HashMap<>(this._data), other);
    }


    public static ConfigFile fromFile(File configFile) {
        Map<String, Object> parsedJson = parseJsonFile(configFile);
        if (parsedJson == null)
        {
            parsedJson = new HashMap<>();
        }

        return new ConfigFile(parsedJson, configFile);
    }

    public static ConfigFile fromJSON(Map<String, Object> json)
    {
        return new ConfigFile(json, null);
    }

    static Map<String, Object> parseJsonFile(File file) {
//        ElytraPhysicsClientMod.LOGGER.info("Attempting to parse file " + file.getPath());

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
