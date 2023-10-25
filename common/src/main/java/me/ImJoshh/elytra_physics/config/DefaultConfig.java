package me.ImJoshh.elytra_physics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class DefaultConfig {
    public static Map<String, Object> getDefaultConfigJSON(Class<?> modClass)
    {
        try {
            InputStream stream = modClass.getClassLoader().getResourceAsStream("defaultConfig.json");
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
