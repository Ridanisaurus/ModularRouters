package me.desht.modularrouters.client.gui.widgets.button;

import me.desht.modularrouters.client.gui.ISendToServer;
import me.desht.modularrouters.logic.RouterRedstoneBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Collections;
import java.util.List;

public class RedstoneBehaviourButton extends TexturedCyclerButton<RouterRedstoneBehaviour> {
    public RedstoneBehaviourButton(int x, int y, int width, int height, RouterRedstoneBehaviour initialVal, ISendToServer dataSyncer) {
        super(x, y, width, height, initialVal, dataSyncer);
    }

    @Override
    protected int getTextureX() {
        return 16 * getState().ordinal();
    }

    @Override
    protected int getTextureY() {
        return 16;
    }

    @Override
    public List<Component> getTooltip() {
        return Collections.singletonList(
                // TODO 1.16 append = appendSibling
                new TranslatableComponent("modularrouters.guiText.tooltip.redstone.label")
                        .append(new TextComponent(": "))
                        .append(new TranslatableComponent(getState().getTranslationKey()))
        );
    }
}
