package com.misterd.gosolar.datagen.custom;

import com.misterd.gosolar.GoSolar;
import com.misterd.gosolar.item.GSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GSItemModelProvider extends ItemModelProvider {
    public GSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GoSolar.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(GSItems.SILICA_BLEND.get());
        this.basicItem(GSItems.BASIC_SOLAR_CELL.get());
        this.basicItem(GSItems.HARDENED_SOLAR_CELL.get());
        this.basicItem(GSItems.ADVANCED_SOLAR_CELL.get());
        this.basicItem(GSItems.ELITE_SOLAR_CELL.get());
        this.basicItem(GSItems.ULTIMATE_SOLAR_CELL.get());
    }
}
