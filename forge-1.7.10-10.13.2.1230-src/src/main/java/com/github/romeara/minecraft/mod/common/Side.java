package com.github.romeara.minecraft.mod.common;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

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

    public int minecraftIndex() {
        return minecraftIndex;
    }

    public static Side getByMinecraftIndex(int minecraftIndex, Side defaultSide) {
        Side side = LOOKUP.get(minecraftIndex);

        return (side != null ? side : defaultSide);
    }
}
