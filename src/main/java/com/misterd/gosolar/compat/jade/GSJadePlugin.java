package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.block.custom.BatteryBlock;
import com.misterd.gosolar.block.custom.EnergyReceiverBlock;
import com.misterd.gosolar.block.custom.EnergyTransmitterBlock;
import com.misterd.gosolar.block.custom.SolarPanelBlock;
import com.misterd.gosolar.blockentity.custom.BatteryBlockEntity;
import com.misterd.gosolar.blockentity.custom.EnergyReceiverBlockEntity;
import com.misterd.gosolar.blockentity.custom.EnergyTransmitterBlockEntity;
import com.misterd.gosolar.blockentity.custom.SolarPanelBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class GSJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SolarPanelProvider.INSTANCE, SolarPanelBlockEntity.class);
        registration.registerBlockDataProvider(BatteryProvider.INSTANCE, BatteryBlockEntity.class);
        registration.registerBlockDataProvider(EnergyTransmitterProvider.INSTANCE, EnergyTransmitterBlockEntity.class);
        registration.registerBlockDataProvider(EnergyReceiverProvider.INSTANCE, EnergyReceiverBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SolarPanelProvider.INSTANCE, SolarPanelBlock.class);
        registration.registerBlockComponent(BatteryProvider.INSTANCE, BatteryBlock.class);
        registration.registerBlockComponent(EnergyTransmitterProvider.INSTANCE, EnergyTransmitterBlock.class);
        registration.registerBlockComponent(EnergyReceiverProvider.INSTANCE, EnergyReceiverBlock.class);
    }
}