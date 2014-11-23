package com.github.romeara.minecraft.mod.adventuregear.item.data;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.github.romeara.minecraft.mod.adventuregear.data.IPersitentData;

public class FurnaceState implements IPersitentData, IInventory {

    private static final String DATA_ID_KEY = "com.github.romeara.mod.adventuregear.furnace.state.data.id";

    private final UUID dataId;

    public FurnaceState() {
        this.dataId = UUID.randomUUID();
    }

    public FurnaceState(NBTTagCompound savedState) {
        if (!savedState.hasKey(DATA_ID_KEY)) {
            this.dataId = UUID.randomUUID();
        } else {
            this.dataId = UUID.fromString(savedState.getString(DATA_ID_KEY));

            readFromNBT(savedState);
        }

    }

    @Override
    public UUID getProcessId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSizeInventory() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getInventoryName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void markDirty() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void openInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
        // TODO Auto-generated method stub
        return false;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        // TODO
        return tagCompound;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        // TODO
    }

    // TODO doc - clears all associated tags from tag compound
    public NBTTagCompound clearFromNBT(NBTTagCompound tagCompound) {
        // TODO
        return tagCompound;
    }

    // TODO doc - true if this state is non-default and should be saved
    public boolean hasActiveState() {
        // TODO
        return true;
    }

    public static FurnaceState tickState(FurnaceState currentState) {
        // TODO
        return currentState;
    }

}
