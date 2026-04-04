package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.block.custom.EnergyReceiverBlock;
import com.misterd.gosolar.blockentity.custom.EnergyReceiverBlockEntity;
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

public enum EnergyReceiverProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("gosolar", "energy_receiver_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        long transferRate = data.getLong("transferRate");

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);

        tooltip.add(Component.literal(fmt.format(transferRate) + " RF/t")
                .withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (be instanceof EnergyReceiverBlockEntity) {
            long transferRate = ((EnergyReceiverBlock) accessor.getBlockState().getBlock()).getTransferRate();
            data.putLong("transferRate", transferRate);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}