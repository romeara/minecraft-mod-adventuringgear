package com.github.romeara.minecraft.mod.adventuregear.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

/**
 * Workbench GUI which can launch from an item action
 * 
 * <p>
 * The standard interaction window checks if the player is within a certain distance of a workbench, which doesn't work
 * with item-activated crafting
 * </p>
 * 
 * @author romeara
 */
public class PortableContainerWorkbench extends ContainerWorkbench {

    /**
     * Creates a new workbench instance
     * 
     * @param player
     *            The player causing an action that generated a crafting interaction
     * @param world
     *            The mine craft world the player is operating within
     * @param x
     *            The x coordinate being interacted with
     * @param y
     *            The y coordinate being interacted with
     * @param z
     *            The z coordinate being interacted with
     */
    public PortableContainerWorkbench(InventoryPlayer player, World world, int x, int y, int z) {
        super(player, world, x, y, z);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        // Modified such that GUI will stay open. Normally, the crafting window would check that the player
        // was within a certain radius of the crafting bench
        return true;
    }

}
