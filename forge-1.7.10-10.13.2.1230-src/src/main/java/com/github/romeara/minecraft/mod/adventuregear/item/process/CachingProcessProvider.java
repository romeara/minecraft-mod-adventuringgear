package com.github.romeara.minecraft.mod.adventuregear.item.process;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

// TODO doc
public class CachingProcessProvider<T extends IItemProcess> implements IProcessProvider<T> {

    private Cache<UUID, T> cache;

    private Cache<UUID, T> remoteCache;

    public CachingProcessProvider(int maximumSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        remoteCache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public T getProcess(boolean isRemote, UUID processId) {
        if (isRemote) {
            return (processId != null ? remoteCache.getIfPresent(processId) : null);
        }

        return (processId != null ? cache.getIfPresent(processId) : null);
    }

    @Override
    public void updateProcess(boolean isRemote, T process) {
        if (process != null) {
            if (isRemote) {
                remoteCache.put(process.getProcessId(), process);
            } else {
                cache.put(process.getProcessId(), process);
            }
        }
    }

}
