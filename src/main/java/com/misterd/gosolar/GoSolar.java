package com.misterd.gosolar;

import com.misterd.gosolar.block.GSBlocks;
import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.component.GSDataComponents;
import com.misterd.gosolar.gui.GSMenuTypes;
import com.misterd.gosolar.gui.custom.BatteryBlockScreen;
import com.misterd.gosolar.gui.custom.EnergyTransmitterScreen;
import com.misterd.gosolar.item.GSCreativeTab;
import com.misterd.gosolar.item.GSItems;
import com.misterd.gosolar.network.GSNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(GoSolar.MODID)
public class GoSolar {
    public static final String MODID = "gosolar";

    public GoSolar(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        GSItems.register(modEventBus);
        GSBlocks.register(modEventBus);
        GSCreativeTab.register(modEventBus);
        GSBlockEntities.register(modEventBus);
        GSDataComponents.register(modEventBus);
        GSMenuTypes.register(modEventBus);
        GSNetwork.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(GSMenuTypes.BATTERY_MENU.get(), BatteryBlockScreen::new);
            event.register(GSMenuTypes.ENERGY_TRANSMITTER_MENU.get(), EnergyTransmitterScreen::new);
        }
    }
}
