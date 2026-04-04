package com.misterd.gosolar.blockentity;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.block.GSBlocks;
import com.misterd.gosolar.blockentity.custom.BatteryBlockEntity;
import com.misterd.gosolar.blockentity.custom.EnergyReceiverBlockEntity;
import com.misterd.gosolar.blockentity.custom.EnergyTransmitterBlockEntity;
import com.misterd.gosolar.blockentity.custom.SolarPanelBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GoSolar.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL_BE =
            BLOCK_ENTITIES.register("solar_panel_be", () -> BlockEntityType.Builder.of(
                    SolarPanelBlockEntity::new,
                    GSBlocks.BASIC_SOLAR_PANEL.get(),
                    GSBlocks.HARDENED_SOLAR_PANEL.get(),
                    GSBlocks.ADVANCED_SOLAR_PANEL.get(),
                    GSBlocks.ELITE_SOLAR_PANEL.get(),
                    GSBlocks.ULTIMATE_SOLAR_PANEL.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BatteryBlockEntity>> BATTERY_BE =
            BLOCK_ENTITIES.register("battery_be", () -> BlockEntityType.Builder.of(
                    BatteryBlockEntity::new,
                    GSBlocks.BASIC_BATTERY.get(),
                    GSBlocks.HARDENED_BATTERY.get(),
                    GSBlocks.ADVANCED_BATTERY.get(),
                    GSBlocks.ELITE_BATTERY.get(),
                    GSBlocks.ULTIMATE_BATTERY.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyTransmitterBlockEntity>> ENERGY_TRANSMITTER_BE =
            BLOCK_ENTITIES.register("energy_transmitter_be", () -> BlockEntityType.Builder.of(
                    EnergyTransmitterBlockEntity::new,
                    GSBlocks.ENERGY_TRANSMITTER.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyReceiverBlockEntity>> ENERGY_RECEIVER_BE =
            BLOCK_ENTITIES.register("energy_receiver_be", () -> BlockEntityType.Builder.of(
                    EnergyReceiverBlockEntity::new,
                    GSBlocks.BASIC_ENERGY_RECEIVER.get(),
                    GSBlocks.HARDENED_ENERGY_RECEIVER.get(),
                    GSBlocks.ADVANCED_ENERGY_RECEIVER.get(),
                    GSBlocks.ELITE_ENERGY_RECEIVER.get(),
                    GSBlocks.ULTIMATE_ENERGY_RECEIVER.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}