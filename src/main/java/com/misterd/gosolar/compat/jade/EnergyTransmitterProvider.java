package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.blockentity.custom.EnergyTransmitterBlockEntity;
import com.misterd.gosolar.network.GSWirelessNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public enum EnergyTransmitterProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("gosolar", "energy_transmitter_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        long poolStored = data.getLong("poolStored");
        long maxPool = data.getLong("maxPool");
        boolean isPublic = data.getBoolean("isPublic");

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
        double pct = maxPool > 0 ? (double) poolStored * 100.0D / (double) maxPool : 0.0D;

        tooltip.add(Component.literal(fmt.format(poolStored) + " / " + fmt.format(maxPool) + " RF")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal(String.format("%.1f%%", pct))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(isPublic
                ? Component.translatable("gui.gosolar.transmitter_toggle_public").withStyle(ChatFormatting.GREEN)
                : Component.translatable("gui.gosolar.transmitter_toggle_private").withStyle(ChatFormatting.RED));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (be instanceof EnergyTransmitterBlockEntity transmitter && accessor.getLevel() instanceof ServerLevel serverLevel) {
            UUID ownerUUID = transmitter.getOwnerUUID();
            GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
            long poolStored = ownerUUID != null ? network.getPool(ownerUUID) : 0L;
            data.putLong("poolStored", poolStored);
            data.putLong("maxPool", GSWirelessNetwork.MAX_POOL);
            data.putBoolean("isPublic", transmitter.isPublic());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}