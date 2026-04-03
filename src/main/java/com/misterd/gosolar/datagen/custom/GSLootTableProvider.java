package com.misterd.gosolar.datagen.custom;

import com.misterd.gosolar.block.GSBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class GSLootTableProvider extends BlockLootSubProvider {
    public GSLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        dropSelf(GSBlocks.BASIC_SOLAR_PANEL.get());
        dropSelf(GSBlocks.HARDENED_SOLAR_PANEL.get());
        dropSelf(GSBlocks.ADVANCED_SOLAR_PANEL.get());
        dropSelf(GSBlocks.ELITE_SOLAR_PANEL.get());
        dropSelf(GSBlocks.ULTIMATE_SOLAR_PANEL.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GSBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
