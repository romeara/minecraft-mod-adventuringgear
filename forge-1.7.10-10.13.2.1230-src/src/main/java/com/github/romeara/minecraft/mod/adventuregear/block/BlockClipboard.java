package com.github.romeara.minecraft.mod.adventuregear.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.adventuregear.AdventuringGearMod;
import com.github.romeara.minecraft.mod.adventuregear.gui.PortableContainerWorkbench;
import com.github.romeara.minecraft.mod.adventuregear.gui.PortableGuiCrafting;
import com.github.romeara.minecraft.mod.adventuregear.item.ItemClipboard;
import com.github.romeara.minecraft.mod.common.entity.Entities;
import com.github.romeara.minecraft.mod.common.entity.IGuiWorldEntity;
import com.github.romeara.minecraft.mod.common.entity.IModWorldEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Definition for a player-usable block corresponding to the "clipboard" item
 * 
 * <p>
 * The clipboard is a tool which, when held in the player's hand and "used" (default is right-click), opens a workbench
 * crafting interface. This tool is a relatively low-cost item, as it does not provide anything which significantly
 * increases the player's power, just the convenience of performing a required operation (crafting) in the game. Its
 * base resource cost ends up being exactly 2 logs
 * </p>
 * 
 * <p>
 * The block version allows the clipboard to double as an effective replacement for a standard workbench, removing the
 * necessity of maintaining both
 * </p>
 * 
 * @author romeara
 */
public class BlockClipboard extends Block implements IModWorldEntity, IGuiWorldEntity {

    /** Mod-unique identifier for the clipboard block */
    private static final String NAME = "block_clipboard";

    /** Description of the rendered texture for the front of the placed clipboard */
    private IIcon frontIcon = null;

    /** Description of the rendered texture for all non-front sides of the clipboard */
    private IIcon sideIcon = null;

    /**
     * Creates a block clipboard instance, setting its name and rendering information
     */
    public BlockClipboard() {
        super(Material.wood);
        disableStats();
        setBlockName(NAME);
        setBlockTextureName(NAME);
        float f = 0.25F;
        float f1 = 1.0F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaData) {
        if (side == metaData) {
            // Looking as "front"
            return frontIcon;
        }

        // Looking at a side
        return sideIcon;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int blockMeta = blockAccess.getBlockMetadata(x, y, z);
        float widthMin = 0.1875F;
        float widthMax = 0.8125F;
        float heightMin = 0.0625F;
        float heightMax = 0.875F;
        float depth = 0.0675F;

        com.github.romeara.minecraft.mod.common.Side sideRep =
                com.github.romeara.minecraft.mod.common.Side.getByMinecraftIndex(blockMeta, null);

        Entities.setBlockRenderBounds(this, sideRep, heightMin, heightMax, widthMin, widthMax, depth);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return ItemClipboard.getInstance();
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int sideInt = world.getBlockMetadata(x, y, z);

        com.github.romeara.minecraft.mod.common.Side side =
                com.github.romeara.minecraft.mod.common.Side.getByMinecraftIndex(sideInt, null);

        if (!Entities.isAttachedBlockSolid(world, x, y, z, side)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return ItemClipboard.getInstance();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        frontIcon = iconRegister.registerIcon(AdventuringGearMod.MODID + ":" + this.getTextureName() + "_front");
        sideIcon = iconRegister.registerIcon(AdventuringGearMod.MODID + ":" + this.getTextureName() + "_side");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            AdventuringGearMod instance = AdventuringGearMod.getInstance();
            player.openGui(instance, instance.getGuiId(this), world, x, y, z);
        }

        return true;
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

    /**
     * @return The registered instance of this block in the games
     */
    public static Block getInstance() {
        return GameRegistry.findBlock(AdventuringGearMod.MODID, NAME);
    }

}
