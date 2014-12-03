package com.github.romeara.minecraft.mod.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

/**
 * Utility class which allows more natural construction of Minecraft crafting recipes
 * 
 * @author romeara
 */
public class ShapedCraftingRecipe {

    // Only need 9 possible keys, as there can only be 9 types of items
    private static final Character[] RECIPE_KEYS = new Character[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };

    /** The thing (or things) the recipe makes */
    private ItemStack result = null;

    /** Array representing the items/blocks in the recipe. Null indicates no item */
    private Object[][] recipe = new Object[3][3];

    /**
     * Creates a new recipe with a result of a single item
     * 
     * @param resultItemModId
     *            The ID of the mod which provides the item/block
     * @param resultItemName
     *            The registered name of the item/block
     */
    public ShapedCraftingRecipe(String resultItemModId, String resultItemName) {
        this(resultItemModId, resultItemName, 1);
    }

    /**
     * Creates a new recipe with a result of a multiple items
     * 
     * @param resultItemModId
     *            The ID of the mod which provides the item/block
     * @param resultItemName
     *            The registered name of the item/block
     * @param stackSize
     *            The number of items the recipe makes
     */
    public ShapedCraftingRecipe(String resultItemModId, String resultItemName, int stackSize) {
        Item resultItem = GameRegistry.findItem(resultItemModId, resultItemName);

        if (resultItem != null) {
            result = new ItemStack(resultItem, stackSize);
        } else {
            // Might be a block
            Block resultBlock = GameRegistry.findBlock(resultItemModId, resultItemName);

            if (resultBlock != null) {
                result = new ItemStack(resultBlock, stackSize);
            } else {
                throw new IllegalArgumentException("Could not find result item/block " + resultItemModId + ":" + resultItemName);
            }
        }
    }

    /**
     * Creates a new recipe with a result of a single item
     * 
     * @param item
     *            The resulting item
     */
    public ShapedCraftingRecipe(Item item) {
        result = new ItemStack(item, 1);
    }

    /**
     * Creates a new recipe with a result of multiple items
     * 
     * @param item
     *            The resulting item
     * @param stackSize
     *            The number of items the recipe makes
     */
    public ShapedCraftingRecipe(Item item, int stackSize) {
        result = new ItemStack(item, stackSize);
    }

    /**
     * Creates a new recipe with a result of a single item
     * 
     * @param block
     *            The resulting block
     */
    public ShapedCraftingRecipe(Block block) {
        result = new ItemStack(block, 1);
    }

    /**
     * Creates a new recipe with a result of multiple items
     * 
     * @param block
     *            The resulting block
     * @param stackSize
     *            The number of items the recipe makes
     */
    public ShapedCraftingRecipe(Block block, int stackSize) {
        result = new ItemStack(block, stackSize);
    }

    /**
     * Sets the top left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topLeft(Item entity) {
        recipe[0][0] = entity;

        return this;
    }

    /**
     * Sets the top middle required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topMid(Item entity) {
        recipe[0][1] = entity;

        return this;
    }

    /**
     * Sets the top right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topRight(Item entity) {
        recipe[0][2] = entity;

        return this;
    }

    /**
     * Sets the middle left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe midLeft(Item entity) {
        recipe[1][0] = entity;

        return this;
    }

    /**
     * Sets the center required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe center(Item entity) {
        recipe[1][1] = entity;

        return this;
    }

    /**
     * Sets the middle right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe midRight(Item entity) {
        recipe[1][2] = entity;

        return this;
    }

    /**
     * Sets the bottom left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmLeft(Item entity) {
        recipe[2][0] = entity;

        return this;
    }

    /**
     * Sets the bottom middle required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmMid(Item entity) {
        recipe[2][1] = entity;

        return this;
    }

    /**
     * Sets the bottom right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmRight(Item entity) {
        recipe[2][2] = entity;

        return this;
    }

    /**
     * Sets the top left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topLeft(Block entity) {
        recipe[0][0] = entity;

        return this;
    }

    /**
     * Sets the top middle required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topMid(Block entity) {
        recipe[0][1] = entity;

        return this;
    }

    /**
     * Sets the top right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe topRight(Block entity) {
        recipe[0][2] = entity;

        return this;
    }

    /**
     * Sets the middle left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe midLeft(Block entity) {
        recipe[1][0] = entity;

        return this;
    }

    /**
     * Sets the center required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe center(Block entity) {
        recipe[1][1] = entity;

        return this;
    }

    /**
     * Sets the middle right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe midRight(Block entity) {
        recipe[1][2] = entity;

        return this;
    }

    /**
     * Sets the bottom left required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmLeft(Block entity) {
        recipe[2][0] = entity;

        return this;
    }

    /**
     * Sets the bottom middle required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmMid(Block entity) {
        recipe[2][1] = entity;

        return this;
    }

    /**
     * Sets the bottom right required ingredient
     * 
     * @param entity
     *            The ingredient
     * @return This crafting recipe instance
     */
    public ShapedCraftingRecipe btmRight(Block entity) {
        recipe[2][2] = entity;

        return this;
    }

