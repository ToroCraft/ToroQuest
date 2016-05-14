package net.torocraft.torobasemod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.entity.projectile.EntityTorchArrow;
import net.torocraft.torobasemod.entity.projectile.RenderTorchArrow;
import net.torocraft.torobasemod.item.armor.ItemBullArmor;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamondArmor;
import net.torocraft.torobasemod.item.armor.ItemKingArmor;
import net.torocraft.torobasemod.item.armor.ItemSamuraiArmor;
import net.torocraft.torobasemod.item.projectile.ItemTorchArrow;

public class ToroBaseModItems {

	public static final void init() {
		initTools();
		initArmor();
	}

	private static void initTools() {
		ItemTorchArrow.init();
	}

	private static void initArmor() {
		ItemKingArmor.init();
		ItemBullArmor.init();
		ItemHeavyDiamondArmor.init();
		ItemSamuraiArmor.init();
	}
	
	@SideOnly(Side.CLIENT)
	public static final void registerRenders() {
		ItemKingArmor.registerRenders();
		ItemBullArmor.registerRenders();
		ItemHeavyDiamondArmor.registerRenders();
		registerRendersTorchArrow();
		ItemTorchArrow.registerRenders();
		ItemSamuraiArmor.registerRenders();
	}

	@SideOnly(Side.CLIENT)
	private static void registerRendersTorchArrow() {
		EntityRegistry.registerModEntity(EntityTorchArrow.class, "TorchArrow", 100, ToroBaseMod.instance, 80, 10, true);
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityTorchArrow.class, new RenderTorchArrow(rm));
	}

}
