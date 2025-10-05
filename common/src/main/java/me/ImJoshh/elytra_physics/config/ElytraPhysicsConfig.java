package me.ImJoshh.elytra_physics.config;

import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.Set;

public interface ElytraPhysicsConfig {
    Set<Class<RenderLayer<?, ?>>> getElytraLayers();
    boolean adaptiveGravityEnabled();
    boolean preventSnappingEnabled();
    float getGravityMultiplier();
    float getLeanMultiplier();
    float getSpreadMultiplier();

    class Spec {
        public static ListConfigField<String> LAYER_INJECTORS = new ListConfigField<>("inject_layers", String.class, () -> "");

        public static ConfigField<Boolean> ADAPTIVE_GRAVITY = new ConfigField<>("adaptive_gravity", Boolean.class);
        public static ConfigField<Boolean> PREVENT_SNAPPING = new ConfigField<>("prevent_snapping", Boolean.class);

        public static ConfigField<Double> GRAVITY_MULTIPLIER = new ConfigField<>("gravity_multiplier", Double.class);
        public static ConfigField<Double> ELYTRA_LEAN_MULTIPLIER = new ConfigField<>("elytra_lean_multiplier", Double.class);
        public static ConfigField<Double> ELYTRA_SPREAD_MULTIPLIER = new ConfigField<>("elytra_spread_multiplier", Double.class);

        public static ConfigField<?>[] get() {
            return new ConfigField[] {LAYER_INJECTORS, ADAPTIVE_GRAVITY, PREVENT_SNAPPING, GRAVITY_MULTIPLIER, ELYTRA_LEAN_MULTIPLIER, ELYTRA_SPREAD_MULTIPLIER};
        }

    }
}
