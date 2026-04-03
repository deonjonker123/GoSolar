package com.misterd.gosolar.blockentity.custom;

import com.misterd.gosolar.block.custom.SolarPanelBlock;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class SolarPanelBlockEntity extends BlockEntity {

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(GSBlockEntities.SOLAR_PANEL_BE.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() {
        long rfPerTick = ((SolarPanelBlock) getBlockState().getBlock()).getRfPerTick();
        return new IEnergyStorage() {
            @Override public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }
            @Override public int extractEnergy(int maxExtract, boolean simulate) {
                return (int) Math.min(maxExtract, rfPerTick);
            }
            @Override public int getEnergyStored() { return (int) rfPerTick; }
            @Override public int getMaxEnergyStored() { return (int) rfPerTick; }
            @Override public boolean canExtract() { return true; }
            @Override public boolean canReceive() { return false; }
        };
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        boolean powered = isGenerating(serverLevel, pos);

        if (powered != state.getValue(SolarPanelBlock.POWERED)) {
            level.setBlockAndUpdate(pos, state.setValue(SolarPanelBlock.POWERED, powered));
        }

        if (!powered) return;

        long rfPerTick = ((SolarPanelBlock) state.getBlock()).getRfPerTick();

        BlockPos below = pos.below();
        IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, below, Direction.UP);
        if (storage != null && storage.canReceive()) {
            storage.receiveEnergy((int) rfPerTick, false);
        }
    }

    private static boolean isGenerating(ServerLevel level, BlockPos pos) {
        return level.isDay() && level.canSeeSky(pos.above());
    }
}