package com.github.romeara.minecraft.mod.common.entity;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.common.Side;
import com.google.common.collect.ImmutableSet;

// TODO doc
// TODO move to common "library" plugin
public final class Entities {

    private static final Set<Block> TRANSPARENT_BLOCKS;

    static {
        ImmutableSet.Builder<Block> builder = ImmutableSet.builder();

        builder.add(Blocks.vine);
        builder.add(Blocks.tallgrass);
        builder.add(Blocks.deadbush);

        TRANSPARENT_BLOCKS = builder.build();
    }

    private Entities() {
    }

    // TODO
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

    // TODO
    public static boolean place(World world, int x, int y, int z, int side, Block blockToPlace, EntityPlayer player, ItemStack held) {
        Block block = world.getBlock(x, y, z);

        Side sideRep = Side.getByMinecraftIndex(side, null);

        if (block == Blocks.snow_layer) {
            sideRep = Side.TOP;
        }

        if (!world.getBlock(x, y, z).getMaterial().isSolid()) {
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
