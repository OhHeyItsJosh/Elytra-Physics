package me.ImJoshh.elytra_physics.config.ui;

import me.ImJoshh.elytra_physics.config.ui.widget.ListEntryList;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class ListEditSubScreen<T> extends OptionsSubScreen {

    private final static int HEADER_SIZE = 40;
    private final static int FOOTER_SIZE = 40;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, HEADER_SIZE, FOOTER_SIZE);

    private final List<T> values;
    private final Class<T> clazz;
    private Consumer<List<T>> onComplete;

    private ListEntryList<T> entryList;

    public ListEditSubScreen(Screen screen, List<T> values, Options options, Component component, Class<T> clazz) {
        super(screen, options, component);

        this.clazz = clazz;
        this.values = values;
    }

    public void onCompleteEdit(Consumer<List<T>> onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected void init() {
        super.init();

        // title
        this.layout.addToHeader(new StringWidget(this.title, this.font));

        // content
        this.entryList = this.addRenderableWidget(new ListEntryList<>(this.minecraft, this.values, this.clazz, this.width, this.height, HEADER_SIZE, this.height - FOOTER_SIZE));
        this.addWidget(this.entryList);

        // footer
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.onComplete.accept(this.entryList.getValues());
            this.minecraft.setScreen(this.lastScreen);
        }).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.entryList.updateSize(this.width, this.height, HEADER_SIZE, this.height - FOOTER_SIZE);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
//
//    @Override
//    protected void addOptions() {}
}
