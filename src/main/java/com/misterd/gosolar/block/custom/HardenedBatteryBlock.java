package com.misterd.gosolar.block.custom;

public class HardenedBatteryBlock extends BatteryBlock {

    public HardenedBatteryBlock(Properties properties) {
        super(8_000_000, 4_096, properties);
    }
}