package net.torocraft.fishing.events;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.torocraft.fishing.FishingMod;

public class EventHooks {

	@SubscribeEvent
	public void dropWorms(HarvestDropsEvent event) {
		if (isGrassBlock(event)) {
			if (event.isSilkTouching) {
				return;
			}
			BiomeGenBase biome = event.world.getBiomeGenForCoords(event.pos);
			if (isBiomeOfType(biome, Type.PLAINS)) {
				dropWormWithOdds(event, 12);
			} else if (isBiomeOfType(biome, Type.FOREST)) {
				dropWormWithOdds(event, 6);
			} else if (isBiomeOfType(biome, Type.JUNGLE)) {
				dropWormWithOdds(event, 1);
			}
		}
	}

	private boolean isBiomeOfType(BiomeGenBase biome, Type type) {
		return BiomeDictionary.isBiomeOfType(biome, type);
	}

	private void dropWormWithOdds(HarvestDropsEvent event, int maxOdds) {
		if (event.harvester.worldObj.rand.nextInt(maxOdds) == 0) {
			event.drops.add(new ItemStack(FishingMod.worms));
		}
	}

	private boolean isGrassBlock(HarvestDropsEvent event) {
		return event.state.getBlock().getUnlocalizedName().equals("tile.grass");
	}
	
}
