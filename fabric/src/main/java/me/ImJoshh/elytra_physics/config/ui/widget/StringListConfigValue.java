package me.ImJoshh.elytra_physics.config.ui.widget;

import me.ImJoshh.elytra_physics.config.ui.ListEditSubScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

// TODO : generify
public class StringListConfigValue extends ConfigValue<Button, List<String>> {

    Button editButton;
    List<String> values;

    Screen parent;

    public StringListConfigValue(String key, List<String> value, List<String> defaultValue, Minecraft minecraft, Screen parentScreen) {
        super(key, defaultValue, minecraft);

        this.parent = parentScreen;

        this.values = value;
        this.editButton = Button
                .builder(CommonComponents.ELLIPSIS, this::showListEditScreen)
                .size(WIDTH, ConfigFieldList.ROW_HEIGHT)
                .createNarration((component) -> this.getFieldNarration())
                .tooltip(Tooltip.create(this.getFieldTooltip()))
                .build();

        this.updateButtonText();
    }

    private void showListEditScreen(Button button) {
        ListEditSubScreen<String> editScreen = new ListEditSubScreen<>(this.parent, this.values, this.minecraft.options, this.getFieldName(), String.class);
        editScreen.onCompleteEdit((newValues) -> {
            this.values = newValues;
            this.updateButtonText();
        });

        this.minecraft.setScreen(editScreen);
    }

    private void updateButtonText() {
        this.editButton.setMessage(Component.translatable("elytra_physics.configuration.components.list_edit", Component.literal(String.valueOf(this.values.size()))));
    }

    @Override
    Button getWidget() {
        return this.editButton;
    }

    @Override
    public List<String> getValue() {
        return this.values;
    }

    @Override
    void resetDefault() {
        this.values = this.defaultValue;
        this.updateButtonText();
    }
}
