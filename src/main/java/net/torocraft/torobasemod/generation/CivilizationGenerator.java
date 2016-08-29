package net.torocraft.torobasemod.generation;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CivilizationGenerator {
	
	public CivilizationGenerator() {
		
	}
	
	@SubscribeEvent
	public void onPopulateChunk(PopulateChunkEvent.Populate event) {
		if(event.isHasVillageGenerated()) {
			System.out.println("Village Has Generated");
		}
	}
}
