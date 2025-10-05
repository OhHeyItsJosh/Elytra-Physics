package me.ImJoshh.elytra_physics.mixinFlagSystem;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.ElytraPhysics;
import org.apache.logging.log4j.core.jackson.MapEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixinFlagsManager {

    private static final Path FLAGS_CONFIG_FILE_DIR;

    private final Map<String, Boolean> defaults;

    public MixinFlagsManager(Map<String, Boolean> defaults) {
        this.defaults = defaults;
    }

    public Map<String, Boolean> loadMixinFlags() {
        try {
            List<String> rawConfig = Files.readAllLines(FLAGS_CONFIG_FILE_DIR);
            return this.parseRawFlags(rawConfig);
        }
        catch(IOException e) {
            LogUtils.getLogger().warn(String.format("Mixin flags for '%s' could not be loaded: %s", ElytraPhysics.MOD_ID, e.getMessage()));
            return this.defaults;
        }
    }

    public void serialiseFlags(Map<String, Boolean> flags) throws IOException {
        StringBuilder plaintextBuilder = new StringBuilder();

        // serialise to string
        for (Map.Entry<String, Boolean> flag : flags.entrySet()) {
            plaintextBuilder
                    .append(flag.getKey())
                    .append("=")
                    .append(flag.getValue())
                    .append("\n");
        }

        Files.writeString(FLAGS_CONFIG_FILE_DIR, plaintextBuilder.toString());
    }

    private Map<String, Boolean> parseRawFlags(List<String> lines) {
        Map<String, Boolean> flags = new HashMap<>();

        for (String line : lines)
        {
            String[] split = line.split("=");
            String key = split[0].toLowerCase().trim();
            Boolean state = Boolean.parseBoolean(split[1]);

            flags.put(key, state);
        }

        return flags;
    }

    static {
        // from my testing user.dir is always the game directory for the instance, hopefully this doesn't change with
        // different launchers or operating systems.
        FLAGS_CONFIG_FILE_DIR = Paths.get(System.getProperty("user.dir") + String.format("/config/%s_mixin-flags.txt", ElytraPhysics.MOD_ID));
    }
}
