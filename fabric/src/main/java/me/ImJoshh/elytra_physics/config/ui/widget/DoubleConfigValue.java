package me.ImJoshh.elytra_physics.config.ui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubleConfigValue extends ConfigValue<EditBox, Double> {

    EditBox inputBox;

    Pattern validNumberMatcher = Pattern.compile("([0-9]|\\.)*");

    public DoubleConfigValue(String key, Double value, Double defaultValue, Minecraft minecraft) {
        super(key, defaultValue, minecraft);

        this.inputBox = new EditBox(this.minecraft.font, 0, 0, WIDTH, ConfigFieldList.ROW_HEIGHT, this.getFieldNarration());
        this.inputBox.setValue(value.toString());
//        this.inputBox.setFilter(this::isNumber);
        this.inputBox.setTooltip(Tooltip.create(this.getFieldTooltip()));
    }

    private boolean isNumber(String value) {
        Matcher matcher = validNumberMatcher.matcher(value);
        return value.isEmpty() || matcher.matches();
    }

    @Override
    EditBox getWidget() {
        return this.inputBox;
    }

    @Override
    public Double getValue() {
        try {
            return Double.parseDouble(this.inputBox.getValue());
        }
        catch (Exception e) {
            return this.defaultValue;
        }
    }

    @Override
    void resetDefault() {
        this.inputBox.setValue(this.defaultValue.toString());
    }
}
