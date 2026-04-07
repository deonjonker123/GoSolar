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
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnergyTransmitterBlockEntity extends BlockEntity implements MenuProvider {

    private static final String TAG_INVENTORY        = "Inventory";
    private static final String TAG_OWNER            = "Owner";
    private static final String TAG_PUBLIC           = "IsPublic";
    private static final String TAG_CHARGE_INVENTORY = "ChargeInventory";

    private static final int SLOT_DRAIN  = 0;
    private static final int SLOT_CHARGE = 1;

    @Nullable
    private UUID ownerUUID = null;
    private boolean isPublic        = false;
    private boolean chargeInventory = false;

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
            if (level instanceof ServerLevel serverLevel && ownerUUID != null) {
                long pool = GSWirelessNetwork.get(serverLevel).getPool(ownerUUID);
                return switch (index) {
                    case 0 -> (int) (pool >> 32);
                    case 1 -> (int) (pool & 0xFFFFFFFFL);
                    case 2 -> (int) (GSWirelessNetwork.MAX_POOL >> 32);
                    case 3 -> (int) (GSWirelessNetwork.MAX_POOL & 0xFFFFFFFFL);
                    case 4 -> isPublic ? 1 : 0;
                    case 5 -> chargeInventory ? 1 : 0;
                    default -> 0;
                };
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 4) {
                isPublic = value == 1;
                setChanged();
                updatePublicState();
            } else if (index == 5) {
                chargeInventory = value == 1;
                setChanged();
            }
        }

        @Override
        public int getCount() { return 6; }
    };

    private final IEnergyStorage energyStorage = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!(level instanceof ServerLevel serverLevel) || ownerUUID == null) return 0;
            GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
            long canReceive = Math.min(maxReceive, GSWirelessNetwork.MAX_POOL - network.getPool(ownerUUID));
            if (canReceive <= 0) return 0;
            if (!simulate) {
                network.addToPool(ownerUUID, canReceive);
                setChanged();
                updatePoweredState();
            }
            return (int) canReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!(level instanceof ServerLevel serverLevel) || ownerUUID == null) return 0;
            GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
            long canExtract = Math.min(maxExtract, network.getPool(ownerUUID));
            if (canExtract <= 0) return 0;
            if (!simulate) {
                network.removeFromPool(ownerUUID, canExtract);
                setChanged();
                updatePoweredState();
            }
            return (int) canExtract;
        }

        @Override public int getEnergyStored() {
            if (level instanceof ServerLevel serverLevel && ownerUUID != null)
                return (int) Math.min(GSWirelessNetwork.get(serverLevel).getPool(ownerUUID), Integer.MAX_VALUE);
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
    public SimpleContainer getInventory()     { return inventory; }
    public ContainerData getContainerData()   { return containerData; }
    @Nullable public UUID getOwnerUUID()      { return ownerUUID; }
    public boolean isPublic()                 { return isPublic; }
    public boolean isChargeInventory()        { return chargeInventory; }

    public void setOwner(UUID uuid)           { this.ownerUUID = uuid; setChanged(); }
    public void setPublic(boolean pub)        { this.isPublic = pub; setChanged(); updatePublicState(); }
    public void setChargeInventory(boolean b) { this.chargeInventory = b; setChanged(); }

    private void updatePoweredState() {
        if (level == null || ownerUUID == null) return;
        long pool = (level instanceof ServerLevel sl) ?
                GSWirelessNetwork.get(sl).getPool(ownerUUID) : 0;
        boolean powered = pool > 0;
        BlockState state = getBlockState();
        if (state.getValue(EnergyTransmitterBlock.POWERED) != powered) {
            level.setBlockAndUpdate(worldPosition, state.setValue(EnergyTransmitterBlock.POWERED, powered));
        }
    }

    private void updatePublicState() {
        if (level instanceof ServerLevel serverLevel && ownerUUID != null) {
            GSWirelessNetwork.get(serverLevel).setPublic(ownerUUID, isPublic);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyTransmitterBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel) || be.ownerUUID == null) return;

        GSWirelessNetwork network = GSWirelessNetwork.get(serverLevel);
        long pool = network.getPool(be.ownerUUID);
        boolean powered = pool > 0;

        if (state.getValue(EnergyTransmitterBlock.POWERED) != powered) {
            level.setBlockAndUpdate(pos, state.setValue(EnergyTransmitterBlock.POWERED, powered));
        }

        // --- Drain slot (item → pool) ---
        ItemStack drainStack = be.inventory.getItem(SLOT_DRAIN);
        if (!drainStack.isEmpty()) {
            IEnergyStorage itemEnergy = drainStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canExtract()) {
                long canReceive = GSWirelessNetwork.MAX_POOL - network.getPool(be.ownerUUID);
                int toExtract = (int) Math.min(itemEnergy.getEnergyStored(), canReceive);
                int extracted = itemEnergy.extractEnergy(toExtract, false);
                if (extracted > 0) {
                    network.addToPool(be.ownerUUID, extracted);
                    be.setChanged();
                }
            }
        }

        // --- Charge slot (pool → item), always takes priority ---
        ItemStack chargeStack = be.inventory.getItem(SLOT_CHARGE);
        if (!chargeStack.isEmpty()) {
            IEnergyStorage itemEnergy = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null && itemEnergy.canReceive()) {
                int toInsert = (int) Math.min(network.getPool(be.ownerUUID),
                        itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored());
                int accepted = itemEnergy.receiveEnergy(toInsert, false);
                if (accepted > 0) {
                    network.removeFromPool(be.ownerUUID, accepted);
                    be.setChanged();
                }
            }
        }

        // --- Inventory charging (pool → hotbar + offhand + curios) ---
        if (be.chargeInventory) {
            Player owner = serverLevel.getPlayerByUUID(be.ownerUUID);
            if (owner != null) {
                List<ItemStack> targets = new ArrayList<>();

                // Hotbar slots 0-8
                for (int i = 0; i < 9; i++) {
                    targets.add(owner.getInventory().items.get(i));
                }

                // Offhand
                targets.addAll(owner.getInventory().offhand);

                // Curios (optional dependency)
                if (ModList.get().isLoaded("curios")) {
                    CuriosApi.getCuriosInventory(owner).ifPresent(curios ->
                            curios.getCurios().values().forEach(handler -> {
                                var stacks = handler.getStacks();
                                for (int i = 0; i < stacks.getSlots(); i++) {
                                    targets.add(stacks.getStackInSlot(i));
                                }
                            })
                    );
                }

                for (ItemStack stack : targets) {
                    if (stack.isEmpty()) continue;
                    IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                    if (itemEnergy == null || !itemEnergy.canReceive()) continue;

                    long poolNow = network.getPool(be.ownerUUID);
                    if (poolNow <= 0) break;

                    int toInsert = (int) Math.min(poolNow,
                            itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored());
                    if (toInsert <= 0) continue;

                    int accepted = itemEnergy.receiveEnergy(toInsert, false);
                    if (accepted > 0) {
                        network.removeFromPool(be.ownerUUID, accepted);
                        be.setChanged();
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (ownerUUID != null) tag.putUUID(TAG_OWNER, ownerUUID);
        tag.putBoolean(TAG_PUBLIC, isPublic);
        tag.putBoolean(TAG_CHARGE_INVENTORY, chargeInventory);
        tag.put(TAG_INVENTORY, inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.hasUUID(TAG_OWNER)) ownerUUID = tag.getUUID(TAG_OWNER);
        isPublic        = tag.getBoolean(TAG_PUBLIC);
        chargeInventory = tag.getBoolean(TAG_CHARGE_INVENTORY);
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