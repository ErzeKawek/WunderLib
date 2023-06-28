package de.ambertation.wunderlib.ui.layout.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import de.ambertation.wunderlib.ui.layout.components.render.NullRenderer;
import de.ambertation.wunderlib.ui.layout.values.Value;

@Environment(EnvType.CLIENT)
public class Empty extends LayoutComponent<NullRenderer, Empty> {
    public Empty(
            Value width,
            Value height
    ) {
        super(width, height, null);
    }

    @Override
    public int getContentWidth() {
        return 0;
    }

    @Override
    public int getContentHeight() {
        return 0;
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return false;
    }

    private boolean focused;

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean bl) {
        focused = bl;
    }
}
