package me.ImJoshh.elytra_physics.config.ui.widget;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class BooleanConfigValue extends ConfigValue<CycleButton<Boolean>, Boolean> {

    private final CycleButton<Boolean> toggleButton;

    public BooleanConfigValue(String key, Boolean value, Boolean defaultValue, Minecraft minecraft) {
        super(key, defaultValue, minecraft);

        CycleButton.Builder<Boolean> builder = CycleButton.builder((val) -> {
            if (val) return CommonComponents.GUI_YES.copy().setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
            else return CommonComponents.GUI_NO.copy().setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
        });

        this.toggleButton = builder
                .withValues(OptionInstance.BOOLEAN_VALUES.valueListSupplier())
                .withInitialValue(value)
                .displayOnlyValue()
                .withCustomNarration((component) -> this.getFieldAndValueNarration(component.getValue() ? CommonComponents.GUI_YES : CommonComponents.GUI_NO))
                .withTooltip((val) -> Tooltip.create(this.getFieldTooltip()))
                .create(0, 0, WIDTH, ConfigFieldList.ROW_HEIGHT, Component.literal(""), (btn, val) -> {});
    }

    @Override
    public CycleButton<Boolean> getWidget() {
        return this.toggleButton;
    }

    @Override
    public Boolean getValue() {
        return this.toggleButton.getValue();
    }

    @Override
    public void resetDefault() {
        this.toggleButton.setValue(this.defaultValue);
    }
}
