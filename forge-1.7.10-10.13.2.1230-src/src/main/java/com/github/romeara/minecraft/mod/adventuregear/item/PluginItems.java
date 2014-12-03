package com.github.romeara.minecraft.mod.adventuregear.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.adventuregear.util.Side;
import com.google.common.collect.ImmutableSet;

// TODO doc
// TODO move to common "library" plugin
public final class PluginItems {

    private static final Set<Block> TRANSPARENT_BLOCKS;

    static {
        ImmutableSet.Builder<Block> builder = ImmutableSet.builder();

        builder.add(Blocks.vine);
        builder.add(Blocks.tallgrass);
        builder.add(Blocks.deadbush);

        TRANSPARENT_BLOCKS = builder.build();
    }

    private PluginItems() {
    }

    /**
     * Determines if a block can be placed on the side of another block. Decompiled code named this function
     * "func_150936_a"
     */
    public static boolean canPlaceOnSide(Block blockToPlace, World world, int x, int y, int z, int side, EntityPlayer player, ItemStack held) {
        Block block = world.getBlock(x, y, z);

        Side sideRep = Side.getByMinecraftIndex(side, null);

        if (block == Blocks.snow_layer) {
            side = 1;
        } else if (!TRANSPARENT_BLOCKS.contains(block) && !block.isReplaceable(world, x, y, z)) {
            y = adjustY(sideRep, y);
            z = adjustZ(sideRep, z);
            x = adjustX(sideRep, x);
        }

        return world.canPlaceEntityOnSide(blockToPlace, x, y, z, false, side, (Entity) null, held);
    }

    // TODO doc
    public static boolean placeBlockOnSideIfValid(Block blockToPlace, ItemStack held, EntityPlayer player, World world, int x, int y, int z, int side) {
        Side sideRep = Side.getByMinecraftIndex(side, null);

        if (Side.BOTTOM.equals(sideRep) || Side.TOP.equals(sideRep)) {
            return false;
        } else if (!world.getBlock(x, y, z).getMaterial().isSolid()) {
            return false;
        } else {
            z = adjustZ(sideRep, z);
            x = adjustX(sideRep, x);

            if (!player.canPlayerEdit(x, y, z, side, held)) {
                return false;
            } else if (!blockToPlace.canPlaceBlockAt(world, x, y, z)) {
                return false;
            } else {
                if (!world.isRemote) {
                    world.setBlock(x, y, z, blockToPlace, side, 3);
                }
                --held.stackSize;

                return true;
            }
        }
    }

    // TODO
    public static boolean placeBlockOnTopIfValid(Block blockToPlace, ItemStack held, EntityPlayer player, World world, int x, int y, int z, int side) {
        Side sideRep = Side.getByMinecraftIndex(side, null);

        if (!Side.TOP.equals(sideRep)) {
            return false;
        } else if (!world.getBlock(x, y, z).getMaterial().isSolid()) {
            return false;
        } else {
            y = adjustY(sideRep, y);

            if (!player.canPlayerEdit(x, y, z, side, held)) {
                return false;
            } else if (!blockToPlace.canPlaceBlockAt(world, x, y, z)) {
                return false;
            } else {
                if (!world.isRemote) {
                    world.setBlock(x, y, z, blockToPlace, side, 3);
                }
                --held.stackSize;

                return true;
            }
        }
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public static boolean placeBlockIfValid(Item item, Block blockToPlace, ItemStack held, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        Side sideRep = Side.getByMinecraftIndex(side, null);

        // The metadata return represents that the block is replaceable
        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1; // Top of current
        } else if (!TRANSPARENT_BLOCKS.contains(block) && !block.isReplaceable(world, x, y, z)) {
            y = adjustY(sideRep, y);
            z = adjustZ(sideRep, z);
            x = adjustX(sideRep, x);
        }

        if (held.stackSize == 0) {
            return false;
        } else if (!player.canPlayerEdit(x, y, z, side, held)) {
            return false;
        } else if (y == 255 && blockToPlace.getMaterial().isSolid()) {
            return false;
        } else if (world.canPlaceEntityOnSide(blockToPlace, x, y, z, false, side, player, held)) {
            int currentItemDamage = item.getMetadata(held.getItemDamage());
            int blockMetadata = blockToPlace.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, currentItemDamage);

            if (placeBlockAt(blockToPlace, held, player, world, x, y, z, side, hitX, hitY, hitZ, blockMetadata)) {
                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F,
                        blockToPlace.stepSound.func_150496_b(), (blockToPlace.stepSound.getVolume() + 1.0F) / 2.0F,
                        blockToPlace.stepSound.getPitch() * 0.8F);
                --held.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Adjusts the coordinate based on the side interacted with
     * 
     * @param side
     *            The side the ;layer interacted with
     * @param y
     *            The original coordinate
     * @return The adjusted coordinate
     */
    private static int adjustY(Side side, int y) {
        if (Side.BOTTOM.equals(side)) {
            return y - 1;
        }

        if (Side.TOP.equals(side)) {
            return y + 1;
        }

        return y;
    }

    /**
     * Adjusts the coordinate based on the side interacted with
     * 
     * @param side
     *            The side the ;layer interacted with
     * @param z
     *            The original coordinate
     * @return The adjusted coordinate
     */
    private static int adjustZ(Side side, int z) {
        if (Side.NORTH.equals(side)) {
            return z - 1;
        }

        if (Side.SOUTH.equals(side)) {
            ++z;
        }

        return z;
    }

    /**
     * Adjusts the coordinate based on the side interacted with
     * 
     * @param side
     *            The side the ;layer interacted with
     * @param x
     *            The original coordinate
     * @return The adjusted coordinate
     */
    private static int adjustX(Side side, int x) {
        if (Side.WEST.equals(side)) {
            return x - 1;
        }

        if (Side.EAST.equals(side)) {
            return x + 1;
        }

        return x;
    }

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     * 
     * @param stack
     *            The item stack that was used to place the block. This can be changed inside the method.
     * @param player
     *            The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side
     *            The side the player (or machine) right-clicked on.
     */
    private static boolean placeBlockAt(Block blockToPlace, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
            float hitY,
            float hitZ, int metadata) {

        if (!world.setBlock(x, y, z, blockToPlace, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == blockToPlace) {
            blockToPlace.onBlockPlacedBy(world, x, y, z, player, stack);
            blockToPlace.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }

}
