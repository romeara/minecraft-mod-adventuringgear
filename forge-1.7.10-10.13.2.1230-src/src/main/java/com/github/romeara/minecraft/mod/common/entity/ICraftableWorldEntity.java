package com.github.romeara.minecraft.mod.common.entity;

import com.github.romeara.minecraft.mod.common.ShapedCraftingRecipe;

/**
 * Represents an item/block which may be crafted by the player
 * 
 * @author romeara
 */
public interface ICraftableWorldEntity extends IModWorldEntity {

    /**
     * @return An array of any shaped recipes the player may use to create the item. An empty array if there are no
     *         shaped recipes
     */
    ShapedCraftingRecipe[] getRecipes();

}
