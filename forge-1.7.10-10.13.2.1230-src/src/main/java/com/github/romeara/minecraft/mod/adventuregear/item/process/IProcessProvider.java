package com.github.romeara.minecraft.mod.adventuregear.item.process;

import java.util.UUID;

// TODO doc
public interface IProcessProvider<T extends IItemProcess> {

    T getProcess(boolean isRemote, UUID processId);

    void updateProcess(boolean isRemote, T process);
}
