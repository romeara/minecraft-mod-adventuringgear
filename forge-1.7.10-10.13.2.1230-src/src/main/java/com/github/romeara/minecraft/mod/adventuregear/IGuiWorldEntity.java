package com.github.romeara.minecraft.mod.adventuregear;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Represents an item/block which triggers a GUI when used
 * 
 * @author romeara
 */
public interface IGuiWorldEntity extends IModWorldEntity {

    /**
     * Returns a GUI container instance for use when the user is interacting with a server instance of Minecraft
     * 
     * @param player
     *            The player performing an action
     * @param world
     *            Reference to the world the player is operating within
     * @param x
     *            The x coordinate the player is interacting with
     * @param y
     *            The y coordinate the player is interacting with
     * @param z
     *            The z coordinate the player is interacting with
     * @return A GUI container instance for use when the user is interacting with a server instance of Minecraft
     */
    Container getServerGui(EntityPlayer player, World world, int x, int y, int z);

    /**
     * Returns a GUI container instance for use when the user is interacting with a local instance of Minecraft
     * 
     * @param player
     *            The player performing an action
     * @param world
     *            Reference to the world the player is operating within
     * @param x
     *            The x coordinate the player is interacting with
     * @param y
     *            The y coordinate the player is interacting with
     * @param z
     *            The z coordinate the player is interacting with
     * @return A GUI container instance for use when the user is interacting with a local instance of Minecraft
     */
    GuiContainer getClientGui(EntityPlayer player, World world, int x, int y, int z);
}
