package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.block.custom.SolarPanelBlock;
import com.misterd.gosolar.blockentity.custom.SolarPanelBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.NumberFormat;
import java.util.Locale;

public enum SolarPanelProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("gosolar", "solar_panel_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        long rfPerTick = data.getLong("rfPerTick");
        boolean powered = data.getBoolean("powered");

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);

        tooltip.add(Component.literal(fmt.format(rfPerTick) + " RF/t")
                .withStyle(ChatFormatting.YELLOW));

        if (powered) {
            tooltip.add(Component.literal("Generating")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.literal("Not Generating")
                    .withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (be instanceof SolarPanelBlockEntity) {
            BlockState state = accessor.getBlockState();
            long rfPerTick = ((SolarPanelBlock) state.getBlock()).getRfPerTick();
            boolean powered = state.getValue(SolarPanelBlock.POWERED);
            data.putLong("rfPerTick", rfPerTick);
            data.putBoolean("powered", powered);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}