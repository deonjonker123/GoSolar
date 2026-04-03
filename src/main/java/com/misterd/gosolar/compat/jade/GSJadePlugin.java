package com.misterd.gosolar.compat.jade;

import com.misterd.gosolar.block.custom.BatteryBlock;
import com.misterd.gosolar.block.custom.SolarPanelBlock;
import com.misterd.gosolar.blockentity.custom.BatteryBlockEntity;
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
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SolarPanelProvider.INSTANCE, SolarPanelBlock.class);
        registration.registerBlockComponent(BatteryProvider.INSTANCE, BatteryBlock.class);
    }
}