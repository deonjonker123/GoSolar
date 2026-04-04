package com.misterd.gosolar.item;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.block.GSBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GSCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GoSolar.MODID);

    public static final Supplier<CreativeModeTab> GOSOLAR = CREATIVE_MODE_TAB.register("gosolar_creativetab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GSItems.ELITE_SOLAR_CELL.get()))
                    .title(Component.translatable("creativetab.gosolar"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GSItems.SILICA_BLEND);
                        output.accept(GSItems.BASIC_SOLAR_CELL);
                        output.accept(GSItems.HARDENED_SOLAR_CELL);
                        output.accept(GSItems.ADVANCED_SOLAR_CELL);
                        output.accept(GSItems.ELITE_SOLAR_CELL);
                        output.accept(GSItems.ULTIMATE_SOLAR_CELL);

                        output.accept(GSBlocks.BASIC_SOLAR_PANEL);
                        output.accept(GSBlocks.HARDENED_SOLAR_PANEL);
                        output.accept(GSBlocks.ADVANCED_SOLAR_PANEL);
                        output.accept(GSBlocks.ELITE_SOLAR_PANEL);
                        output.accept(GSBlocks.ULTIMATE_SOLAR_PANEL);

                        output.accept(GSBlocks.BASIC_BATTERY);
                        output.accept(GSBlocks.HARDENED_BATTERY);
                        output.accept(GSBlocks.ADVANCED_BATTERY);
                        output.accept(GSBlocks.ELITE_BATTERY);
                        output.accept(GSBlocks.ULTIMATE_BATTERY);

                        output.accept(GSBlocks.ENERGY_TRANSMITTER);

                        output.accept(GSBlocks.BASIC_ENERGY_RECEIVER);
                        output.accept(GSBlocks.HARDENED_ENERGY_RECEIVER);
                        output.accept(GSBlocks.ADVANCED_ENERGY_RECEIVER);
                        output.accept(GSBlocks.ELITE_ENERGY_RECEIVER);
                        output.accept(GSBlocks.ULTIMATE_ENERGY_RECEIVER);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
