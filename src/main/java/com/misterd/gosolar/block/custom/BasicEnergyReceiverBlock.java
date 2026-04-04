package com.misterd.gosolar.block.custom;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class BasicEnergyReceiverBlock extends EnergyReceiverBlock {

    public BasicEnergyReceiverBlock(BlockBehaviour.Properties properties) {
        super(1_024, properties);
    }
}