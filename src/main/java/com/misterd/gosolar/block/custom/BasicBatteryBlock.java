package com.misterd.gosolar.block.custom;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class BasicBatteryBlock extends BatteryBlock {

    public BasicBatteryBlock(BlockBehaviour.Properties properties) {
        super(2_000_000, 1_024, properties);
    }
}