package com.github.romeara.minecraft.mod.adventuregear.item.process;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

// TODO doc
public class CachingProcessProvider<T extends IItemProcess> implements IProcessProvider<T> {

    private Cache<UUID, T> cache;

    public CachingProcessProvider(int maximumSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public T getProcess(UUID processId) {
        return (processId != null ? cache.getIfPresent(processId) : null);
    }

    @Override
    public void updateProcess(T process) {
        if (process != null) {
            cache.put(process.getProcessId(), process);
        }
    }

}
