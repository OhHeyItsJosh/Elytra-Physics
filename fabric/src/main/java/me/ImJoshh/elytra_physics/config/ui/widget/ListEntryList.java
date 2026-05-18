package me.ImJoshh.elytra_physics.config.ui.widget;

import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListEntryList<T> extends ContainerObjectSelectionList<ListEntryList.ConfigListEntry> {

    private final Function<T, ConfigListEntry> entryProvider;
    private final AddBtnListEntry addOption;

    public ListEntryList(Minecraft minecraft, List<T> values, Class<T> clazz) {
        super(minecraft, 0, 0 , 0, ConfigFieldList.ROW_HEIGHT + (ConfigFieldList.ROW_PADDING * 2));

        this.entryProvider = this.determineEntryProvider(clazz);

        this.addEntries(values);

        // add button
        this.addOption = new AddBtnListEntry(this::addNewEntry);
        this.addEntry(this.addOption);
    }

    public List<T> getValues() {
        return this.children().stream()
                .filter((entry) -> entry instanceof ListEntryList.HasValue<?>)
                .map((entry) -> ((HasValue<T>) entry).getValue())
                .collect(Collectors.toList());
    }

    private Function<T, ConfigListEntry> determineEntryProvider(Class<T> clazz) {
        if (clazz.equals(String.class))
            return (str) -> new StringListEntry((String) str, this::removeEntry);

        else {
            ElytraPhysicsClientMod.LOGGER.warn(String.format("%s config list type is not currently supported", clazz.getName()));
            return null;
        }
    }

    private void addEntries(List<T> values) {
        if (entryProvider == null)
            return;

        for (T value : values)
        {
            this.addEntry(entryProvider.apply(value));
        }
    }

    private void addNewEntry() {
        // goofy, but it works
        this.removeEntry(this.addOption);
        this.addEntry(entryProvider.apply(null));
        this.addEntry(this.addOption);
    }

    @Override
    public int getRowWidth() {
        return 300;
    }

    public class AddBtnListEntry extends ConfigListEntry {

        private final Button addButton;

        public AddBtnListEntry(Runnable callback) {
            this.addButton = Button
                    .builder(Component.literal("+"), (button) -> callback.run())
                    .size(ConfigFieldList.ROW_HEIGHT, ConfigFieldList.ROW_HEIGHT)
                    .createNarration((component) -> Component.translatable("elytra_physics.narrator.config_list_add_entry"))
                    .build();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float delta) {
            this.addButton.setPosition(this.getContentX() + (ListEntryList.this.getRowWidth() / 2) - (this.addButton.getWidth() / 2), this.getContentY() + ConfigFieldList.ROW_PADDING);
            this.addButton.extractRenderState(graphics, mouseX, mouseY, delta);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return List.of(this.addButton);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return List.of(this.addButton);
        }
    }

    public class StringListEntry extends ConfigListEntry implements HasValue<String> {

        private final EditBox valueEditBox;
        private final Button removeButton;

        public StringListEntry(String value, Consumer<ConfigListEntry> removeMe) {
            int editBoxWidth = ListEntryList.this.getRowWidth() - (ConfigFieldList.ROW_PADDING * 3) - ConfigFieldList.ROW_HEIGHT;
            this.valueEditBox = new EditBox(ListEntryList.this.minecraft.font, 0, 0, editBoxWidth, ConfigFieldList.ROW_HEIGHT, CommonComponents.ELLIPSIS);
            this.valueEditBox.setMaxLength(9999);
            this.valueEditBox.setValue(Objects.requireNonNullElse(value, ""));

            this.removeButton = Button
                    .builder(Component.literal("X"), (button) -> removeMe.accept(this))
                    .size(ConfigFieldList.ROW_HEIGHT, ConfigFieldList.ROW_HEIGHT)
                    .createNarration((component) -> Component.translatable("elytra_physics.narrator.config_list_delete_entry", Component.literal(this.valueEditBox.getValue())))
                    .build();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean bl, float delta) {
            int y = this.getContentY() + ConfigFieldList.ROW_PADDING;

            this.removeButton.setPosition(ListEntryList.this.scrollBarX() - ConfigFieldList.ROW_PADDING - this.removeButton.getWidth(), y);
            this.removeButton.extractRenderState(graphics, mouseX, mouseY, delta);

            this.valueEditBox.setPosition(this.getContentX() + ConfigFieldList.ROW_PADDING, y);
            this.valueEditBox.extractRenderState(graphics, mouseX, mouseY, delta);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return List.of(this.valueEditBox, this.removeButton);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return List.of(this.valueEditBox, this.removeButton);
        }

        @Override
        public String getValue() {
            return this.valueEditBox.getValue();
        }
    }

    public interface HasValue<T> {
        T getValue();
    }

    public abstract static class ConfigListEntry extends ContainerObjectSelectionList.Entry<ConfigListEntry> {

    }
}
