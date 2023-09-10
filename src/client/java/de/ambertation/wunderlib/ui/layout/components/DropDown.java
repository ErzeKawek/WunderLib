package de.ambertation.wunderlib.ui.layout.components;

import de.ambertation.wunderlib.ui.ColorHelper;
import de.ambertation.wunderlib.ui.layout.values.Value;
import de.ambertation.wunderlib.ui.vanilla.LayoutScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DropDown<T> extends Button {
    private final static Component EMPTY = Component.literal("---");

    public interface OnChange<T> {
        void now(DropDown<T> dropDown, T value);
    }

    Panel panel;
    private final VerticalStack items;
    private Component valueComponent;
    private T currentValue;

    private final List<ValueItem<T>> values = new ArrayList<>(4);

    private OnChange<T> onChange;

    public DropDown(
            Value width,
            Value height
    ) {
        super(width, height, EMPTY);
        this.onPress = this::onPress;

        this.items = new VerticalStack(Value.fit(), Value.fit());
    }

    public void close() {
        if (panel != null) {
            panel = null;
            parentPanel.parentScreen.setOverlayProvider(null);
            didClose();
        }
    }

    private void didClose() {
        onBoundsChanged();
    }

    private void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private record ValueItem<T>(
            Component title,
            T value,
            LayoutComponent<?, ?> component,
            Consumer<T> callback) {

    }

    public DropDown<T> onChange(OnChange<T> onChange) {
        this.onChange = onChange;
        return this;
    }

    public DropDown<T> addOption(Component title, T value) {
        return addOption(title, value, null);
    }

    public DropDown<T> addOption(Component title, T value, @Nullable Consumer<T> callback) {
        final var component = new Text(Value.fit(), Value.fixed(20), title) {
            @Override
            public boolean mouseClicked(double d, double e, int i) {
                if (this.relativeBounds.contains(d, e)) {
                    playDownSound(Minecraft.getInstance().getSoundManager());
                    select(value, true);
                    close();
                    return true;
                }
                return false;
            }
        };

        ValueItem<T> item = new ValueItem<>(title, value, component, callback);
        values.add(item);
        items.add(item.component);

        if (valueComponent == null) select(value, false);
        return this;
    }

    public boolean select(T value) {
        return select(value, true);
    }

    public boolean select(T value, boolean fireEvent) {
        for (ValueItem<T> item : values) {
            if (item.value.equals(value)) {
                valueComponent = item.title;
                if (currentValue != value) {
                    currentValue = value;
                    if (fireEvent && item.callback != null) item.callback.accept(item.value);
                    if (fireEvent && onChange != null) onChange.now(this, value);
                }
                return true;
            }
        }

        valueComponent = null;
        if (currentValue != null) {
            currentValue = null;
            if (fireEvent && onChange != null) onChange.now(this, null);
        }
        return false;
    }

    public T selectedOption() {
        return currentValue;
    }

    @Override
    protected Component contentComponent() {
        if (valueComponent == null)
            return EMPTY;
        return valueComponent;
    }

    private void onPress(Button button) {
        if (panel == null) {
            final int PADDING = 1;
            panel = new Panel(parentPanel.parentScreen) {
                @Override
                public boolean mouseClicked(double d, double e, int i) {
                    if (!super.mouseClicked(d, e, i)) {
                        close();
                    }
                    return true;
                }
            };
            Container backdrop = new Container(Value.fill(), Value.fill());
            backdrop.setBackgroundColor(ColorHelper.CONTAINER_BACKGROUND);
            panel.setChild(backdrop).setZIndex(parentPanel.getZIndex() + 50);

            //height calculation
            items.calculateLayoutInParent(parentPanel);
            int height = items.getScreenBounds().height;
            if (screenBounds.bottom() + height > parentPanel.parentScreen.height) {
                height = Math.max(30, parentPanel.parentScreen.height - screenBounds.bottom() - 3 * PADDING);
            }

            Container contentContainer = Container.create(
                    Value.fit(),
                    Value.fixed(height + 2 * PADDING),
                    VerticalScroll.create(items)
            );
            contentContainer.setOutlineColor(ColorHelper.OVERLAY_BORDER);
            contentContainer.setBackgroundColor(ColorHelper.OVERLAY_BACKGROUND);
            contentContainer.setPadding(PADDING + 4, PADDING, PADDING, PADDING);

            backdrop.addChild(screenBounds.left + 10, screenBounds.bottom(), contentContainer);


            panel.calculateLayout();
            parentPanel.parentScreen.setOverlayProvider(new LayoutScreen.OverlayProvider() {
                @Override
                public @NotNull Panel getOverlay() {
                    return panel;
                }

                @Override
                public void willRemoveOverlay() {
                    parentPanel.setInputEnabled(true);
                    panel = null;
                    didClose();
                }
            });

            parentPanel.setInputEnabled(false);
        }
    }
}
