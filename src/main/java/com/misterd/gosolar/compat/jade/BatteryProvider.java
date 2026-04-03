package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.blockentity.custom.BatteryBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.NumberFormat;
import java.util.Locale;

public enum BatteryProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("gosolar", "battery_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        long energyStored = data.getLong("energyStored");
        long capacity = data.getLong("capacity");
        long transferRate = data.getLong("transferRate");

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
        double pct = capacity > 0 ? (double) energyStored * 100.0D / (double) capacity : 0.0D;

        tooltip.add(Component.literal(String.format("%.1f%%", pct))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(fmt.format(transferRate) + " RF/t transfer rate")
                .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (be instanceof BatteryBlockEntity battery) {
            data.putLong("capacity", battery.getCapacity());
            data.putLong("transferRate", battery.getTransferRate());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}