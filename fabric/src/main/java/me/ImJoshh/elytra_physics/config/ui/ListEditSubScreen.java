package me.ImJoshh.elytra_physics.config.ui;

import me.ImJoshh.elytra_physics.config.ui.widget.ListEntryList;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class ListEditSubScreen<T> extends OptionsSubScreen {

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
        this.addRenderableWidget(new ListEntryList<>(this.minecraft, this.values, this.clazz));
    }

    //    @Override
//    protected void addContents() {
//        this.entryList = this.layout.addToContents(new ListEntryList<>(this.minecraft, this.values, this.clazz));
//    }
//
//    @Override
//    protected void repositionElements() {
//        this.layout.arrangeElements();
//        this.entryList.updateSize(this.width, this.layout);
//    }

    @Override
    public void onClose() {
        super.onClose();

        this.onComplete.accept(this.entryList.getValues());
    }
//
//    @Override
//    protected void addOptions() {}
}
