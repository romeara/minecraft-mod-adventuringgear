package com.github.romeara.minecraft.mod.adventuregear.item.process;

import java.util.UUID;

// TODO doc
public interface IProcessProvider<T extends IItemProcess> {

    T getProcess(UUID processId);

    void updateProcess(T process);
}
