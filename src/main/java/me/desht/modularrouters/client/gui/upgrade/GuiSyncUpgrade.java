package me.desht.modularrouters.client.gui.upgrade;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.client.gui.widgets.GuiScreenBase;
import me.desht.modularrouters.client.gui.widgets.button.ItemStackButton;
import me.desht.modularrouters.client.gui.widgets.textfield.IntegerTextField;
import me.desht.modularrouters.client.gui.widgets.textfield.TextFieldManager;
import me.desht.modularrouters.config.ConfigHandler;
import me.desht.modularrouters.item.upgrade.SyncUpgrade;
import me.desht.modularrouters.network.PacketHandler;
import me.desht.modularrouters.network.SyncUpgradeSettingsMessage;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSyncUpgrade extends GuiScreenBase {
    private static final ResourceLocation textureLocation = new ResourceLocation(ModularRouters.MODID, "textures/gui/sync_upgrade.png");
    private static final ItemStack clockStack = new ItemStack(Items.CLOCK);
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 48;

    private final String title;

    private int xPos, yPos;
    private int tunedValue;
    private final Hand hand;

    public GuiSyncUpgrade(ItemStack upgradeStack, Hand hand) {
        super(upgradeStack.getDisplayName());

        this.title = upgradeStack.getDisplayName().getString();
        this.tunedValue = SyncUpgrade.getTunedValue(upgradeStack);
        this.hand = hand;
    }

    @Override
    public void init() {
        xPos = (width - GUI_WIDTH) / 2;
        yPos = (height - GUI_HEIGHT) / 2;

        TextFieldManager manager = getTextFieldManager().clear();
        IntegerTextField intField = new IntegerTextField(manager, font,
                xPos + 77, yPos + 27, 25, 16, 0, ConfigHandler.ROUTER.baseTickRate.get() - 1);
        intField.func_212954_a((str) -> {
            tunedValue = str.isEmpty() ? 0 : Integer.parseInt(str);
            sendSettingsDelayed(5);
        });
        intField.setValue(tunedValue);
        intField.useGuiTextBackground();

        addButton(new TooltipButton(xPos + 55, yPos + 24, 16, 16));

        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(textureLocation);
        blit(xPos, yPos, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        font.drawString(title, xPos + GUI_WIDTH / 2f - font.getStringWidth(title) / 2f, yPos + 6, 0x404040);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void sendSettingsToServer() {
        PacketHandler.NETWORK.sendToServer(new SyncUpgradeSettingsMessage(tunedValue, hand));
    }

    private static class TooltipButton extends ItemStackButton {
        TooltipButton(int x, int y, int width, int height) {
            super(x, y, width, height, clockStack, true, p -> {});
            MiscUtil.appendMultiline(tooltip1, "guiText.tooltip.tunedValue", 0, ConfigHandler.ROUTER.baseTickRate.get() - 1);
            MiscUtil.appendMultiline(tooltip1, "guiText.tooltip.numberFieldTooltip");
        }

        @Override
        public void playDownSound(SoundHandler soundHandlerIn) {
            // no sound
        }
    }
}
