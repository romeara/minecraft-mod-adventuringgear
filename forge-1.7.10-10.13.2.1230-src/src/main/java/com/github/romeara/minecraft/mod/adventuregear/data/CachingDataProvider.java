package com.github.romeara.minecraft.mod.adventuregear.data;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cpw.mods.fml.common.FMLCommonHandler;

// TODO doc
public class CachingDataProvider<T extends IPersitentData> implements IDataProvider<T> {

    private Cache<UUID, T> clientCache;

    private Cache<UUID, T> serverCache;

    public CachingDataProvider(int maximumSize) {
        CacheBuilder builder = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(10, TimeUnit.MINUTES);

        clientCache = builder.build();
        serverCache = builder.build();
    }

    @Override
    public T get(UUID processId) {
        return (processId != null ? getCache().getIfPresent(processId) : null);
    }

    @Override
    public void update(T process) {
        if (process != null) {
            getCache().put(process.getProcessId(), process);
        }
    }

    @Override
    public void clear(UUID id) {
        getCache().invalidate(id);
    }

    private boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    private Cache<UUID, T> getCache() {
        if (isClient()) {
            return clientCache;
        }

        return serverCache;
    }

}
