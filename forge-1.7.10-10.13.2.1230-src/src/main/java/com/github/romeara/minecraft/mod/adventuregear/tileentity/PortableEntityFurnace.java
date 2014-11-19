package com.github.romeara.minecraft.mod.adventuregear.tileentity;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;

import com.github.romeara.minecraft.mod.adventuregear.item.process.IFurnaceProcess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PortableEntityFurnace implements IInventory, IFurnaceProcess {

    /** The ItemStacks that hold the items currently being used in the furnace */
    private ItemStack[] furnaceItemStacks = new ItemStack[3];

    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;

    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;

    /** The number of ticks that the current item has been cooking for */
    private int furnaceCookTime;

    private UUID id;

    private boolean dirty;

    public PortableEntityFurnace() {
        super();
        id = UUID.randomUUID();
        dirty = true;
    }

    public PortableEntityFurnace(NBTTagCompound p_145839_1_) {
        super();

        readFromNBT(p_145839_1_);
    }

    @Override
    public UUID getProcessId() {
        return id;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return this.furnaceItemStacks[slotIndex];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        ItemStack stack = getStackInSlot(slotIndex);
        if (stack != null) {
            if (stack.stackSize > amount) {
                stack = stack.splitStack(amount);
                markDirty();
            } else {
                setInventorySlotContents(slotIndex, null);
            }
        }
        return stack;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        if (this.furnaceItemStacks[slotIndex] != null) {
            ItemStack itemstack = this.furnaceItemStacks[slotIndex];
            this.furnaceItemStacks[slotIndex] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
        this.furnaceItemStacks[slotIndex] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getInventoryName() {
        return "container.furnace";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    // TODO move, doc, cleanup
    public static UUID getId(NBTTagCompound p_145839_1_) {
        if (p_145839_1_ != null && p_145839_1_.hasKey("furnaceDataId")) {
            return UUID.fromString(p_145839_1_.getString("furnaceDataId"));
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound itemMetaData) {
        if (itemMetaData != null && itemMetaData.hasKey("furnaceDataId")) {
            this.id = UUID.fromString(itemMetaData.getString("furnaceDataId"));
            NBTTagList nbttaglist = itemMetaData.getTagList("Items", 10);
            this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                byte b0 = nbttagcompound1.getByte("Slot");

                if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
                {
                    this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                }
            }

            this.furnaceBurnTime = itemMetaData.getShort("BurnTime");
            this.furnaceCookTime = itemMetaData.getShort("CookTime");
            this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound itemMetaData) {
        if (dirty) {
            itemMetaData.setShort("BurnTime", (short) this.furnaceBurnTime);
            itemMetaData.setShort("CookTime", (short) this.furnaceCookTime);
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 0; i < this.furnaceItemStacks.length; ++i)
            {
                if (this.furnaceItemStacks[i] != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("Slot", (byte) i);
                    this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
            }

            itemMetaData.setTag("Items", nbttaglist);

            itemMetaData.setString("furnaceDataId", id.toString());
            dirty = false;
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int maxValue) {
        return this.furnaceCookTime * maxValue / 200;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_) {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * p_145955_1_ / this.currentItemBurnTime;
    }

    /**
     * Furnace isBurning
     */
    @Override
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @Override
    public void runTick() {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (this.furnaceBurnTime != 0 || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null) {
            if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);

                if (this.furnaceBurnTime > 0) {
                    flag1 = true;

                    if (this.furnaceItemStacks[1] != null) {
                        --this.furnaceItemStacks[1].stackSize;

                        if (this.furnaceItemStacks[1].stackSize == 0) {
                            this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }
        }

        if (flag != this.furnaceBurnTime > 0) {
            flag1 = true;
            // TODO Update block state (for animiations, see original implementation)
        }

        if (flag1) {
            this.markDirty();
        }
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) {
                return false;
            }
            if (this.furnaceItemStacks[2] == null) {
                return true;
            }
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) {
                return false;
            }
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            // Forge BugFix: Make it respect stack sizes properly.
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize();
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = itemstack.copy();
            } else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
                // Forge BugFix: Results may have multiple items
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize;
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
            markDirty();
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return p_94041_1_ == 2 ? false : (p_94041_1_ == 1 ? TileEntityFurnace.isItemFuel(p_94041_2_) : true);
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public int hasCookedForTicks() {
        return furnaceCookTime;
    }

    @Override
    public void setHasCookedForTicks(int ticks) {
        this.furnaceCookTime = ticks;
    }

    @Override
    public int remainingFuelTicks() {
        return furnaceBurnTime;
    }

    @Override
    public void setRemainingFuleTicks(int ticks) {
        this.furnaceBurnTime = ticks;
    }

    @Override
    public int currentFuelProvidedTicks() {
        return currentItemBurnTime;
    }

    @Override
    public void setCurrentFuelProvidedTicks(int ticks) {
        this.currentItemBurnTime = ticks;
    }

}
