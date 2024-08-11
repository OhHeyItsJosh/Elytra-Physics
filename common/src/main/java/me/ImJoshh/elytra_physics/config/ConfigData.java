package me.ImJoshh.elytra_physics.config;

import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.HashSet;
import java.util.Set;

public class ConfigData {

    private static Set<Class<RenderLayer<?, ?>>> s_shouldInjectlayers = new HashSet<>();
    public static Set<Class<RenderLayer<?, ?>>> getLayersToInject() {
        return s_shouldInjectlayers;
    }

    public static void addLayerToInject(Class<RenderLayer<?, ?>> layer) {
        s_shouldInjectlayers.add(layer);
    }
}