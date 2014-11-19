package com.github.romeara.minecraft.mod.adventuregear.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityFurnace;

// TODO
public class PortableTileEntityFurance extends TileEntityFurnace {

    public PortableTileEntityFurance() {
        super();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }
}
