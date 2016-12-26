package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.material.ToolMaterials;

public class ItemFireSword extends ItemSword {

	public static ItemFireSword INSTANCE;

	public static final String NAME = "fire_sword";

	public static void init() {
		INSTANCE = new ItemFireSword();
		ResourceLocation resourceName = new ResourceLocation(ToroQuest.MODID, NAME);
		GameRegistry.register(INSTANCE, resourceName);
	}

	public static void registerRenders() {
		ModelResourceLocation model = new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(INSTANCE, 0, new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory"));
	}

	public ItemFireSword() {
		super(ToolMaterials.FIRE_MATERIAL);
		setUnlocalizedName(NAME);
	}

}
