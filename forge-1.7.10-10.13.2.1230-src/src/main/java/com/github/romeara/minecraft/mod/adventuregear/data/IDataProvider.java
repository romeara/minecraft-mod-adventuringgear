package com.github.romeara.minecraft.mod.adventuregear.data;

import java.util.UUID;

// TODO doc
public interface IDataProvider<T extends IPersitentData> {

    T get(UUID id);

    void update(T persistentData);

    void clear(UUID id);
}
