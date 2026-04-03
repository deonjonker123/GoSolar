package com.misterd.gosolar.item.custom;

import com.misterd.gosolar.block.custom.BatteryBlock;
import com.misterd.gosolar.component.GSDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BatteryBlockItem extends BlockItem {

    public BatteryBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        long capacity = ((BatteryBlock) this.getBlock()).getCapacity();
        Long stored = stack.get(GSDataComponents.ENERGY_STORED.get());
        long energyStored = stored != null ? stored : 0L;

        tooltip.add(Component.literal(formatRF(energyStored) + " / " + formatRF(capacity) + " RF")
                .withStyle(ChatFormatting.GOLD));

        double pct = capacity > 0 ? (double) energyStored * 100.0D / (double) capacity : 0.0D;
        tooltip.add(Component.literal(String.format("%.1f%%", pct))
                .withStyle(ChatFormatting.GREEN));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        long capacity = ((BatteryBlock) this.getBlock()).getCapacity();
        Long stored = stack.get(GSDataComponents.ENERGY_STORED.get());
        long energyStored = stored != null ? stored : 0L;
        return capacity > 0 ? (int) (13.0D * energyStored / capacity) : 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        long capacity = ((BatteryBlock) this.getBlock()).getCapacity();
        Long stored = stack.get(GSDataComponents.ENERGY_STORED.get());
        long energyStored = stored != null ? stored : 0L;
        float pct = capacity > 0 ? (float) energyStored / capacity : 0.0F;
        int r = (int) (255 * (1.0F - pct));
        int g = (int) (255 * pct);
        return (r << 16) | (g << 8);
    }

    private static String formatRF(long value) {
        if (value >= 1_000_000_000L) return String.format("%.1fB", value / 1_000_000_000.0D);
        if (value >= 1_000_000L)     return String.format("%.1fM", value / 1_000_000.0D);
        if (value >= 1_000L)         return String.format("%.1fK", value / 1_000.0D);
        return String.valueOf(value);
    }
}