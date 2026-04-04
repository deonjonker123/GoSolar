package com.misterd.gosolar.blockentity.custom;

import com.misterd.gosolar.block.custom.EnergyTransmitterBlock;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.gui.custom.EnergyTransmitterMenu;
import com.misterd.gosolar.network.GSWirelessNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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

public class EnergyTransmitterBlockEntity extends BlockEntity implements MenuProvider {

    private static final String TAG_INVENTORY = "Inventory";
    private static final int SLOT_DRAIN = 0;
    private static final int SLOT_CHARGE = 1;

    private final SimpleContainer inventory = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            EnergyTransmitterBlockEntity.this.setChanged();
        }
    };

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            if (level instanceof ServerLevel serverLevel) {
                long pool = GSWirelessNetwork.get(serverLevel).getPool();
                return switch (index) {
                    case 0 -> (int) (pool >> 32);
                    case 1 -> (int) (pool & 0xFFFFFFFFL);
                    case 2 -> (int) (GSWirelessNetwork.MAX_POOL >> 32);
                    case 3 -> (int) (GSWirelessNetwork.MAX_POOL & 0xFFFFFFFFL);
                    default -> 0;
                };
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() { return 4; }
    };

    private final IEnergyStorage energyStorage = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!(level instanceof ServerLevel serverLevel)) return 0;
            GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
            long canReceive = Math.min(maxReceive, GSWirelessNetwork.MAX_POOL - network.getPool());
            if (canReceive <= 0) return 0;
            if (!simulate) {
                network.addToPool(canReceive);
                setChanged();
                updatePoweredState(true);
            }
            return (int) canReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!(level instanceof ServerLevel serverLevel)) return 0;
            GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
            long canExtract = Math.min(maxExtract, network.getPool());
            if (canExtract <= 0) return 0;
            if (!simulate) {
                network.removeFromPool(canExtract);
                setChanged();
                updatePoweredState(network.getPool() > 0);
            }
            return (int) canExtract;
        }

        @Override public int getEnergyStored() {
            if (level instanceof ServerLevel serverLevel)
                return (int) Math.min(GSWirelessNetwork.get(serverLevel).getPool(), Integer.MAX_VALUE);
            return 0;
        }
        @Override public int getMaxEnergyStored() { return Integer.MAX_VALUE; }
        @Override public boolean canExtract() { return true; }
        @Override public boolean canReceive() { return true; }
    };

    public EnergyTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(GSBlockEntities.ENERGY_TRANSMITTER_BE.get(), pos, state);
    }

    public IEnergyStorage getEnergyStorage() { return energyStorage; }
    public SimpleContainer getInventory() { return inventory; }
    public ContainerData getContainerData() { return containerData; }

    private void updatePoweredState(boolean powered) {
        if (level == null) return;
        BlockState state = getBlockState();
        if (state.getValue(EnergyTransmitterBlock.POWERED) != powered) {
            level.setBlockAndUpdate(worldPosition, state.setValue(EnergyTransmitterBlock.POWERED, powered));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyTransmitterBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
        boolean powered = network.getPool() > 0;

        if (state.getValue(EnergyTransmitterBlock.POWERED) != powered) {
            level.setBlockAndUpdate(pos, state.setValue(EnergyTransmitterBlock.POWERED, powered));
        }

        ItemStack drainStack = be.inventory.getItem(SLOT_DRAIN);
        if (!drainStack.isEmpty()) {
            IEnergyStorage itemEnergy = drainStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canExtract()) {
                long canReceive = GSWirelessNetwork.MAX_POOL - network.getPool();
                int toExtract = (int) Math.min(itemEnergy.getEnergyStored(), canReceive);
                int extracted = itemEnergy.extractEnergy(toExtract, false);
                if (extracted > 0) {
                    network.addToPool(extracted);
                    be.setChanged();
                }
            }
        }

        ItemStack chargeStack = be.inventory.getItem(SLOT_CHARGE);
        if (!chargeStack.isEmpty()) {
            IEnergyStorage itemEnergy = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canReceive()) {
                int toInsert = (int) Math.min(network.getPool(), itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored());
                int accepted = itemEnergy.receiveEnergy(toInsert, false);
                if (accepted > 0) {
                    network.removeFromPool(accepted);
                    be.setChanged();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_INVENTORY, inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.fromTag(tag.getList(TAG_INVENTORY, Tag.TAG_COMPOUND), registries);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EnergyTransmitterMenu(containerId, playerInventory, this, containerData);
    }
}