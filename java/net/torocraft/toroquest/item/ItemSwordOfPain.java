package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;

public class ItemSwordOfPain extends ItemSword {

	public static ItemSwordOfPain INSTANCE;

	public static final String NAME = "sword_of_pain";

	public static void init() {
		INSTANCE = new ItemSwordOfPain();
		GameRegistry.register(INSTANCE, new ResourceLocation(ToroQuest.MODID, NAME));
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, new ModelResourceLocation("iron_sword", "inventory"));
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
		if (event.getSource() == null || !(event.getSource().getEntity() instanceof EntityPlayer)) {
			return false;
		}
		EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
		return player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == INSTANCE;
	}

	private void alterDamage(LivingHurtEvent event) {
		float amount = event.getAmount();
		event.setAmount(amount * 2);
		event.getSource().getEntity().attackEntityFrom(event.getSource(), amount / 2);
	}
}
