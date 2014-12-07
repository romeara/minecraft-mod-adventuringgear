package com.github.romeara.minecraft.mod.common.entity;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.common.Side;
import com.google.common.collect.ImmutableSet;

/**
 * Provides common operational functions related to blocks and items in the world
 * 
 * @author romeara
 */
public final class Entities {

    /** Reference of blocks considered transparent for the purposes of placing other blocks */
    private static final Set<Block> TRANSPARENT_BLOCKS;

    static {
        ImmutableSet.Builder<Block> builder = ImmutableSet.builder();

        builder.add(Blocks.vine);
        builder.add(Blocks.tallgrass);
        builder.add(Blocks.deadbush);

        TRANSPARENT_BLOCKS = builder.build();
    }

    private Entities() {
        // Prevent instantiation of utility class
    }

    /**
     * Sets the rendering boundaries for a block based on the side it is placed on
     * 
     * @param block
     *            The block being placed
     * @param sideRep
     *            The side being placed on
     * @param heightMin
     *            The lower bound for the height of the block [0.0F, 1.0F]
     * @param heightMax
     *            The upper bound for the height of the block [0.0F, 1.0F]
     * @param widthMin
     *            The lower bound for the width of the block [0.0F, 1.0F]
     * @param widthMax
     *            The upper bound for the width of the block [0.0F, 1.0F]
     * @param depth
     *            The depth of render block [0.0F, 1.0F]
     */
    public static void setBlockRenderBounds(Block block, Side sideRep, float heightMin, float heightMax, float widthMin, float widthMax, float depth) {
        float minX = 0.0F;
        float maxX = 1.0F;
        float minY = 0.0F;
        float maxY = 1.0F;
        float minZ = 0.0F;
        float maxZ = 1.0F;

        if (Side.TOP.equals(sideRep)) {
            minX = widthMin;
            minY = 0.0F;
            minZ = heightMin;
            maxX = widthMax;
            maxY = depth;
            maxZ = heightMax;
        }

        if (Side.BOTTOM.equals(sideRep)) {
            minX = widthMin;
            minY = 1.0F - depth;
            minZ = heightMin;
            maxX = widthMax;
            maxY = 1.0F;
            maxZ = heightMax;
        }

        if (Side.NORTH.equals(sideRep)) {
            minX = widthMin;
            minY = heightMin;
            minZ = 1.0F - depth;
            maxX = widthMax;
            maxY = heightMax;
            maxZ = 1.0F;
        }

        if (Side.SOUTH.equals(sideRep)) {
            minX = widthMin;
            minY = heightMin;
            minZ = 0.0F;
            maxX = widthMax;
            maxY = heightMax;
            maxZ = depth;
        }

        if (Side.WEST.equals(sideRep)) {
            minX = 1.0F - depth;
            minY = heightMin;
            minZ = widthMin;
            maxX = 1.0F;
            maxY = heightMax;
            maxZ = widthMax;
        }

        if (Side.EAST.equals(sideRep)) {
            minX = 0.0F;
            minY = heightMin;
            minZ = widthMin;
            maxX = depth;
            maxY = heightMax;
            maxZ = widthMax;
        }

        block.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Checks if the block attached to the provided side of a block is a solid block
     * 
     * @param world
     *            The world to check within
     * @param x
     *            The x coordinate of the block
     * @param y
     *            The y coordinate of the block
     * @param z
     *            The z coordinate of the block
     * @param side
     *            The side to checked the attached block for
     * @return True if the block attached to the side indicated is solid, false otherwise
     */
    public static boolean isAttachedBlockSolid(World world, int x, int y, int z, Side side) {
        boolean solid = false;

        int neighborX = x;
        int neighborY = y;
        int neighborZ = z;

        if (Side.TOP.equals(side)) {
            neighborY--;
        }

        if (Side.BOTTOM.equals(side)) {
            neighborY++;
        }

        if (Side.NORTH.equals(side)) {
            neighborZ++;
        }

        if (Side.SOUTH.equals(side)) {
            neighborZ--;
        }

        if (Side.WEST.equals(side)) {
            neighborX++;
        }

        if (Side.EAST.equals(side)) {
            neighborX--;
        }

        return world.getBlock(neighborX, neighborY, neighborZ).getMaterial().isSolid();
    }

    /**
     * Places a block in the world at the specified coordinates if the specified coordinates are a valid placement
     * location
     * 
     * <p>
     * Does not decrement the held stack size if the block is placed
     * </p>
     * 
     * @param world
     *            The world the block will be placed in
     * @param x
     *            The x coordinate to place on
     * @param y
     *            The y coordinate to place on
     * @param z
     *            The z coordinate to place on
     * @param side
     *            The side to place on
     * @param blockToPlace
     *            The block to place into the world
     * @param player
     *            The player placing the block
     * @param held
     *            The item stack currently held by the player
     * @return True if the block was placed into the world, false otherwise
     */
    public static boolean place(World world, int x, int y, int z, int side, Block blockToPlace, EntityPlayer player, ItemStack held) {
        Block block = world.getBlock(x, y, z);

        Side sideRep = Side.getByMinecraftIndex(side, null);

        if (block == Blocks.snow_layer) {
            sideRep = Side.TOP;
        }

        if (!block.getMaterial().isSolid()) {
            return false;
        } else {
            if (!TRANSPARENT_BLOCKS.contains(block) && !block.isReplaceable(world, x, y, z)) {
                x = adjustX(sideRep, x);
                y = adjustY(sideRep, y);
                z = adjustZ(sideRep, z);
            }

            if (!player.canPlayerEdit(x, y, z, sideRep.minecraftIndex(), held)) {
                return false;
            } else if (!blockToPlace.canPlaceBlockAt(world, x, y, z)) {
                return false;
            } else {
                boolean placed = true;

                if (!world.isRemote) {
                    placed = world.setBlock(x, y, z, blockToPlace, sideRep.minecraftIndex(), 3);
                }

                if (placed) {
                    world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F,
                            blockToPlace.stepSound.func_150496_b(),
                            (blockToPlace.stepSound.getVolume() + 1.0F) / 2.0F,
                            blockToPlace.stepSound.getPitch() * 0.8F);

                    if (world.getBlock(x, y, z) == blockToPlace) {
                        blockToPlace.onBlockPlacedBy(world, x, y, z, player, held);
                        blockToPlace.onPostBlockPlaced(world, x, y, z, sideRep.minecraftIndex());
                    }
                }

                return true;
            }
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

}
