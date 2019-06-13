package me.desht.modularrouters.client.gui.widgets.button;

import me.desht.modularrouters.client.gui.ISendToServer;
import net.minecraft.client.gui.widget.button.Button;

import java.util.ArrayList;
import java.util.List;

public abstract class TexturedToggleButton extends TexturedButton implements ToggleButton {
    protected final List<String> tooltip2 = new ArrayList<>();
    private boolean toggled;

    public TexturedToggleButton(int x, int y, int width, int height, boolean toggled, ISendToServer dataSyncer) {
        super(x, y, width, height, button -> {
            ((TexturedToggleButton) button).toggle();
            if (dataSyncer != null) dataSyncer.sendToServer();
        });
        this.toggled = toggled;
    }

    public void toggle() {
        setToggled(!isToggled());
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    @Override
    public List<String> getTooltip() {
        return toggled ? tooltip2 : super.getTooltip();
    }
}
