package com.misterd.gosolar.blockentity.custom;

import com.misterd.gosolar.block.custom.EnergyReceiverBlock;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.network.GSWirelessNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnergyReceiverBlockEntity extends BlockEntity {

    private static final String TAG_OWNER = "Owner";

    @Nullable
    private UUID ownerUUID = null;

    public EnergyReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(GSBlockEntities.ENERGY_RECEIVER_BE.get(), pos, state);
    }

    public void setOwner(UUID uuid) { this.ownerUUID = uuid; setChanged(); }
    @Nullable public UUID getOwnerUUID() { return ownerUUID; }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyReceiverBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel) || be.ownerUUID == null) return;

        GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);

        List<UUID> accessiblePools = new ArrayList<>();

        if (network.getPool(be.ownerUUID) > 0) {
            accessiblePools.add(be.ownerUUID);
        }

        Map<UUID, Long> publicPools = network.getPublicPools();
        publicPools.forEach((uuid, amount) -> {
            if (!uuid.equals(be.ownerUUID) && amount > 0) {
                accessiblePools.add(uuid);
            }
        });

        if (accessiblePools.isEmpty()) return;

        long transferRate = ((EnergyReceiverBlock) state.getBlock()).getTransferRate();
        long perPool = transferRate / accessiblePools.size();
        if (perPool <= 0) return;

        for (Direction side : Direction.values()) {
            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(side), side.getOpposite());
            if (storage == null || !storage.canReceive()) continue;

            for (UUID poolOwner : accessiblePools) {
                long available = network.getPool(poolOwner);
                if (available <= 0) continue;
                int toInsert = (int) Math.min(perPool, available);
                int accepted = storage.receiveEnergy(toInsert, false);
                if (accepted > 0) {
                    network.removeFromPool(poolOwner, accepted);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (ownerUUID != null) tag.putUUID(TAG_OWNER, ownerUUID);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.hasUUID(TAG_OWNER)) ownerUUID = tag.getUUID(TAG_OWNER);
    }
}