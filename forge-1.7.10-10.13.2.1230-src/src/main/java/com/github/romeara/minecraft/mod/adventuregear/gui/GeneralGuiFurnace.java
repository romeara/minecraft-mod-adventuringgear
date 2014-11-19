package com.github.romeara.minecraft.mod.adventuregear.gui;

import java.util.UUID;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.github.romeara.minecraft.mod.adventuregear.AdventuringGearMod;
import com.github.romeara.minecraft.mod.adventuregear.item.process.IFurnaceProcess;

public class GeneralGuiFurnace extends GuiContainer {

    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");

    private static final String __OBFID = "CL_00000758";

    private UUID processId;

    private boolean remote;

    public GeneralGuiFurnace(InventoryPlayer p_i1091_1_, UUID processId, boolean remote) {
        super(new GeneralContainerFurnace(p_i1091_1_, processId, remote));
        this.processId = processId;
        this.remote = remote;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        IFurnaceProcess tileFurnace = AdventuringGearMod.getInstance().getFurnaceProcessProvider().getProcess(remote, processId);
        String s = tileFurnace.hasCustomInventoryName() ? tileFurnace.getInventoryName() : I18n.format(tileFurnace.getInventoryName(),
                new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        IFurnaceProcess tileFurnace = AdventuringGearMod.getInstance().getFurnaceProcessProvider().getProcess(remote, processId);

        if (tileFurnace.isBurning()) {
            int i1 = tileFurnace.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
            i1 = tileFurnace.getCookProgressScaled(24);
            this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
        }
    }

}
