package com.github.romeara.minecraft.mod.adventuregear.gui;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import com.github.romeara.minecraft.mod.adventuregear.AdventuringGearMod;
import com.github.romeara.minecraft.mod.adventuregear.data.IFurnaceProcess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// TODO doc
public class GeneralContainerFurnace extends Container {

    private UUID furnaceProcessId;

    private int lastCookTime;

    private int lastBurnTime;

    private int lastItemBurnTime;

    private static final String __OBFID = "CL_00001748";

    public GeneralContainerFurnace(InventoryPlayer playerInventory, UUID furnaceProcessId) {
        this.furnaceProcessId = furnaceProcessId;
        IFurnaceProcess furnaceProcess = AdventuringGearMod.getInstance().furnaceData().get(furnaceProcessId);

        this.addSlotToContainer(new Slot(furnaceProcess, 0, 56, 17));
        this.addSlotToContainer(new Slot(furnaceProcess, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(playerInventory.player, furnaceProcess, 2, 116, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        IFurnaceProcess furnaceProcess = AdventuringGearMod.getInstance().furnaceData().get(furnaceProcessId);

        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, furnaceProcess.hasCookedForTicks());
        crafting.sendProgressBarUpdate(this, 1, furnaceProcess.remainingFuelTicks());
        crafting.sendProgressBarUpdate(this, 2, furnaceProcess.currentFuelProvidedTicks());
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        IFurnaceProcess furnaceProcess = AdventuringGearMod.getInstance().furnaceData().get(furnaceProcessId);

        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastCookTime != furnaceProcess.hasCookedForTicks()) {
                icrafting.sendProgressBarUpdate(this, 0, furnaceProcess.hasCookedForTicks());
            }

            if (this.lastBurnTime != furnaceProcess.remainingFuelTicks()) {
                icrafting.sendProgressBarUpdate(this, 1, furnaceProcess.remainingFuelTicks());
            }

            if (this.lastItemBurnTime != furnaceProcess.currentFuelProvidedTicks()) {
                icrafting.sendProgressBarUpdate(this, 2, furnaceProcess.currentFuelProvidedTicks());
            }
        }

        this.lastCookTime = furnaceProcess.hasCookedForTicks();
        this.lastBurnTime = furnaceProcess.remainingFuelTicks();
        this.lastItemBurnTime = furnaceProcess.currentFuelProvidedTicks();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int progressBarIndex, int ticks) {
        IFurnaceProcess furnaceProcess = AdventuringGearMod.getInstance().furnaceData().get(furnaceProcessId);

        if (progressBarIndex == 0) {
            furnaceProcess.setHasCookedForTicks(ticks);
        }

        if (progressBarIndex == 1) {
            furnaceProcess.setRemainingFuleTicks(ticks);
        }

        if (progressBarIndex == 2) {
            furnaceProcess.setCurrentFuelProvidedTicks(ticks);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        IFurnaceProcess furnaceProcess = AdventuringGearMod.getInstance().furnaceData().get(furnaceProcessId);

        return furnaceProcess.isUseableByPlayer(player);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotIndex != 1 && slotIndex != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (slotIndex >= 3 && slotIndex < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

}
