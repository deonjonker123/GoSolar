package com.misterd.gosolar.blockentity.custom;

import com.misterd.gosolar.block.custom.BatteryBlock;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.component.GSDataComponents;
import com.misterd.gosolar.gui.custom.BatteryBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BatteryBlockEntity extends BlockEntity implements MenuProvider {

    private static final String TAG_ENERGY = "Energy";
    private static final String TAG_INVENTORY = "Inventory";
    private static final int SLOT_DRAIN = 0;
    private static final int SLOT_CHARGE = 1;

    private long energyStored = 0;

    private final SimpleContainer inventory = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            BatteryBlockEntity.this.setChanged();
        }
    };

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) (energyStored >> 32);
                case 1 -> (int) (energyStored & 0xFFFFFFFFL);
                case 2 -> (int) (getCapacity() >> 32);
                case 3 -> (int) (getCapacity() & 0xFFFFFFFFL);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> energyStored = ((long) value << 32) | (energyStored & 0xFFFFFFFFL);
                case 1 -> energyStored = (energyStored & 0xFFFFFFFF00000000L) | (value & 0xFFFFFFFFL);
                case 2, 3 -> {}
            }
        }

        @Override
        public int getCount() { return 4; }
    };

    private final IEnergyStorage energyStorage = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            long canReceive = Math.min(maxReceive, Math.min(getTransferRate(), getCapacity() - energyStored));
            if (canReceive <= 0) return 0;
            if (!simulate) {
                energyStored += canReceive;
                setChanged();
                updatePoweredState();
            }
            return (int) canReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            long canExtract = Math.min(maxExtract, Math.min(getTransferRate(), energyStored));
            if (canExtract <= 0) return 0;
            if (!simulate) {
                energyStored -= canExtract;
                setChanged();
                updatePoweredState();
            }
            return (int) canExtract;
        }

        @Override public int getEnergyStored() { return (int) Math.min(energyStored, Integer.MAX_VALUE); }
        @Override public int getMaxEnergyStored() { return (int) Math.min(getCapacity(), Integer.MAX_VALUE); }
        @Override public boolean canExtract() { return true; }
        @Override public boolean canReceive() { return true; }
    };

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(GSBlockEntities.BATTERY_BE.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() { return energyStorage; }
    public SimpleContainer getInventory() { return inventory; }
    public ContainerData getContainerData() { return containerData; }
    public long getCapacity() { return ((BatteryBlock) getBlockState().getBlock()).getCapacity(); }
    public long getTransferRate() { return ((BatteryBlock) getBlockState().getBlock()).getTransferRate(); }
    public long getEnergyStoredLong() { return energyStored; }

    private void updatePoweredState() {
        if (level == null) return;
        boolean powered = energyStored > 0;
        BlockState state = getBlockState();
        if (state.getValue(BatteryBlock.POWERED) != powered) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BatteryBlock.POWERED, powered));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BatteryBlockEntity be) {
        if (level.isClientSide()) return;

        ItemStack drainStack = be.inventory.getItem(SLOT_DRAIN);
        if (!drainStack.isEmpty()) {
            IEnergyStorage itemEnergy = drainStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canExtract()) {
                int toExtract = (int) Math.min(be.getTransferRate(), be.getCapacity() - be.energyStored);
                int extracted = itemEnergy.extractEnergy(toExtract, false);
                if (extracted > 0) {
                    be.energyStored += extracted;
                    be.setChanged();
                    be.updatePoweredState();
                }
            }
        }

        ItemStack chargeStack = be.inventory.getItem(SLOT_CHARGE);
        if (!chargeStack.isEmpty()) {
            IEnergyStorage itemEnergy = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canReceive()) {
                int toInsert = (int) Math.min(be.getTransferRate(), be.energyStored);
                int accepted = itemEnergy.receiveEnergy(toInsert, false);
                if (accepted > 0) {
                    be.energyStored -= accepted;
                    be.setChanged();
                    be.updatePoweredState();
                }
            }
        }

        if (be.energyStored > 0) {
            List<IEnergyStorage> receivers = new ArrayList<>();
            for (Direction side : Direction.values()) {
                IEnergyStorage neighbor = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(side), side.getOpposite());
                if (neighbor != null && neighbor.canReceive()) {
                    receivers.add(neighbor);
                }
            }

            if (!receivers.isEmpty()) {
                int perSide = (int) Math.min(be.getTransferRate() / receivers.size(), be.energyStored);
                for (IEnergyStorage receiver : receivers) {
                    if (perSide <= 0) break;
                    int accepted = receiver.receiveEnergy(perSide, false);
                    if (accepted > 0) {
                        be.energyStored -= accepted;
                        be.setChanged();
                        be.updatePoweredState();
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(TAG_ENERGY, energyStored);
        tag.put(TAG_INVENTORY, inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStored = tag.getLong(TAG_ENERGY);
        inventory.fromTag(tag.getList(TAG_INVENTORY, Tag.TAG_COMPOUND), registries);
    }

    public void saveEnergyToItem(ItemStack stack) {
        if (energyStored > 0) {
            stack.set(GSDataComponents.ENERGY_STORED.get(), energyStored);
        }
    }

    public void loadEnergyFromItem(ItemStack stack) {
        Long stored = stack.get(GSDataComponents.ENERGY_STORED.get());
        if (stored != null) {
            energyStored = stored;
            updatePoweredState();
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BatteryBlockMenu(containerId, playerInventory, this, containerData);
    }
}