package me.ImJoshh.elytra_physics.config;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import me.ImJoshh.elytra_physics.mixinFlagSystem.ElytraPhysicsMixinFlags;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ElytraPhysicsConfig {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PlatformConfigValueProvider valueProvider;

    private Set<Class<RenderLayer<?, ?>>> elytraLayers;
    private boolean enabled;
    private boolean adaptiveGravity;
    private boolean preventSnapping;
    private float gravityMultiplier;
    private float leanMultiplier;
    private float spreadMultiplier;

    public ElytraPhysicsConfig(PlatformConfigValueProvider valueProvider) {
        this.valueProvider = valueProvider;
        this.cacheFields();
    }

    public void cacheFields() {
        // load elytra layers into set of classes for easier comparison
        List<String> injectLayersStrings = new ArrayList<>(this.valueProvider.getField(Spec.LAYER_INJECTORS.KEY));
        injectLayersStrings.add(WingsLayer.class.getName());

        this.elytraLayers = injectLayersStrings.stream().map((layerClassName) -> {
            try {
                Class<RenderLayer<?, ?>> clazz = (Class<RenderLayer<?, ?>>) Class.forName(layerClassName);
                LOGGER.info("Successfully added class '" + clazz.getName() + "' to layer inject list");

                return clazz;
            }
            catch (Exception e) {
                LOGGER.info("Class '" + layerClassName + "' not found");
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());

        // fetch other variables
        this.enabled = this.valueProvider.getField(Spec.ENABLED.KEY);
        this.preventSnapping = this.valueProvider.getField(Spec.PREVENT_SNAPPING.KEY);
        this.adaptiveGravity = this.valueProvider.getField(Spec.ADAPTIVE_GRAVITY.KEY);
        this.gravityMultiplier = (float) ((Double) this.valueProvider.getField(Spec.GRAVITY_MULTIPLIER.KEY)).doubleValue();
        this.leanMultiplier = (float) ((Double) this.valueProvider.getField(Spec.ELYTRA_LEAN_MULTIPLIER.KEY)).doubleValue();
        this.spreadMultiplier = (float) ((Double) this.valueProvider.getField(Spec.ELYTRA_SPREAD_MULTIPLIER.KEY)).doubleValue();

        // save mixin flags
        ElytraPhysicsMixinFlags.serialise(this.preventSnapping);
    }

    public Set<Class<RenderLayer<?, ?>>> getElytraLayers() {
        return this.elytraLayers;
    }
    public boolean getEnabled() {
        return this.enabled;
    }
    public boolean adaptiveGravityEnabled() {
        return this.adaptiveGravity;
    }
    public float getGravityMultiplier() {
        return this.gravityMultiplier;
    }
    public float getLeanMultiplier() {
        return this.leanMultiplier;
    }
    public float getSpreadMultiplier() {
        return this.spreadMultiplier;
    }

    public static class Spec {
        public static ListConfigField<String> LAYER_INJECTORS = new ListConfigField<>("inject_layers", String.class, () -> "");

        public static ConfigField<Boolean> ENABLED = new ConfigField<>("enabled", Boolean.class);
        public static ConfigField<Boolean> ADAPTIVE_GRAVITY = new ConfigField<>("adaptive_gravity", Boolean.class);
        public static ConfigField<Boolean> PREVENT_SNAPPING = new ConfigField<>("prevent_snapping", Boolean.class);

        public static ConfigField<Double> GRAVITY_MULTIPLIER = new ConfigField<>("gravity_multiplier", Double.class);
        public static ConfigField<Double> ELYTRA_LEAN_MULTIPLIER = new ConfigField<>("elytra_lean_multiplier", Double.class);
        public static ConfigField<Double> ELYTRA_SPREAD_MULTIPLIER = new ConfigField<>("elytra_spread_multiplier", Double.class);

        public static ConfigField<?>[] get() {
            return new ConfigField[] {LAYER_INJECTORS, ENABLED, ADAPTIVE_GRAVITY, PREVENT_SNAPPING, GRAVITY_MULTIPLIER, ELYTRA_LEAN_MULTIPLIER, ELYTRA_SPREAD_MULTIPLIER};
        }

    }
}
