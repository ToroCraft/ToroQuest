package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
@Mod.EventBusSubscriber
public class ItemSpicyChicken extends ItemFood {

	public static ItemSpicyChicken INSTANCE;

	public static final String NAME = "spicy_chicken";

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		INSTANCE = new ItemSpicyChicken();
		INSTANCE.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME));
		event.getRegistry().register(INSTANCE);
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0,
				new ModelResourceLocation("minecraft:cooked_chicken", "inventory"));
	}

	public ItemSpicyChicken() {
		super(6, 0.6F, true);
		setUnlocalizedName(NAME);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			int burnSeconds = 30;
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(12), burnSeconds * 20));
			player.setFire(burnSeconds);
		}
	}

}
