package com.misterd.gosolar.datagen.custom;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.block.GSBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class GSBlockTagProvider extends BlockTagsProvider {
    public GSBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GoSolar.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(GSBlocks.BASIC_SOLAR_PANEL.get())
                .add(GSBlocks.HARDENED_SOLAR_PANEL.get())
                .add(GSBlocks.ADVANCED_SOLAR_PANEL.get())
                .add(GSBlocks.ELITE_SOLAR_PANEL.get())
                .add(GSBlocks.ULTIMATE_SOLAR_PANEL.get())

                .add(GSBlocks.BASIC_BATTERY.get())
                .add(GSBlocks.HARDENED_BATTERY.get())
                .add(GSBlocks.ADVANCED_BATTERY.get())
                .add(GSBlocks.ELITE_BATTERY.get())
                .add(GSBlocks.ULTIMATE_BATTERY.get());
    }
}
