package com.github.romeara.minecraft.mod.adventuregear.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

/**
 * Extension of {@link GuiContainer} based off of {@link GuiCrafting}. Adjusted to use
 * "portable" workbench container, which removes the requirement for the player to be
 * in a certain range of a workbench
 * 
 * @author romeara
 */
public class PortableGuiCrafting extends GuiContainer {

    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");

    private static final String __OBFID = "CL_00000750";

    /**
     * Creates a portable gui instance
     */
    public PortableGuiCrafting(InventoryPlayer player, World world, int x, int y, int z) {
        super(new PortableContainerWorkbench(player, world, x, y, z));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
