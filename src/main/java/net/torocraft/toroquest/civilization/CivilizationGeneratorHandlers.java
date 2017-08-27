package net.torocraft.toroquest.civilization;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CivilizationGeneratorHandlers {

	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Post event) {
		registerCiv(event);
	}

	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Pre event) {
		registerCiv(event);
	}

	protected void registerCiv(PopulateChunkEvent event) {
		if (!event.isHasVillageGenerated()) {
			return;
		}
		CivilizationUtil.registerNewCivilization(event.getWorld(), event.getChunkX(), event.getChunkZ());
	}


	/*
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void spawnDungeon(DLDEvent.PlaceDungeonBegin
	 * event) {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void spawnDungeon(DLDEvent.PlaceDungeonFinish
	 * event) {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void spawnDungeon(DLDEvent.AfterChestTileEntity
	 * event) { addArmorToSlot2(event); }
	 * 
	 * protected void addArmorToSlot2(DLDEvent.AfterChestTileEntity event) {
	 * event.getContents().setInventorySlotContents(2, new
	 * ItemStack(getRandomArmor(event.getRandom()))); }
	 * 
	 * private Item getRandomArmor(Random rand) { int i = rand.nextInt(12);
	 * 
	 * switch(i) { case 0: return ItemRoyalArmor.chestplateItem; case 1: return
	 * ItemRoyalArmor.leggingsItem; case 2: return ItemRoyalArmor.helmetItem;
	 * case 3: return ItemRoyalArmor.bootsItem;
	 * 
	 * case 4: return ItemSamuraiArmor.chestplateItem; case 5: return
	 * ItemSamuraiArmor.leggingsItem; case 6: return
	 * ItemSamuraiArmor.helmetItem; case 7: return ItemSamuraiArmor.bootsItem;
	 * 
	 * case 8: return ItemReinforcedDiamondArmor.chestplateItem; case 9: return
	 * ItemReinforcedDiamondArmor.leggingsItem; case 10: return
	 * ItemReinforcedDiamondArmor.helmetItem; default: return
	 * ItemReinforcedDiamondArmor.bootsItem; }
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void spawnDungeon(DLDEvent.BeforeBuild event) {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void handle(DLDEvent.BeforePlaceSpawner event) {
	 * 
	 * if (event.getWorld().rand.nextInt(100) > 50) { return; }
	 * 
	 * event.setCanceled(true); event.getWorld().setBlockState(event.getPos(),
	 * BlockToroSpawner.INSTANCE.getDefaultState()); TileEntityToroSpawner
	 * spawner = (TileEntityToroSpawner)
	 * event.getWorld().getTileEntity(event.getPos());
	 * spawner.setTriggerDistance(8); spawner.setSpawnRadius(10); int count =
	 * 30; List<String> entities = new ArrayList<String>(count); for (int i = 0;
	 * i < count; i++) { entities.add("Zombie"); }
	 * spawner.setEntityIds(entities); }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void
	 * placeDungeonBlock(DLDEvent.AddTileEntitiesToRoom event) {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void
	 * placeDungeonBlock(DLDEvent.AddChestBlocksToRoom event) {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void placeDungeonBlock(DLDEvent.AddEntrance event)
	 * {
	 * 
	 * }
	 * 
	 * @Method(modid = "dldungeonsjdg")
	 * 
	 * @SubscribeEvent public void placeDungeonBlock(DLDEvent.PlaceBlock event)
	 * {
	 * 
	 * }
	 * 
	 * protected void replaceSpawnersWithDiamond(DLDEvent.PlaceBlock event) { if
	 * (event.getBlock().getUnlocalizedName().equals("tile.mobSpawner")) {
	 * event.getWorld().setBlockState(event.getPos(),
	 * Blocks.DIAMOND_BLOCK.getDefaultState()); event.setCanceled(true); } }
	 * 
	 * protected void printFloorPlan(DLDEvent.BeforeBuild event) { int[][] room
	 * = event.getMapMatrix().room; StringBuilder s = new StringBuilder();
	 * s.append("\n"); for (int i = 0; i < room.length; i++) { for (int j = 0; j
	 * < room.length; j++) { s.append(pad(room[i][j])); } s.append("\n"); }
	 * System.out.println(s.toString()); }
	 */

	private Object pad(int i) {
		if (i < 10) {
			return "0" + i;
		}
		return i + "";
	}

}
