package com.github.romeara.minecraft.mod.adventuregear.item;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.adventuregear.AdventuringGearMod;
import com.github.romeara.minecraft.mod.adventuregear.block.BlockClipboard;
import com.github.romeara.minecraft.mod.adventuregear.gui.PortableContainerWorkbench;
import com.github.romeara.minecraft.mod.adventuregear.gui.PortableGuiCrafting;
import com.github.romeara.minecraft.mod.common.ShapedCraftingRecipe;
import com.github.romeara.minecraft.mod.common.entity.Entities;
import com.github.romeara.minecraft.mod.common.entity.ICraftableWorldEntity;
import com.github.romeara.minecraft.mod.common.entity.IGuiWorldEntity;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Definition for a player-usable item called a "clipboard"
 * 
 * <p>
 * The clipboard is a tool which, when held in the player's hand and "used" (default is right-click), opens a workbench
 * crafting interface. This tool is a relatively low-cost item, as it does not provide anything which significantly
 * increases the player's power, just the convenience of performing a required operation (crafting) in the game. Its
 * base resource cost ends up being exactly 2 logs
 * </p>
 * 
 * @author romeara
 */
public class ItemClipboard extends Item implements ICraftableWorldEntity, IGuiWorldEntity {

    /** Mod-unique identifier for the clipboard */
    private static final String NAME = "clipboard";

    /**
     * Creates an item clipboard instance, setting its creative tab, standard name, and texture
     */
    public ItemClipboard() {
        super();

        setUnlocalizedName(NAME);
        setTextureName(AdventuringGearMod.MODID + ":" + NAME);
        setCreativeTab(CreativeTabs.tabTools);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack heldStack, World world, EntityPlayer player) {
        // Remote will automatically be dealt with automatically by the system and the open GUI call
        if (!world.isRemote && !player.isSneaking()) {
            AdventuringGearMod instance = AdventuringGearMod.getInstance();
            player.openGui(instance, instance.getGuiId(this), world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }

        return heldStack;
    }

    @Override
    public boolean onItemUse(ItemStack held, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        // Place a block in the world if right-clicking
        if (player.isSneaking()) {
            boolean used = false;

            Block clipboardBlock = BlockClipboard.getInstance();

            if (clipboardBlock != null && Entities.place(world, x, y, z, side, clipboardBlock, player, held)) {
                held.stackSize = held.stackSize - 1;
                used = true;
            }

            return used;
        }

        return super.onItemUse(held, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public String getIdentifier() {
        return NAME;
    }

    @Override
    public Container getServerGui(EntityPlayer player, World world, int x, int y, int z) {
        Container container = new PortableContainerWorkbench(player.inventory, world, x, y, z);
        return container;
    }

    @Override
    public GuiContainer getClientGui(EntityPlayer player, World world, int x, int y, int z) {
        GuiContainer guiContainer = new PortableGuiCrafting(player.inventory, world, x, y, z);
        return guiContainer;
    }

    @Override
    public ShapedCraftingRecipe[] getRecipes() {
        ShapedCraftingRecipe recipe = new ShapedCraftingRecipe(AdventuringGearMod.MODID, NAME);

        recipe.topMid(Blocks.planks)
                .midLeft(Items.stick).center(Blocks.planks).midRight(Items.stick)
                .btmLeft(Items.stick).btmMid(Blocks.crafting_table).btmRight(Items.stick);

        return new ShapedCraftingRecipe[] { recipe };
    }

    /**
     * @return The registered instance of this item in the games
     */
    public static Item getInstance() {
        return GameRegistry.findItem(AdventuringGearMod.MODID, NAME);
    }

}
