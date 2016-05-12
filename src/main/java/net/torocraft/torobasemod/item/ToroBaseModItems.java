package net.torocraft.torobasemod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
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
import net.torocraft.torobasemod.item.projectile.ItemTorchArrow;

public class ToroBaseModItems {

	public static final void init() {
		initTools();
		initArmor();
	}

	private static void initTools() {
		ItemTorchArrow.init();
		registerRendersTorchArrow();
	}

	private static void initArmor() {
		ItemKingArmor.init();
		ItemBullArmor.init();
		ItemHeavyDiamondArmor.init();
	}
	
	@SideOnly(Side.CLIENT)
	public static final void registerRenders() {
		ItemKingArmor.registerRenders();
		ItemBullArmor.registerRenders();
		ItemHeavyDiamondArmor.registerRenders();
		ItemTorchArrow.registerRenders();
	}
	
	private static void registerRendersTorchArrow() {
		EntityRegistry.registerModEntity(EntityTorchArrow.class, "TorchArrow", 100, ToroBaseMod.instance, 64, 20, true);
	}

	public static void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTorchArrow.class, new IRenderFactory<EntityTorchArrow>() {

			@Override
			public Render<? super EntityTorchArrow> createRenderFor(
					RenderManager manager) {
				return new RenderTorchArrow(manager);
			}
		});
	}

}
