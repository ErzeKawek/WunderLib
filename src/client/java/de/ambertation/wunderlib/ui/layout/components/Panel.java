package de.ambertation.wunderlib.ui.layout.components;


import de.ambertation.wunderlib.ui.layout.components.input.RelativeContainerEventHandler;
import de.ambertation.wunderlib.ui.layout.values.Rectangle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class Panel implements ComponentWithBounds, RelativeContainerEventHandler, NarratableEntry, Renderable {
    protected LayoutComponent<?, ?> child;
    List<? extends GuiEventListener> listeners = List.of();
    public final Rectangle bounds;

    public Panel(int width, int height) {
        this(0, 0, width, height);
    }

    public Panel(int left, int top, int width, int height) {
        this(new Rectangle(left, top, width, height));
    }

    public Panel(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setChild(LayoutComponent<?, ?> c) {
        this.child = c;
        listeners = List.of(c);
    }

    public void calculateLayout() {
        if (child != null) {
            child.updateContainerWidth(bounds.width);
            child.updateContainerHeight(bounds.height);
            child.setRelativeBounds(0, 0);
            child.updateScreenBounds(bounds.left, bounds.top);
        }
    }

    @Override
    public Rectangle getRelativeBounds() {
        return bounds;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return listeners;
    }

    @Override
    public Rectangle getInputBounds() {
        return bounds;
    }

    boolean dragging = false;

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean bl) {
        dragging = bl;
    }

    GuiEventListener focused;

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        focused = guiEventListener;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTicks) {
        if (child != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(bounds.left, bounds.top, 0);
            child.render(guiGraphics, mouseX - bounds.left, mouseY - bounds.top, deltaTicks, bounds, bounds);
            guiGraphics.pose().popPose();
        }
    }


}
