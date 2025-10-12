package me.ImJoshh.elytra_physics.config.ui;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigFieldList;
import me.ImJoshh.elytra_physics.config.ui.widget.ConfigValue;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public abstract class ConfigScreen extends Screen {

    private final static Logger LOGGER = LogUtils.getLogger();

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 40, 40);
    private ConfigFieldList configList;

    Button saveButton;
    Button cancelButton;

    public ConfigScreen(Component component) {
        super(component);
    }

    public abstract ConfigValue<?, ?>[] getConfigEntries();
    public abstract void onSave(ConfigValue<?, ?>[] entries);

    protected void init() {
        ConfigValue<?, ?>[] entries = this.getConfigEntries();

        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout buttonRow = this.layout.addToFooter(LinearLayout.horizontal().spacing(4));

        this.cancelButton = buttonRow.addChild(Button.builder(Component.translatable("gui.cancel"), (button) -> {
            this.onClose();
        }).build());

        this.saveButton = buttonRow.addChild(Button.builder(Component.translatable("mco.configure.world.buttons.save"), this::saveClicked).build());

        this.configList = this.layout.addToContents(new ConfigFieldList(this.minecraft, entries));

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    private void saveClicked(Button button) {
        this.onSave(this.configList.getConfigValues());
        this.onClose();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.configList.updateSize(this.width, this.layout);
    }
}
