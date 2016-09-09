package net.torocraft.toroquest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.toroquest.block.ToroBaseModBlocks;
import net.torocraft.toroquest.crafting.ToroBaseModRecipes;
import net.torocraft.toroquest.entities.ToroEntities;
import net.torocraft.toroquest.generation.CivilizationGenerator;
import net.torocraft.toroquest.generation.WorldGenMageTowerPlacer;
import net.torocraft.toroquest.generation.WorldGenMonolithPlacer;
import net.torocraft.toroquest.item.ToroBaseModItems;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.TERRAIN_GEN_BUS.register(new CivilizationGenerator());
	}

	public void init(FMLInitializationEvent e) {
		ToroBaseModItems.init();
		ToroBaseModBlocks.init();
		ToroBaseModRecipes.init();
		WorldGenMageTowerPlacer.init();
		WorldGenMonolithPlacer.init();
		ToroEntities.init();
    }

	public void postInit(FMLPostInitializationEvent e) {
		
	}
}
