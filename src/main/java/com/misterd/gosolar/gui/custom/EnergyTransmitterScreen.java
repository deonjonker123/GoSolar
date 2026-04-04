package com.misterd.gosolar.gui.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnergyTransmitterScreen extends AbstractContainerScreen<EnergyTransmitterMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("gosolar", "textures/gui/transmitter_gui.png");

    public EnergyTransmitterScreen(EnergyTransmitterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 162;
        this.inventoryLabelY = Integer.MAX_VALUE;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        long poolStored = this.menu.getPoolStored();
        long maxPool = this.menu.getMaxPool();

        if (maxPool > 0 && poolStored > 0) {
            int fillWidth = (int) (139.0D * (double) poolStored / (double) maxPool);
            if (fillWidth > 0) {
                guiGraphics.blit(GUI_TEXTURE, x + 6, y + 24, 0, 162, fillWidth, 37);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);

        long poolStored = this.menu.getPoolStored();
        long maxPool = this.menu.getMaxPool();

        Component rfText = Component.literal(formatRF(poolStored) + " / " + formatRF(maxPool) + " RF");
        int rfTextWidth = this.font.width(rfText);
        guiGraphics.drawString(this.font, rfText, 75 - rfTextWidth / 2, 34, 0xFFFFFF, true);

        if (maxPool > 0) {
            double pct = (double) poolStored * 100.0D / (double) maxPool;
            Component pctText = Component.literal(String.format("%.1f%%", pct));
            int pctWidth = this.font.width(pctText);
            guiGraphics.drawString(this.font, pctText, 75 - pctWidth / 2, 44, 0xAAAAAA, true);
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
        long poolStored = this.menu.getPoolStored();
        long maxPool = this.menu.getMaxPool();

        if (mouseX >= x + 6 && mouseX <= x + 145 && mouseY >= y + 24 && mouseY <= y + 61) {
            List<Component> tooltip = new ArrayList<>();
            double pct = maxPool > 0 ? (double) poolStored * 100.0D / (double) maxPool : 0.0D;
            tooltip.add(Component.translatable("gui.gosolar.transmitter_pool_title").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.literal(fmt.format(poolStored) + " / " + fmt.format(maxPool) + " RF").withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.literal(String.format("%.1f%%", pct)).withStyle(ChatFormatting.GRAY));
            guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    private static String formatRF(long value) {
        if (value >= 1_000_000_000L) return String.format("%.1fB", value / 1_000_000_000.0D);
        if (value >= 1_000_000L)     return String.format("%.1fM", value / 1_000_000.0D);
        if (value >= 1_000L)         return String.format("%.1fK", value / 1_000.0D);
        return String.valueOf(value);
    }
}