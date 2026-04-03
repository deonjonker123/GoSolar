package com.misterd.gosolar.gui.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import com.mojang.blaze3d.systems.RenderSystem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BatteryBlockScreen extends AbstractContainerScreen<BatteryBlockMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("gosolar", "textures/gui/battery_gui.png");

    public BatteryBlockScreen(BatteryBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 162;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 4;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        long energyStored = this.menu.getEnergyStored();
        long maxEnergy = this.menu.getMaxEnergyStored();

        if (maxEnergy > 0 && energyStored > 0) {
            int fillWidth = (int) (160.0D * (double) energyStored / (double) maxEnergy);
            if (fillWidth > 0) {
                guiGraphics.blit(GUI_TEXTURE, x + 8, y + 15, 0, 162, fillWidth, 34);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);

        long energyStored = this.menu.getEnergyStored();
        long maxEnergy = this.menu.getMaxEnergyStored();
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);

        Component rfText = Component.literal(fmt.format(energyStored) + " / " + fmt.format(maxEnergy) + " RF");
        int rfTextWidth = this.font.width(rfText);
        guiGraphics.drawString(this.font, rfText, 88 - rfTextWidth / 2, 24, 0xFFFFFF, true);

        if (maxEnergy > 0) {
            double pct = (double) energyStored * 100.0D / (double) maxEnergy;
            Component pctText = Component.literal(String.format("%.1f%%", pct));
            int pctWidth = this.font.width(pctText);
            guiGraphics.drawString(this.font, pctText, 88 - pctWidth / 2, 34, 0xAAAAAA, true);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
        long energyStored = this.menu.getEnergyStored();
        long maxEnergy = this.menu.getMaxEnergyStored();

        if (mouseX >= x + 7 && mouseX <= x + 169 && mouseY >= y + 14 && mouseY <= y + 50) {
            List<Component> tooltip = new ArrayList<>();
            double pct = maxEnergy > 0 ? (double) energyStored * 100.0D / (double) maxEnergy : 0.0D;
            tooltip.add(Component.literal("Energy Stored").withStyle(net.minecraft.ChatFormatting.GOLD));
            tooltip.add(Component.literal(fmt.format(energyStored) + " / " + fmt.format(maxEnergy) + " RF").withStyle(net.minecraft.ChatFormatting.WHITE));
            tooltip.add(Component.literal(String.format("%.1f%%", pct)).withStyle(net.minecraft.ChatFormatting.GRAY));
            guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }
}