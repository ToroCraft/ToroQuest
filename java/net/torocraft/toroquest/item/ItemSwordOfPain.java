package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
@Mod.EventBusSubscriber
public class ItemSwordOfPain extends ItemSword {

	public static ItemSwordOfPain INSTANCE;

	public static final String NAME = "sword_of_pain";

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		INSTANCE = new ItemSwordOfPain();
		INSTANCE.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME));
		event.getRegistry().register(INSTANCE);
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0,
				new ModelResourceLocation("minecraft:iron_sword", "inventory"));
	}

	public ItemSwordOfPain() {
		super(ToolMaterial.IRON);
		setUnlocalizedName(NAME);
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		if (thisToolWasUsed(event)) {
			alterDamage(event);
		}
	}

	private boolean thisToolWasUsed(LivingHurtEvent event) {
		if (event.getSource() == null || !(event.getSource().getTrueSource() instanceof EntityPlayer)) {
			return false;
		}
		EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
		return player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == INSTANCE;
	}

	private void alterDamage(LivingHurtEvent event) {
		float amount = event.getAmount();
		event.setAmount(amount * 2);
		event.getSource().getTrueSource().attackEntityFrom(event.getSource(), amount / 2);
	}
}
