package com.github.romeara.minecraft.mod.common;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Represents rendered sides of a block in the game world
 * 
 * <p>
 * Stores the minecraft integer representation of the sides
 * </p>
 * 
 * @author romeara
 */
public enum Side {

    TOP(1),
    BOTTOM(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4);

    private static final Map<Integer, Side> LOOKUP;

    static {
        ImmutableMap.Builder<Integer, Side> builder = ImmutableMap.builder();

        for (Side side : values()) {
            builder.put(side.minecraftIndex, side);
        }

        LOOKUP = builder.build();
    }

    private final int minecraftIndex;

    private Side(int minecraftIndex) {
        this.minecraftIndex = minecraftIndex;
    }

    /**
     * @return The internal integer game representation of the side being referenced
     */
    public int minecraftIndex() {
        return minecraftIndex;
    }

    /**
     * Looks up the enumerated side representation based on the minecraft integer representation
     * 
     * @param minecraftIndex
     *            The minecraft representation of the side
     * @param defaultSide
     *            The value to return if no match is found to the provided integer, may be null
     * @return The side representation, or the the provided default if no match is found
     */
    public static Side getByMinecraftIndex(int minecraftIndex, Side defaultSide) {
        Side side = LOOKUP.get(minecraftIndex);

        return (side != null ? side : defaultSide);
    }
}
