package me.ImJoshh.elytra_physics.config.ui;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import me.ImJoshh.elytra_physics.config.FabricConfig;
import me.ImJoshh.elytra_physics.config.field.ConfigField;
import me.ImJoshh.elytra_physics.config.field.ListConfigField;
import me.ImJoshh.elytra_physics.config.ui.widget.BooleanConfigValue;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;
import me.ImJoshh.elytra_physics.config.ui.widget.DoubleConfigValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ElytraPhysicsConfigScreen extends ConfigScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ElytraPhysicsConfigScreen(Screen parent) {
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
        T configValue = FabricConfig.getConfigValue(field.KEY);
        if (configValue == null) {
            LOGGER.warn(String.format("Config field '%s' could not find value", field.KEY));
            return null;
        }

        if (field instanceof ListConfigField<?> listField) {
            // TODO : implement
            return null;
        }
        else if (field.getFieldType().equals(Double.class)) {
            return new DoubleConfigValue(field.KEY, (Double) configValue, FabricConfig.getDefaultValue(field.KEY), this.minecraft);
        }
        else if (field.getFieldType().equals(Boolean.class)) {
            return new BooleanConfigValue(field.KEY, (Boolean) configValue, FabricConfig.getDefaultValue(field.KEY), this.minecraft);
        }
        else {
            return null;
        }
    }

    @Override
    public void onSave(ConfigValue<?, ?>[] entries) {
        ElytraPhysicsClientMod.LOGGER.info("attempting to save config changes...");

        FabricConfig.attemptSaveOperation((configFile) -> {
            for (ConfigValue<?, ?> entry : entries)
            {
                configFile.set(entry.getKey(), entry.getValue());
            }
        });
    }
}
