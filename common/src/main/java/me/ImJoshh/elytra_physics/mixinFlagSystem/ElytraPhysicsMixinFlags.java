package me.ImJoshh.elytra_physics.mixinFlagSystem;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.ElytraPhysics;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElytraPhysicsMixinFlags {
    public static final String APPLY_SNAPPING_FIX = "snapping_fix";

    public static Map<String, Boolean> defaults() {
        return new HashMap<>() {{
            put(APPLY_SNAPPING_FIX, true);
        }};
    }

    public static void serialise(boolean applySnappingFix) {
        MixinFlagsManager mixinFlagsManager = new MixinFlagsManager(defaults());
        try {
            mixinFlagsManager.serialiseFlags(new HashMap<>() {{
                put(ElytraPhysicsMixinFlags.APPLY_SNAPPING_FIX, applySnappingFix);
            }});
        } catch (IOException e) {
            Logger logger = LogUtils.getLogger();

            logger.error(String.format("Mixin flags for '%s' could not be saved: %s", ElytraPhysics.MOD_ID, e.getMessage()));
            e.printStackTrace();
        }
    }
}
