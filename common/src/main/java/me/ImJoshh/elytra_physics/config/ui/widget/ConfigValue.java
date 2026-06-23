package me.ImJoshh.elytra_physics.config.ui.widget;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class ConfigValue<T extends AbstractWidget, M> {

    public static int WIDTH = 100;

    protected final Minecraft minecraft;
    protected final String key;
    protected final M defaultValue;

    public ConfigValue(String key, M defaultValue, Minecraft minecraft) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.minecraft = minecraft;
    }

    abstract T getWidget();
    abstract public M getValue();
    abstract void resetDefault();

    public String getKey() {
        return this.key;
    }

    public boolean isDefault() {
        return this.getValue().equals(this.defaultValue);
    }

    public Component getFieldName() {
        return Component.translatable(String.format("%s.configuration.%s", ElytraPhysics.MOD_ID, this.getKey()));
    }

    public Component getFieldTooltip() {
        return Component.translatable(String.format("%s.configuration.%s.tooltip", ElytraPhysics.MOD_ID, this.getKey()));
    }

    public MutableComponent getFieldNarration() {
        return Component.translatable("elytra_physics.narrator.config_option", this.getFieldName());
    }

    public MutableComponent getFieldAndValueNarration() {
        return this.getFieldAndValueNarration(Component.literal(this.getValue().toString()));
    }

    public MutableComponent getFieldAndValueNarration(Component valueNarration) {
        return  Component.translatable("elytra_physics.narrator.config_option_with_value", this.getFieldName(), valueNarration);
    }
}
