package net.torocraft.toroquest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;

public class ItemBattleAxe extends ItemAxe {

	public static ItemBattleAxe INSTANCE;

	public static final String NAME = "diamond_battle_axe";

	public static void init() {
		INSTANCE = new ItemBattleAxe();
		ResourceLocation resourceName = new ResourceLocation(ToroQuest.MODID, NAME);
		GameRegistry.register(INSTANCE, resourceName);
	}

	public static void registerRenders() {
		ModelResourceLocation model = new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(INSTANCE, 0, new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory"));
	}

	public ItemBattleAxe() {
		super(ToolMaterial.DIAMOND, 12f, -3f);
		setUnlocalizedName(NAME);
	}

}
