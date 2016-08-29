package net.torocraft.torobasemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torobasemod.block.ToroBaseModBlocks;
import net.torocraft.torobasemod.crafting.ToroBaseModRecipes;
import net.torocraft.torobasemod.entities.ToroEntities;
import net.torocraft.torobasemod.generation.CivilizationGenerator;
import net.torocraft.torobasemod.generation.WorldGenMageTowerPlacer;
import net.torocraft.torobasemod.generation.WorldGenMonolithPlacer;
import net.torocraft.torobasemod.item.ToroBaseModItems;

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
