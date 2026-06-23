package me.ImJoshh.elytra_physics.config.ui;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.ElytraPhysics;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import me.ImJoshh.elytra_physics.config.PlatformConfigBridge;
import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import me.ImJoshh.elytra_physics.config.ui.widget.BooleanConfigValue;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;
import me.ImJoshh.elytra_physics.config.ui.widget.DoubleConfigValue;
import me.ImJoshh.elytra_physics.config.ui.widget.StringListConfigValue;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ElytraPhysicsConfigScreen extends ConfigScreen {

    public static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsConfigScreen() {
        super(Component.translatable("elytra_physics.configuration.title"));
    }

    @Override
    public ConfigValue<?, ?>[] getConfigEntries() {
        return Arrays.stream(ElytraPhysicsConfig.Spec.get())
                .map(this::resolveConfigField)
                .filter(Objects::nonNull)
                .toArray(ConfigValue[]::new);
    }

    private <T> ConfigValue<?, ?> resolveConfigField(ConfigField<T> field) {
        PlatformConfigBridge configBridge = ElytraPhysics.getConfig().configBridge;
        T configValue =  configBridge.getFieldValue(field.KEY);
        if (configValue == null) {
            LOGGER.warn(String.format("Config field '%s' could not find value", field.KEY));
            return null;
        }

        if (field instanceof ListConfigField<?> && field.getFieldType().equals(String.class)) {
            return new StringListConfigValue(field.KEY, (List<String>) configValue, configBridge.getFieldDefaultValue(field.KEY), this.minecraft, this);
        }
        else if (field.getFieldType().equals(Double.class)) {
            return new DoubleConfigValue(field.KEY, (Double) configValue, configBridge.getFieldDefaultValue(field.KEY), this.minecraft);
        }
        else if (field.getFieldType().equals(Boolean.class)) {
            return new BooleanConfigValue(field.KEY, (Boolean) configValue, configBridge.getFieldDefaultValue(field.KEY), this.minecraft);
        }
        else {
            return null;
        }
    }

    @Override
    public void onSave(ConfigValue<?, ?>[] entries) {
        LOGGER.info("attempting to save config changes...");
        ElytraPhysics.getConfig().configBridge.saveConfig(entries);
    }
}
