package com.misterd.gosolar.blockentity.custom;

import com.misterd.gosolar.block.custom.EnergyReceiverBlock;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.network.GSWirelessNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyReceiverBlockEntity extends BlockEntity {

    public EnergyReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(GSBlockEntities.ENERGY_RECEIVER_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyReceiverBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
        if (network.getPool() <= 0) return;

        long transferRate = ((EnergyReceiverBlock) state.getBlock()).getTransferRate();

        for (Direction side : Direction.values()) {
            if (network.getPool() <= 0) break;
            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(side), side.getOpposite());
            if (storage == null || !storage.canReceive()) continue;
            int toInsert = (int) Math.min(transferRate, network.getPool());
            int accepted = storage.receiveEnergy(toInsert, false);
            if (accepted > 0) {
                network.removeFromPool(accepted);
            }
        }
    }
}