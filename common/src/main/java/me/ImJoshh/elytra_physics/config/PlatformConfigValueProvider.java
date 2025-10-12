package me.ImJoshh.elytra_physics.config;

@FunctionalInterface
public interface PlatformConfigValueProvider {
    <T> T getField(String key);
}
