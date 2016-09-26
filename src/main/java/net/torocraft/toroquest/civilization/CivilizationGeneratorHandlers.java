package net.torocraft.toroquest.civilization;

import jaredbgreat.dldungeons.api.DLDEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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
	public void spawnDunguen(DLDEvent.Spawn event) {
		System.out.println("************ DLDEvent.Spawn");
	}

	@Method(modid = "dldungeonsjdg")
	@SubscribeEvent
	public void placeDungeonBlock(DLDEvent.PlaceBlock event) {

		System.out.println("event caught to place " + event.getBlock().getUnlocalizedName());

		if (event.getBlock().getUnlocalizedName().equals("tile.mobSpawner")) {
			System.out.println("*************** found spawner");
			event.getWorld().setBlockState(new BlockPos(event.getX(), event.getY(), event.getZ()), Blocks.DIAMOND_BLOCK.getDefaultState());
			event.setCanceled(true);
		}

	}

}
