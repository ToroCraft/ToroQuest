package net.torocraft.toroquest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.toroquest.block.ToroQuestBlocks;
import net.torocraft.toroquest.crafting.ToroQuestRecipes;
import net.torocraft.toroquest.entities.ToroQuestEntities;
import net.torocraft.toroquest.generation.CivilizationGenerator;
import net.torocraft.toroquest.generation.WorldGenMageTowerPlacer;
import net.torocraft.toroquest.generation.WorldGenMonolithPlacer;
import net.torocraft.toroquest.item.ToroQuestItems;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.TERRAIN_GEN_BUS.register(new CivilizationGenerator());
	}

	public void init(FMLInitializationEvent e) {
		ToroQuestItems.init();
		ToroQuestBlocks.init();
		ToroQuestRecipes.init();
		WorldGenMageTowerPlacer.init();
		WorldGenMonolithPlacer.init();
		ToroQuestEntities.init();

		EventHandlers handlers = new EventHandlers();
		MinecraftForge.EVENT_BUS.register(handlers);
    }

	public void postInit(FMLPostInitializationEvent e) {
		
	}
}
