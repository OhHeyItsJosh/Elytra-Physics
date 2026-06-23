package me.ImJoshh.elytra_physics.config;

import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;

public interface PlatformConfigBridge {
    <T> T getFieldValue(String key);
    <T> T getFieldDefaultValue(String key);
    void saveConfig(ConfigValue<?, ?>[] entries);
}
