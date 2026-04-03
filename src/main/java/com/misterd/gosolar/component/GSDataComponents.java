package com.misterd.gosolar.component;

import com.misterd.gosolar.GoSolar;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GSDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, GoSolar.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> ENERGY_STORED =
            DATA_COMPONENTS.register("energy_stored", () -> DataComponentType.<Long>builder()
                    .persistent(Codec.LONG)
                    .build());

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}