package me.ImJoshh.elytra_physics.config;

import me.ImJoshh.elytra_physics.ElytraPhysicsNeoForge;
import me.ImJoshh.elytra_physics.mixinFlagSystem.ElytraPhysicsMixinFlags;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;
import java.util.stream.Collectors;

public class NeoForgeConfigAccessor implements ElytraPhysicsConfig {

    private final Set<Class<RenderLayer<?, ?>>> elytraLayers;
    private final boolean adaptiveGravity;
    private final boolean preventSnapping;
    private final float gravityMultiplier;
    private final float leanMultiplier;
    private final float spreadMultiplier;

    @SuppressWarnings("unchecked")
    public NeoForgeConfigAccessor()
    {
        // load elytra layers into set of classes for easier comparison
        List<String> injectLayersStrings = new ArrayList<>(((ModConfigSpec.ConfigValue<List<String>>) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.LAYER_INJECTORS.KEY)).get());
        injectLayersStrings.add(WingsLayer.class.getName());

        this.elytraLayers = injectLayersStrings.stream().map((layerClassName) -> {
            try {
                Class<RenderLayer<?, ?>> clazz = (Class<RenderLayer<?, ?>>) Class.forName(layerClassName);
                ElytraPhysicsNeoForge.LOGGER.info("Successfully added class '" + clazz.getName() + "' to layer inject list");

                return clazz;
            }
            catch (Exception e) {
                ElytraPhysicsNeoForge.LOGGER.info("Class '" + layerClassName + "' not found");
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());

        // fetch other variables
        this.preventSnapping = (boolean) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.PREVENT_SNAPPING.KEY).get();
        this.adaptiveGravity = (boolean) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.ADAPTIVE_GRAVITY.KEY).get();
        this.gravityMultiplier = (float) ((Double) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.GRAVITY_MULTIPLIER.KEY).get()).doubleValue();
        this.leanMultiplier = (float) ((Double) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.ELYTRA_LEAN_MULTIPLIER.KEY).get()).doubleValue();
        this.spreadMultiplier = (float) ((Double) NeoForgeConfig.VALUE_ACCESSORS.get(Spec.ELYTRA_SPREAD_MULTIPLIER.KEY).get()).doubleValue();

        // save mixin flags
        ElytraPhysicsMixinFlags.serialise(this.preventSnapping);
    }

    @Override
    public Set<Class<RenderLayer<?, ?>>> getElytraLayers() {
        return this.elytraLayers;
    }

    @Override
    public boolean adaptiveGravityEnabled() {
        return this.adaptiveGravity;
    }

    @Override
    public boolean preventSnappingEnabled() {
        return this.preventSnapping;
    }

    @Override
    public float getGravityMultiplier() {
        return this.gravityMultiplier;
    }

    @Override
    public float getLeanMultiplier() {
        return this.leanMultiplier;
    }

    @Override
    public float getSpreadMultiplier() {
        return this.spreadMultiplier;
    }
}
