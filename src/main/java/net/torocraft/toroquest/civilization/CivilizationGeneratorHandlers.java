package net.torocraft.toroquest.civilization;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;

public class CivilizationGeneratorHandlers {
	
	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Populate event) {
		if (!event.isHasVillageGenerated()) {
			return;
		}
		CivilizationsWorldSaveData civData = CivilizationsWorldSaveData.get(event.getWorld());
		Civilization civ = civData.getCivilizationAt(event.getChunkX(), event.getChunkZ());
		if (civ != null) {
			return;
		}
		civ = Civilization.values()[event.getRand().nextInt(Civilization.values().length)];
		civData.registerBorder(event.getChunkX(), event.getChunkZ(), civ);
	}

}
