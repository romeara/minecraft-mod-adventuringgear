package com.github.romeara.minecraft.mod.adventuregear.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.github.romeara.minecraft.mod.adventuregear.AdventuringGearMod;
import com.github.romeara.minecraft.mod.adventuregear.ICraftableWorldEntity;
import com.github.romeara.minecraft.mod.adventuregear.IGuiWorldEntity;
import com.github.romeara.minecraft.mod.adventuregear.gui.GeneralContainerFurnace;
import com.github.romeara.minecraft.mod.adventuregear.gui.GeneralGuiFurnace;
import com.github.romeara.minecraft.mod.adventuregear.item.process.IFurnaceProcess;
import com.github.romeara.minecraft.mod.adventuregear.tileentity.PortableEntityFurnace;
import com.github.romeara.minecraft.mod.adventuregear.util.ShapedCraftingRecipe;

public class ItemCampingFurnace extends Item implements ICraftableWorldEntity, IGuiWorldEntity {

    /** Mod-unique identifier for the clipboard */
    private static final String NAME = "camping-furnace";

    private static Map<String, UUID> activeFurnaces = new HashMap<String, UUID>();

    public ItemCampingFurnace() {
        super();

        setUnlocalizedName(NAME);
        setTextureName(AdventuringGearMod.MODID + ":" + NAME);
        setCreativeTab(CreativeTabs.tabTools);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack heldStack, World world,
            EntityPlayer player) {
        if (heldStack.getTagCompound() == null) {
            heldStack.setTagCompound(new NBTTagCompound());
        }

        UUID processId = PortableEntityFurnace.getId(heldStack.getTagCompound());

        AdventuringGearMod instance = AdventuringGearMod.getInstance();
        activeFurnaces.put(player.getDisplayName(), processId);

        player.openGui(instance, instance.getGuiId(this), world, (int) player.posX, (int) player.posY, (int) player.posZ);

        return heldStack;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity holdingEntity, int inventorySlotIndex, boolean isHeldItem) {
        // Should only be done by server
        if (world.isRemote) {
            UUID id = PortableEntityFurnace.getId(itemStack.getTagCompound());

            IFurnaceProcess process = null;

            if (id != null) {
                process = AdventuringGearMod.getInstance().getFurnaceProcessProvider().getProcess(id);

                if (process == null && itemStack.getTagCompound() != null) {
                    process = new PortableEntityFurnace(itemStack.getTagCompound());
                }
            }

            if (process == null) {
                process = new PortableEntityFurnace();
            }

            process.runTick();

            // Write to cache and item
            AdventuringGearMod.getInstance().getFurnaceProcessProvider().updateProcess(process);

            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            process.writeToNBT(itemStack.getTagCompound());
        }

        super.onUpdate(itemStack, world, holdingEntity, inventorySlotIndex, isHeldItem);
    }

    @Override
    public String getIdentifier() {
        return NAME;
    }

    @Override
    public ShapedCraftingRecipe[] getRecipes() {
        ShapedCraftingRecipe recipe = new ShapedCraftingRecipe(AdventuringGearMod.MODID, NAME);

        // Coal and charcoal are the same item, the damage value determines which name and texture they use
        recipe.topMid(Blocks.stonebrick)
                .midLeft(Blocks.stonebrick).center(Items.coal).midRight(Blocks.stonebrick)
                .btmMid(Blocks.stonebrick);

        return new ShapedCraftingRecipe[] { recipe };
    }

    @Override
    public Container getServerGui(EntityPlayer player, World world, int x, int y, int z) {
        UUID stack = activeFurnaces.get(player.getDisplayName());

        if (stack != null) {
            Container container = new GeneralContainerFurnace(player.inventory, stack);
            return container;
        }

        return null;
    }

    @Override
    public GuiContainer getClientGui(EntityPlayer player, World world, int x, int y, int z) {
        UUID stack = activeFurnaces.get(player.getDisplayName());

        if (stack != null) {
            GuiContainer guiContainer = new GeneralGuiFurnace(player.inventory, stack);
            return guiContainer;
        }

        return null;
    }

}
