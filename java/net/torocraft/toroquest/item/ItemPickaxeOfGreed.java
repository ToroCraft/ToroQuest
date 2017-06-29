package net.torocraft.toroquest.item;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
@Mod.EventBusSubscriber
public class ItemPickaxeOfGreed extends ItemPickaxe {
  	//TODO
	//public static Achievement JACKPOT_ACHIEVEMNT = new Achievement("jackpot", "jackpot", 0, 0, Items.IRON_PICKAXE, null).registerStat();

	public static ItemPickaxeOfGreed INSTANCE;
	public static final String NAME = "pickaxe_of_greed";

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		INSTANCE = new ItemPickaxeOfGreed();
		INSTANCE.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME));
		event.getRegistry().register(INSTANCE);
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0,
				new ModelResourceLocation("minecraft:iron_pickaxe", "inventory"));
	}

	public ItemPickaxeOfGreed() {
		super(Item.ToolMaterial.IRON);
		setUnlocalizedName(NAME);
	}

	@SubscribeEvent
	public void onHarvest(HarvestDropsEvent event) {
		if (thisToolWasUsed(event)) {
			alterDrops(event.getWorld().rand, event.getDrops(), event.getHarvester());
		}
	}

	private boolean thisToolWasUsed(HarvestDropsEvent event) {
		return event.getHarvester() != null && event.getHarvester().getHeldItemMainhand() != null
				&& event.getHarvester().getHeldItemMainhand().getItem() == INSTANCE && !event.getWorld().isRemote;
	}

	private void alterDrops(Random rand, List<ItemStack> drops, EntityPlayer player) {

		if (drops.size() < 1) {
			return;
		}

		ItemStack stack = drops.get(0);
		int count = stack.getCount();

		if (rand.nextInt(77) == 0) {
			//player.addStat(JACKPOT_ACHIEVEMNT);
			stack.setCount(stack.getMaxStackSize());
		} else if (rand.nextInt(10) == 0) {
			stack.setCount(count + (rand.nextInt(3) + 3));
		} else {
			drops.clear();
			drops.add(new ItemStack(Items.COAL, count));
		}
	}

}
