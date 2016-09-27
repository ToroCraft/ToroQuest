package net.torocraft.toroquest.civilization;

import jaredbgreat.dldungeons.api.DLDEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CivilizationGeneratorHandlers {

	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Populate event) {
		if (!event.isHasVillageGenerated()) {
			return;
		}
		CivilizationUtil.registerNewCivilization(event.getWorld(), event.getChunkX(), event.getChunkZ());
	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void spawnDunguen(DLDEvent.placeDungeon event) {

	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void spawnDunguen(DLDEvent.BeforeBuild event) {
		// printFloorPlan(event);
	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void placeDungeonBlock(DLDEvent.AddTileEntitiesToRoom event) {

	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void placeDungeonBlock(DLDEvent.AddChestBlocksToRoom event) {

	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void placeDungeonBlock(DLDEvent.AddEntrance event) {

	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void placeDungeonBlock(DLDEvent.PlaceBlock event) {
		// replaceSpawnersWithDiamond(event);
	}

	protected void replaceSpawnersWithDiamond(DLDEvent.PlaceBlock event) {
		if (event.getBlock().getUnlocalizedName().equals("tile.mobSpawner")) {
			event.getWorld().setBlockState(event.getPos(), Blocks.DIAMOND_BLOCK.getDefaultState());
			event.setCanceled(true);
		}
	}

	protected void printFloorPlan(DLDEvent.BeforeBuild event) {
		int[][] room = event.getMapMatrix().room;
		StringBuilder s = new StringBuilder();
		s.append("\n");
		for (int i = 0; i < room.length; i++) {
			for (int j = 0; j < room.length; j++) {
				s.append(pad(room[i][j]));
			}
			s.append("\n");
		}
		System.out.println(s.toString());
	}

	private Object pad(int i) {
		if (i < 10) {
			return "0" + i;
		}
		return i + "";
	}

}
