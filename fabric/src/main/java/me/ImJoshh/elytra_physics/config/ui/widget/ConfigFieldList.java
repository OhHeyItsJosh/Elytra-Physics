package me.ImJoshh.elytra_physics.config.ui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigFieldList extends ContainerObjectSelectionList<ConfigFieldList.ConfigFieldEntry> {

    public static int ROW_HEIGHT = 20;
    public static int ROW_PADDING = 2;
    public ConfigFieldList(Minecraft minecraft, ConfigValue<?, ?>[] entries) {
        super(minecraft, 0, 0 , 0, ROW_HEIGHT + (ROW_PADDING * 2));

        for (ConfigValue<?, ?> entry : entries)
        {
            this.addEntry(new ConfigFieldEntry(entry, this));
        }
    }

    public ConfigValue<?, ?>[] getConfigValues() {
        return this.children().stream().map(ConfigFieldEntry::getConfigValue).toArray(ConfigValue[]::new);
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    public static class ConfigFieldEntry extends ContainerObjectSelectionList.Entry<ConfigFieldEntry> {

        final static int CONTENT_PADDING = 8;

        private final ConfigFieldList parent;

        private final Button resetButton;
        private final StringWidget displayText;

        private final WidgetTooltipHolder tooltipHolder = new WidgetTooltipHolder();
        private final Tooltip tooltip;

        ConfigValue<?, ?> configValue;

        public ConfigFieldEntry(ConfigValue<?, ?> configValue, ConfigFieldList parent) {
            this.configValue = configValue;
            this.parent = parent;

            this.resetButton = Button.builder(Component.translatable("controls.reset"), (button) -> this.configValue.resetDefault())
                    .bounds(0,0, 50, ConfigFieldList.ROW_HEIGHT)
                    .createNarration((component) -> Component.translatable("narrator.controls.reset", this.configValue.getFieldName()))
                    .build();

            // create label
            int maxTextWidth = this.parent.getRowWidth() - (CONTENT_PADDING * 4) - this.resetButton.getWidth() - ConfigValue.WIDTH;
            this.displayText = new StringWidget(this.configValue.getFieldName(), this.parent.minecraft.font);
            this.displayText.setMaxWidth(maxTextWidth);

            // create tooltip
            this.tooltip = Tooltip.create(this.configValue.getFieldTooltip());
            this.tooltipHolder.set(this.tooltip);
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float delta) {
            int y = this.getContentY() + ConfigFieldList.ROW_PADDING;

            // render reset button
            this.resetButton.active = !this.configValue.isDefault();
            this.resetButton.setPosition(this.parent.scrollBarX() - this.resetButton.getWidth() - CONTENT_PADDING, y);

            this.resetButton.extractRenderState(graphics, mouseX, mouseY, delta);

            // render label
            Font font = this.parent.minecraft.font;
            int textVerticalOffset = (ConfigFieldList.ROW_HEIGHT - font.lineHeight) / 2;

            this.displayText.setPosition(this.getContentX() + CONTENT_PADDING, y + textVerticalOffset);
            this.displayText.extractRenderState(graphics, mouseX, mouseY, delta);

            // render input widget
            AbstractWidget widget = this.configValue.getWidget();
            widget.setPosition(this.parent.scrollBarX() - (CONTENT_PADDING * 2) - this.resetButton.getWidth() - widget.getWidth(), y);
            widget.extractRenderState(graphics, mouseX, mouseY, delta);

            // tooltip
            this.tooltipHolder.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, this.isFocused(), this.getRectangle());
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return List.of(this.configValue.getWidget(), this.resetButton);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return List.of(this.configValue.getWidget(), this.resetButton);
        }

        public ConfigValue<?, ?> getConfigValue() {
            return this.configValue;
        }
    }
}