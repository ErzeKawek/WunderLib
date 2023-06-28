package de.ambertation.wunderlib.ui.layout.components;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;
import de.ambertation.wunderlib.ui.layout.values.Size;
import de.ambertation.wunderlib.ui.layout.values.Value;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ImageButton extends Image {
    public static final OnTooltip NO_TOOLTIP = (button, poseStack, i, j) -> {
    };
    public static final OnPress NO_ACTION = (button) -> {
    };

    @Environment(EnvType.CLIENT)
    public interface OnTooltip {
        void onTooltip(ImageButton button, GuiGraphics guiGraphics, int mouseX, int mouseY);
    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(ImageButton button);
    }

    public ImageButton(
            Value width,
            Value height,
            ResourceLocation location
    ) {
        super(width, height, location);
        this.onPress = NO_ACTION;
        this.onTooltip = NO_TOOLTIP;
    }

    public ImageButton(
            Value width,
            Value height,
            ResourceLocation location,
            Size resourceSize
    ) {
        super(width, height, location, resourceSize);
        this.onPress = NO_ACTION;
        this.onTooltip = NO_TOOLTIP;
    }

    OnPress onPress;
    OnTooltip onTooltip;

    public ImageButton onPress(OnPress onPress) {
        this.onPress = onPress;
        return this;
    }

    public ImageButton onToolTip(OnTooltip onTooltip) {
        this.onTooltip = onTooltip;
        return this;
    }

    public ImageButton setEnabled(boolean enabled) {
        setAlpha(enabled ? 1f : 0.5f);
        return this;
    }

    @Override
    public ImageButton setAlpha(float a) {
        return (ImageButton) super.setAlpha(a);
    }

    @Override
    public ImageButton setResourceSize(Size sz) {
        return (ImageButton) super.setResourceSize(sz);
    }

    @Override
    public ImageButton setResourceSize(int width, int height) {
        return (ImageButton) super.setResourceSize(width, height);
    }

    @Override
    public ImageButton setUvRect(Rectangle rect) {
        return (ImageButton) super.setUvRect(rect);
    }

    @Override
    public ImageButton setUvRect(int left, int top, int width, int height) {
        return (ImageButton) super.setUvRect(left, top, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int i) {
        if (getRelativeBounds().contains(mouseX, mouseY)) {
            onPress.onPress(this);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, i);
    }

    @Override
    protected void customRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float deltaTicks,
            Rectangle bounds,
            Rectangle clipRect
    ) {
        super.customRender(guiGraphics, mouseX, mouseY, deltaTicks, bounds, clipRect);
        if (getRelativeBounds().contains(mouseX, mouseY)) {
            onTooltip.onTooltip(this, guiGraphics, mouseX, mouseY);
        }
    }
}
