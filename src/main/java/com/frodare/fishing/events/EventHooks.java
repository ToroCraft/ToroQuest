package com.frodare.fishing.events;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.frodare.fishing.FishingMod;

public class EventHooks {

	@SubscribeEvent
	public void dropWorms(HarvestDropsEvent event) {
		if (isGrassBlock(event)) {
			if (event.isSilkTouching) {
				return;
			}
			BiomeGenBase biome = event.world.getBiomeGenForCoords(event.pos);
			if (BiomeDictionary.isBiomeOfType(biome, Type.PLAINS)) {
				dropWormWithOdds(event, 12);
			} else if (BiomeDictionary.isBiomeOfType(biome, Type.FOREST)) {
				dropWormWithOdds(event, 6);
			} else if (BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE)) {
				dropWormWithOdds(event, 1);
			}
		}
	}

	private void dropWormWithOdds(HarvestDropsEvent event, int maxOdds) {
		if (event.harvester.worldObj.rand.nextInt(maxOdds) == 0) {
			event.drops.add(new ItemStack(FishingMod.worm));
		}
	}

	private boolean isGrassBlock(HarvestDropsEvent event) {
		return event.state.getBlock().getUnlocalizedName().equals("tile.grass");
	}
	
}
