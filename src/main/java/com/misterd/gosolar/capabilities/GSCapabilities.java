package com.misterd.gosolar.capabilities;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = GoSolar.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GSCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                GSBlockEntities.SOLAR_PANEL_BE.get(),
                (be, side) -> side == Direction.DOWN ? be.getEnergyStorage() : null
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                GSBlockEntities.BATTERY_BE.get(),
                (be, side) -> be.getEnergyStorage()
        );
    }
}