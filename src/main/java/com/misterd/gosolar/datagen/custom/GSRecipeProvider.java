package com.misterd.gosolar.datagen.custom;

import com.misterd.gosolar.block.GSBlocks;
import com.misterd.gosolar.item.GSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class GSRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public GSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GSItems.SILICA_BLEND.get(), 3)
                .requires(Items.SAND)
                .requires(Items.QUARTZ)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSItems.BASIC_SOLAR_CELL.get())
                .pattern("GGG")
                .pattern("CBC")
                .pattern("ICI")
                .define('C', GSItems.SILICA_BLEND.get())
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('B', Items.REDSTONE)
                .define('I', Items.COPPER_INGOT)
                .unlockedBy("has_silica_blend", has(GSItems.SILICA_BLEND.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSItems.HARDENED_SOLAR_CELL.get())
                .pattern("GGG")
                .pattern("CBC")
                .pattern("ICI")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.REDSTONE_BLOCK)
                .define('C', GSItems.BASIC_SOLAR_CELL.get())
                .unlockedBy("has_basic_solar_cell", has(GSItems.BASIC_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSItems.ADVANCED_SOLAR_CELL.get())
                .pattern("GGG")
                .pattern("CBC")
                .pattern("ICI")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.GOLD_INGOT)
                .define('B', Items.REDSTONE_BLOCK)
                .define('C', GSItems.HARDENED_SOLAR_CELL.get())
                .unlockedBy("has_hardened_solar_cell", has(GSItems.HARDENED_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSItems.ELITE_SOLAR_CELL.get())
                .pattern("GGG")
                .pattern("CBC")
                .pattern("ICI")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.DIAMOND)
                .define('B', Items.REDSTONE_BLOCK)
                .define('C', GSItems.ADVANCED_SOLAR_CELL.get())
                .unlockedBy("has_advanced_solar_cell", has(GSItems.ADVANCED_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSItems.ULTIMATE_SOLAR_CELL.get())
                .pattern("GGG")
                .pattern("CBC")
                .pattern("ICI")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.NETHERITE_INGOT)
                .define('B', Items.REDSTONE_BLOCK)
                .define('C', GSItems.ELITE_SOLAR_CELL.get())
                .unlockedBy("has_elite_solar_cell", has(GSItems.ELITE_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.BASIC_SOLAR_PANEL.get())
                .pattern("CCC")
                .pattern("SPS")
                .pattern("SSS")
                .define('C', GSItems.BASIC_SOLAR_CELL.get())
                .define('S', Tags.Items.STONES)
                .define('P', Items.COPPER_INGOT)
                .unlockedBy("has_basic_solar_cell", has(GSItems.BASIC_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.HARDENED_SOLAR_PANEL.get())
                .pattern("CCC")
                .pattern("SPS")
                .pattern("SSS")
                .define('C', GSItems.HARDENED_SOLAR_CELL.get())
                .define('S', Tags.Items.STONES)
                .define('P', Items.IRON_INGOT)
                .unlockedBy("has_hardened_solar_cell", has(GSItems.HARDENED_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ADVANCED_SOLAR_PANEL.get())
                .pattern("CCC")
                .pattern("SGS")
                .pattern("SSS")
                .define('C', GSItems.ADVANCED_SOLAR_CELL.get())
                .define('S', Tags.Items.STONES)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_advanced_solar_cell", has(GSItems.ADVANCED_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ELITE_SOLAR_PANEL.get())
                .pattern("CCC")
                .pattern("SDS")
                .pattern("SSS")
                .define('C', GSItems.ELITE_SOLAR_CELL.get())
                .define('S', Tags.Items.STONES)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_elite_solar_cell", has(GSItems.ELITE_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ULTIMATE_SOLAR_PANEL.get())
                .pattern("CCC")
                .pattern("SXS")
                .pattern("SSS")
                .define('C', GSItems.ULTIMATE_SOLAR_CELL.get())
                .define('S', Tags.Items.STONES)
                .define('X', Items.NETHER_STAR)
                .unlockedBy("has_ultimate_solar_cell", has(GSItems.ULTIMATE_SOLAR_CELL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.BASIC_BATTERY.get())
                .pattern("SIS")
                .pattern("SCS")
                .pattern("SRS")
                .define('S', Tags.Items.STONES)
                .define('C', GSItems.SILICA_BLEND)
                .define('R', Items.REDSTONE_BLOCK)
                .define('I', Items.COPPER_INGOT)
                .unlockedBy("has_silica_blend", has(GSItems.SILICA_BLEND.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.HARDENED_BATTERY.get())
                .pattern("SCS")
                .pattern("SBS")
                .pattern("SRS")
                .define('S', Tags.Items.STONES)
                .define('C', Items.IRON_INGOT)
                .define('B', GSBlocks.BASIC_BATTERY.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_basic_battery", has(GSBlocks.BASIC_BATTERY.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ADVANCED_BATTERY.get())
                .pattern("SGS")
                .pattern("SBS")
                .pattern("SRS")
                .define('S', Tags.Items.STONES)
                .define('G', Items.GOLD_INGOT)
                .define('B', GSBlocks.HARDENED_BATTERY.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_hardened_battery", has(GSBlocks.HARDENED_BATTERY.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ELITE_BATTERY.get())
                .pattern("SDS")
                .pattern("SBS")
                .pattern("SRS")
                .define('S', Tags.Items.STONES)
                .define('D', Items.DIAMOND)
                .define('B', GSBlocks.ADVANCED_BATTERY.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_advanced_battery", has(GSBlocks.ADVANCED_BATTERY.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GSBlocks.ULTIMATE_BATTERY.get())
                .pattern("SNS")
                .pattern("SBS")
                .pattern("SRS")
                .define('S', Tags.Items.STONES)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', GSBlocks.ELITE_BATTERY.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_elite_battery", has(GSBlocks.ELITE_BATTERY.get()))
                .save(recipeOutput);
    }
}