    /**
     * @return A representation of the result of recipe
     */
    public ItemStack result() {
        return result;
    }

    /**
     * @return Minecraft representation of the recipe
     */
    public Object[] recipe() {
        Map<UniqueIdentifier, Character> keyConversion = getIdConversion();

        List<Object> recipeArray = new ArrayList<Object>();

        recipeArray.add(getRow(recipe[0], keyConversion));
        recipeArray.add(getRow(recipe[1], keyConversion));
        recipeArray.add(getRow(recipe[2], keyConversion));

        // Add key conversion information
        for (Entry<UniqueIdentifier, Character> entry : keyConversion.entrySet()) {
            recipeArray.add(entry.getValue());
            recipeArray.add(getEntity(entry.getKey()));
        }

        return recipeArray.toArray(new Object[recipeArray.size()]);
    }

    /**
     * Creates a Minecraft representation of a recipe row
     * 
     * @param row
     *            Array of 3 objects representing the require items in the row
     * @param keyConversion
     *            Map relating items to Minecraft recipe placeholders
     * @return String representing the recipe row
     */
    private String getRow(Object[] row, Map<UniqueIdentifier, Character> keyConversion) {
        StringBuilder rowString = new StringBuilder();

        for (Object item : row) {
            if (item != null) {
                Character key = keyConversion.get(getId(item));

                rowString.append(key);
            } else {
                rowString.append(" ");
            }
        }

        return rowString.toString();
    }

    /**
     * Looks up the item/block associated with the identifier
     * 
     * @param identifier
     *            Unique identifier for an item/block in minecraft
     * @return The entity if found, null otherwise
     */
    private Object getEntity(UniqueIdentifier identifier) {
        Object entity = GameRegistry.findItem(identifier.modId, identifier.name);

        if (entity == null) {
            entity = GameRegistry.findBlock(identifier.modId, identifier.name);
        }

        return entity;
    }

    /**
     * Gets the unique ID of the given item/block
     * 
     * @param entity
     *            The item/block to get the unique ID for
     * @return The unique id, or null if no ID was found
     */
    private UniqueIdentifier getId(Object entity) {
        if (entity instanceof Item) {
            return GameRegistry.findUniqueIdentifierFor((Item) entity);
        } else if (entity instanceof Block) {
            return GameRegistry.findUniqueIdentifierFor((Block) entity);
        } else {
            return null;
        }
    }

    /**
     * Creates a map of items to Minecraft recipe placeholders
     * 
     * @return Map relating entities used in the recipe to single-character Minecraft recipe placeholders
     */
    private Map<UniqueIdentifier, Character> getIdConversion() {
        Map<UniqueIdentifier, Character> keyConversion = new HashMap<UniqueIdentifier, Character>();
        int currentKey = 0;

        for (Object[] itemSet : recipe) {
            for (Object item : itemSet) {
                if (item != null) {
                    UniqueIdentifier itemId = getId(item);
                    Character key = keyConversion.get(itemId);

                    if (key == null) {
                        key = RECIPE_KEYS[currentKey];
                        currentKey++;
                        keyConversion.put(itemId, key);
                    }
                }
            }
        }

        return keyConversion;
    }
}
