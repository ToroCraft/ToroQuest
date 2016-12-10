package net.torocraft.toroquest.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.item.ItemToroLeather;
import net.torocraft.toroquest.material.ArmorMaterials;
import net.torocraft.toroquest.util.ToroBaseUtils;

public class ItemToroArmor extends ItemArmor {

	public static final String NAME = "toro_armor";

	public static ItemToroArmor helmetItem;
	public static ItemToroArmor chestplateItem;
	public static ItemToroArmor leggingsItem;
	public static ItemToroArmor bootsItem;

	public static void init() {
		initHelmet();
		initChestPlate();
		initLeggings();
		initBoots();
	}

	public static void registerRenders() {
		registerRendersHelmet();
		registerRendersChestPlate();
		registerRendersLeggings();
		registerRendersBoots();
	}

	public static void initRecipes() {
		ItemStack emerald = new ItemStack(Items.EMERALD);
		ItemStack leather = new ItemStack(ItemToroLeather.INSTANCE);
		GameRegistry.addRecipe(new ItemStack(helmetItem), "ggg", "ddd", "d d", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(chestplateItem), "dgd", "ddd", "ddd", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(leggingsItem), "ddd", "dgd", "dgd", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(bootsItem), "dgd", "dgd", 'g', emerald, 'd', leather);
	}

	private static void initBoots() {
		bootsItem = new ItemToroArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		ToroBaseUtils.registerItem(bootsItem, NAME + "_boots");
	}

	private static void registerRendersBoots() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bootsItem, 0, model("boots"));
	}

	private static void initLeggings() {
		leggingsItem = new ItemToroArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		ToroBaseUtils.registerItem(leggingsItem, NAME + "_leggings");
	}

	private static void registerRendersLeggings() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(leggingsItem, 0, model("leggings"));
	}

	private static void initHelmet() {
		helmetItem = new ItemToroArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		ToroBaseUtils.registerItem(helmetItem, NAME + "_helmet");
	}

	private static void registerRendersHelmet() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmetItem, 0, model("helmet"));
	}

	private static void initChestPlate() {
		chestplateItem = new ItemToroArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);
		ToroBaseUtils.registerItem(chestplateItem, NAME + "_chestplate");
	}

	private static void registerRendersChestPlate() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chestplateItem, 0, model("chestplate"));
	}

	private static ModelResourceLocation model(String model) {
		return new ModelResourceLocation(ToroQuest.MODID + ":" + NAME + "_" + model, "inventory");
	}

	public ItemToroArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.TORO, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}

}
