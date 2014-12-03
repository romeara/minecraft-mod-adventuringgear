package com.github.romeara.minecraft.mod.adventuregear.gui;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.common.entity.IGuiWorldEntity;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Handler class which finds and displays GUI instances for items associated with this mod
 * 
 * @author romeara
 */
public class GuiHandler implements IGuiHandler {

    /** GUI-displaying items based based on GUI reference IDs */
    private Map<Integer, IGuiWorldEntity> guiIdRecord;

    /** The next ID which may be assigned to a registered item */
    private int currentId;

    /**
     * Creates a new GUI handler instance with no registered items
     */
    public GuiHandler() {
        guiIdRecord = new HashMap<Integer, IGuiWorldEntity>();
        currentId = 0;
    }

    /**
     * @param item
     *            A GUI-displaying item to register with the handler
     * @return The identifier this handler will associate with the item when looking up GUI references
     */
    public int registerGuiItem(IGuiWorldEntity item) {
        guiIdRecord.put(currentId, item);
        currentId++;

        return currentId - 1;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IGuiWorldEntity item = guiIdRecord.get(ID);

        if (item != null) {
            return item.getServerGui(player, world, x, y, z);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IGuiWorldEntity item = guiIdRecord.get(ID);

        if (item != null) {
            return item.getClientGui(player, world, x, y, z);
        }

        return null;
    }

}
