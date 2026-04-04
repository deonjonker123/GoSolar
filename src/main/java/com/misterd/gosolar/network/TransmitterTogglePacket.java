package com.misterd.gosolar.network;

import com.misterd.gosolar.blockentity.custom.EnergyTransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TransmitterTogglePacket(BlockPos pos, boolean isPublic) implements CustomPacketPayload {

    public static final Type<TransmitterTogglePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("gosolar", "transmitter_toggle")
    );

    public static final StreamCodec<FriendlyByteBuf, TransmitterTogglePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, TransmitterTogglePacket::pos,
            ByteBufCodecs.BOOL, TransmitterTogglePacket::isPublic,
            TransmitterTogglePacket::new
    );

    @Override
    public Type<TransmitterTogglePacket> type() {
        return TYPE;
    }

    public static void handle(TransmitterTogglePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            BlockEntity be = serverPlayer.level().getBlockEntity(packet.pos());
            if (!(be instanceof EnergyTransmitterBlockEntity transmitter)) return;

            if (!serverPlayer.getUUID().equals(transmitter.getOwnerUUID())) return;

            transmitter.setPublic(packet.isPublic());
        });
    }
}