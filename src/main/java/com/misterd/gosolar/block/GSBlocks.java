package com.misterd.gosolar.block;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.block.custom.*;
import com.misterd.gosolar.item.GSItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GoSolar.MODID);

    public static final DeferredBlock<Block> BASIC_SOLAR_PANEL = registerBlock("basic_solar_panel",
            () -> new BasicSolarPanelBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> HARDENED_SOLAR_PANEL = registerBlock("hardened_solar_panel",
            () -> new HardenedSolarPanelBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ADVANCED_SOLAR_PANEL = registerBlock("advanced_solar_panel",
            () -> new AdvancedSolarPanelBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ELITE_SOLAR_PANEL = registerBlock("elite_solar_panel",
            () -> new EliteSolarPanelBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ULTIMATE_SOLAR_PANEL = registerBlock("ultimate_solar_panel",
            () -> new UltimateSolarPanelBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> BASIC_BATTERY = registerBlock("basic_battery",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> HARDENED_BATTERY = registerBlock("hardened_battery",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ADVANCED_BATTERY = registerBlock("advanced_battery",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ELITE_BATTERY = registerBlock("elite_battery",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> ULTIMATE_BATTERY = registerBlock("ultimate_battery",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        GSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
