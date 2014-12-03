package com.github.romeara.minecraft.mod.adventuregear;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.github.romeara.minecraft.mod.adventuregear.block.BlockClipboard;
import com.github.romeara.minecraft.mod.adventuregear.gui.GuiHandler;
import com.github.romeara.minecraft.mod.adventuregear.item.ItemClipboard;
import com.github.romeara.minecraft.mod.common.ShapedCraftingRecipe;
import com.github.romeara.minecraft.mod.common.entity.ICraftableWorldEntity;
import com.github.romeara.minecraft.mod.common.entity.IGuiWorldEntity;
import com.github.romeara.minecraft.mod.common.entity.IModWorldEntity;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Represents the general concept of a mod for Minecraft
 * 
 * <p>
 * The adventuring gear mod is designed to add several convience tools to make it easier for players who wish to wander
 * the world and explore to do so without having to deal with the chore of setting up a "base camp" to create and hold
 * items in the game
 * </p>
 * 
 * @author romeara
 */
@Mod(modid = AdventuringGearMod.MODID, version = AdventuringGearMod.VERSION)
public class AdventuringGearMod {

    /** This mod instance, populated by the forge framework on startup */
    @Instance(AdventuringGearMod.MODID)
    protected static AdventuringGearMod instance;

    // TODO Look into inheriting this from gradle, so we only have to maintain in one place
    /** The unique identifier of the mod */
    public static final String MODID = "com.github.romeara.minecraft.mod.adventuregear";

    // TODO Look into inheriting this from gradle, so we only have to maintain in one place
    /** The current version of this mod */
    public static final String VERSION = "0.1";

    /** Map relating an item name to an associated GUI identifier within the mod instance */
    private Map<String, Integer> guiId = new HashMap<String, Integer>();

    /**
     * @return This mod instance in the running Minecraft application
     */
    public static AdventuringGearMod getInstance() {
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        GuiHandler guiHandler = new GuiHandler();

        registerItem(new ItemClipboard(), guiHandler);
        registerBlock(new BlockClipboard(), guiHandler);

        // Register the GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }

    /**
     * @param item
     *            The GUI-hooked item to lookup the GUI ID for
     * @return The GUI ID for the item
     */
    public int getGuiId(IGuiWorldEntity item) {
        return guiId.get(item.getIdentifier());
    }

    /**
     * Registers an item with the main Minecraft application. Any recipes or GUI hooks present will be registered as
     * well
     * 
     * <p>
     * This is a seperate call from {@link #registerBlock(Block, GuiHandler)}, as items and blocks are separate classes
     * </p>
     * 
     * @param item
     *            The mod item to register
     */
    private <T extends Item & IModWorldEntity> void registerItem(T item, GuiHandler guiHandler) {
        // Perform initial registration of the item object itself
        GameRegistry.registerItem(item, item.getIdentifier(), MODID);

        registerWorldEntity(item, guiHandler);
    }

    /**
     * Registers a block with the main Minecraft application. Any recipes or GUI hooks present will be registered as
     * well
     * 
     * 
     * <p>
     * This is a separate call from {@link #registerItem(Item, GuiHandler)}, as items and blocks are separate classes
     * </p>
     * 
     * @param block
     *            The mod block to register
     */
    private <T extends Block & IModWorldEntity> void registerBlock(T block, GuiHandler guiHandler) {
        // Perform initial registration of the item object itself
        GameRegistry.registerBlock(block, block.getIdentifier());

        registerWorldEntity(block, guiHandler);
    }

    /**
     * Common registration operations to items and blocks
     * 
     * @param entity
     *            The item/block to process registration for
     * @param guiHandler
     *            The gui handler to register any gui-related items/blocks with
     */
    private <T extends IModWorldEntity> void registerWorldEntity(T entity, GuiHandler guiHandler) {
        // Register craftable item's recipes
        if (entity instanceof ICraftableWorldEntity) {
            ICraftableWorldEntity craftable = (ICraftableWorldEntity) entity;

            for (ShapedCraftingRecipe recipe : craftable.getRecipes()) {
                GameRegistry.addRecipe(recipe.result(), recipe.recipe());
            }
        }

        // Register GUI hooks for GUI items
        if (entity instanceof IGuiWorldEntity) {
            int guiIdNum = guiHandler.registerGuiItem((IGuiWorldEntity) entity);

            guiId.put(entity.getIdentifier(), guiIdNum);
        }
    }

}
