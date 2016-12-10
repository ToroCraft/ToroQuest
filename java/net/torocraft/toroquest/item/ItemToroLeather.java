package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;

public class ItemToroLeather extends Item {

	public static ItemToroLeather INSTANCE;

	public static final String NAME = "toro_leather";

	public static void init() {
		INSTANCE = new ItemToroLeather();
		ResourceLocation resourceName = new ResourceLocation(ToroQuest.MODID, NAME);
		GameRegistry.register(INSTANCE, resourceName);
	}

	public static void registerRenders() {
		ModelResourceLocation model = new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(INSTANCE, 0, new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory"));
	}

	public ItemToroLeather() {
		setUnlocalizedName(NAME);
		this.maxStackSize = 64;
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

}
