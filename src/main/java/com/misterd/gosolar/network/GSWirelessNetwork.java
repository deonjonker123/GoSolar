package com.misterd.gosolar.network;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GSWirelessNetwork extends SavedData {

    public static final long MAX_POOL = Integer.MAX_VALUE;
    private static final String NAME = "gosolar_wireless_network";
    private static final String TAG_POOLS = "Pools";
    private static final String TAG_PUBLIC = "Public";

    private final Map<UUID, Long> pools = new HashMap<>();
    private final Map<UUID, Boolean> publicFlags = new HashMap<>();

    private Map<UUID, Long> cachedPublicPools = null;
    private boolean publicPoolsDirty = true;

    public static GSWirelessNetwork get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(GSWirelessNetwork::new, GSWirelessNetwork::load),
                NAME
        );
    }

    private GSWirelessNetwork() {}

    private static GSWirelessNetwork load(CompoundTag tag, HolderLookup.Provider registries) {
        GSWirelessNetwork network = new GSWirelessNetwork();
        CompoundTag poolsTag = tag.getCompound(TAG_POOLS);
        for (String key : poolsTag.getAllKeys()) {
            network.pools.put(UUID.fromString(key), poolsTag.getLong(key));
        }
        CompoundTag publicTag = tag.getCompound(TAG_PUBLIC);
        for (String key : publicTag.getAllKeys()) {
            network.publicFlags.put(UUID.fromString(key), publicTag.getBoolean(key));
        }
        return network;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag poolsTag = new CompoundTag();
        pools.forEach((uuid, amount) -> poolsTag.putLong(uuid.toString(), amount));
        tag.put(TAG_POOLS, poolsTag);

        CompoundTag publicTag = new CompoundTag();
        publicFlags.forEach((uuid, isPublic) -> publicTag.putBoolean(uuid.toString(), isPublic));
        tag.put(TAG_PUBLIC, publicTag);

        return tag;
    }

    public long getPool(UUID owner) {
        return pools.getOrDefault(owner, 0L);
    }

    public void addToPool(UUID owner, long amount) {
        long current = pools.getOrDefault(owner, 0L);
        pools.put(owner, Math.min(current + amount, MAX_POOL));
        setDirty();
    }

    public void removeFromPool(UUID owner, long amount) {
        long current = pools.getOrDefault(owner, 0L);
        pools.put(owner, Math.max(current - amount, 0L));
        setDirty();
    }

    public void setPublic(UUID owner, boolean isPublic) {
        publicFlags.put(owner, isPublic);
        publicPoolsDirty = true;
        setDirty();
    }

    public boolean isPublic(UUID owner) {
        return publicFlags.getOrDefault(owner, false);
    }

    public Map<UUID, Long> getPublicPools() {
        if (publicPoolsDirty || cachedPublicPools == null) {
            Map<UUID, Long> result = new HashMap<>();
            publicFlags.forEach((uuid, isPublic) -> {
                if (isPublic && pools.containsKey(uuid)) {
                    result.put(uuid, pools.get(uuid));
                }
            });
            cachedPublicPools = Collections.unmodifiableMap(result);
            publicPoolsDirty = false;
        }
        return cachedPublicPools;
    }
}