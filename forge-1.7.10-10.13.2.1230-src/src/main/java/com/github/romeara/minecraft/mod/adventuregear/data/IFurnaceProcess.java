package com.github.romeara.minecraft.mod.adventuregear.data;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

// TODO doc
public interface IFurnaceProcess extends IInventory, IPersitentData {

    void readFromNBT(NBTTagCompound p_145839_1_);

    void writeToNBT(NBTTagCompound p_145841_1_);

    // TODO update entity call
    void runTick();

    /**
     * @return True if furnace is burning, false otherwise
     */
    public boolean isBurning();

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    int getBurnTimeRemainingScaled(int maxValue);

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    int getCookProgressScaled(int maxValue);

    /**
     * @return The number of ticks that the current item has been cooking for
     */
    int hasCookedForTicks();

    void setHasCookedForTicks(int ticks);

    /**
     * @return The number of ticks that the furnace will keep burning
     */
    int remainingFuelTicks();

    void setRemainingFuleTicks(int ticks);

    /**
     * @return The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    int currentFuelProvidedTicks();

    void setCurrentFuelProvidedTicks(int ticks);
}
