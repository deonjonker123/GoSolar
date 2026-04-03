package com.misterd.gosolar.item;

import com.misterd.gosolar.GoSolar;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GoSolar.MODID);

    public static final DeferredItem<Item> BASIC_SOLAR_CELL = ITEMS.register("basic_solar_cell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> HARDENED_SOLAR_CELL = ITEMS.register("hardened_solar_cell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ADVANCED_SOLAR_CELL = ITEMS.register("advanced_solar_cell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ELITE_SOLAR_CELL = ITEMS.register("elite_solar_cell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ULTIMATE_SOLAR_CELL = ITEMS.register("ultimate_solar_cell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SILICA_BLEND = ITEMS.register("silica_blend",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
