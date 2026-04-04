package com.misterd.gosolar.gui.custom;

import com.misterd.gosolar.blockentity.custom.EnergyTransmitterBlockEntity;
import com.misterd.gosolar.gui.GSMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyTransmitterMenu extends AbstractContainerMenu {

    public final EnergyTransmitterBlockEntity blockEntity;
    private final Level level;
    private final ContainerData containerData;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_ROW_COUNT * PLAYER_INVENTORY_COLUMN_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int CHARGE_SLOT_INDEX = VANILLA_SLOT_COUNT;
    private static final int DRAIN_SLOT_INDEX = VANILLA_SLOT_COUNT + 1;
    private static final int BLOCK_SLOT_COUNT = 2;
    private static final int TOTAL_SLOT_COUNT = VANILLA_SLOT_COUNT + BLOCK_SLOT_COUNT;

    public EnergyTransmitterMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public EnergyTransmitterMenu(int containerId, Inventory inv, BlockEntity blockEntity, ContainerData containerData) {
        super(GSMenuTypes.ENERGY_TRANSMITTER_MENU.get(), containerId);
        this.blockEntity = (EnergyTransmitterBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.containerData = containerData;
        this.addDataSlots(containerData);

        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);

        this.addSlot(new Slot(this.blockEntity.getInventory(), 1, 156, 22));
        this.addSlot(new Slot(this.blockEntity.getInventory(), 0, 156, 47));
    }

    public long getPoolStored() {
        return ((long) containerData.get(0) << 32) | (containerData.get(1) & 0xFFFFFFFFL);
    }

    public long getMaxPool() {
        return ((long) containerData.get(2) << 32) | (containerData.get(3) & 0xFFFFFFFFL);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot sourceSlot = this.slots.get(slotIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (slotIndex < VANILLA_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, CHARGE_SLOT_INDEX, TOTAL_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), player, this.blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < PLAYER_INVENTORY_ROW_COUNT; i++) {
            for (int j = 0; j < PLAYER_INVENTORY_COLUMN_COUNT; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 79 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < HOTBAR_SLOT_COUNT; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 138));
        }
    }
}