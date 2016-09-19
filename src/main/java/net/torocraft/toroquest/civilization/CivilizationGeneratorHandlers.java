package net.torocraft.toroquest.civilization;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CivilizationGeneratorHandlers {
	
	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Populate event) {
		if (!event.isHasVillageGenerated()) {
			return;
		}
		CivilizationUtil.registerNewCivilization(event.getWorld(), event.getChunkX(), event.getChunkZ());
	}

}
