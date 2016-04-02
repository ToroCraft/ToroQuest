package net.torocraft.bouncermod;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.bouncermod.block.BounceModBlocks;
import net.torocraft.bouncermod.crafting.BounceModRecipes;
import net.torocraft.bouncermod.item.BounceModItems;
import net.torocraft.bouncermod.worldgen.BounceModWorldGen;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

    }

    public void init(FMLInitializationEvent e) {
        BounceModItems.init();
    	BounceModBlocks.init();
    	BounceModRecipes.init();
    	BounceModWorldGen.init();
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
