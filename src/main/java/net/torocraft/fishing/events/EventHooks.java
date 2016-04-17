package net.torocraft.fishing.events;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.fishing.FishingMod;

public class EventHooks {

	@SubscribeEvent
	public void dropWorms(HarvestDropsEvent event) {
		
		if (isGrassBlock(event)) {
			if (event.isSilkTouching()) {
				return;
			}
			
			BiomeGenBase biome = event.getWorld().getBiomeGenForCoords(event.getPos());
			if (isBiomeOfType(biome, Type.PLAINS)) {
				dropWormWithOdds(event, 24);
			} else if (isBiomeOfType(biome, Type.FOREST)) {
				dropWormWithOdds(event, 12);
			} else if (isBiomeOfType(biome, Type.JUNGLE)) {
				dropWormWithOdds(event, 3);
			}
		}
	}

	@SubscribeEvent
	public void hookWormOnPole(PlayerEvent event) {
	}
	
	private boolean isBiomeOfType(BiomeGenBase biome, Type type) {
		return BiomeDictionary.isBiomeOfType(biome, type);
	}

	private void dropWormWithOdds(HarvestDropsEvent event, int maxOdds) {
		
		if (event.getHarvester().worldObj.rand.nextInt(maxOdds) == 0) {
			event.getDrops().add(newWorm(event));
		}
	}

	private ItemStack newWorm(HarvestDropsEvent event) {
		ItemStack worm = new ItemStack(FishingMod.worms);
		if (event.getHarvester().worldObj.rand.nextInt(100) == 0) {
			worm.addEnchantment(FishingMod.juicy, 1);
		}
		return worm;
	}

	private boolean isGrassBlock(HarvestDropsEvent event) {
		return event.getState().getBlock().getUnlocalizedName().equals("tile.grass");
	}
	
}
