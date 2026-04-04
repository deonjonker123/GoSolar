package com.misterd.gosolar.network;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class GSWirelessNetwork extends SavedData {

    public static final long MAX_POOL = Integer.MAX_VALUE;
    private static final String NAME = "gosolar_wireless_network";
    private static final String TAG_POOL = "Pool";

    private long pool = 0;

    public static GSWirelessNetwork get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(GSWirelessNetwork::new, GSWirelessNetwork::load),
                NAME
        );
    }

    private GSWirelessNetwork() {}

    private static GSWirelessNetwork load(CompoundTag tag, HolderLookup.Provider registries) {
        GSWirelessNetwork network = new GSWirelessNetwork();
        network.pool = tag.getLong(TAG_POOL);
        return network;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLong(TAG_POOL, pool);
        return tag;
    }

    public long getPool() { return pool; }

    public void addToPool(long amount) {
        pool = Math.min(pool + amount, MAX_POOL);
        setDirty();
    }

    public void removeFromPool(long amount) {
        pool = Math.max(pool - amount, 0);
        setDirty();
    }
}