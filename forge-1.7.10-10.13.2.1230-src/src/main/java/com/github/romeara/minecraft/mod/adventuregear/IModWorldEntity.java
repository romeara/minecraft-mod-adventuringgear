package com.github.romeara.minecraft.mod.adventuregear;

/**
 * Represents an item/block contributed to Minecraft by a mod.
 * 
 * <p>
 * Mainly used within general handling code built around item/block registration at startup to reduce duplicate
 * definitions algorithms
 * </p>
 * 
 * @author romeara
 */
public interface IModWorldEntity {

    /**
     * @return The identifier for the item (unique within the context of the mod)
     */
    String getIdentifier();
}
