package net.torocraft.torobasemod.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.material.ArmorMaterials;

public class ItemKingArmor extends ItemArmor {
	
	public static final String NAME = "king";

	public static ItemKingArmor helmetItem;
	public static ItemKingArmor chestplateItem;
	public static ItemKingArmor leggingsItem;
	public static ItemKingArmor bootsItem;

	public static void init() {
		initHelmet();
		initChestPlate();
		initLeggings();
		initBoots();
	}

	private static void initBoots() {
		bootsItem = new ItemKingArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(bootsItem, NAME + "_boots");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bootsItem, 0, model("boots"));
	}

	private static void initLeggings() {
		leggingsItem = new ItemKingArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(leggingsItem, NAME + "_leggings");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(leggingsItem, 0, model("leggings"));
	}

	private static void initHelmet() {
		helmetItem = new ItemKingArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(helmetItem, NAME + "_helmet");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmetItem, 0, model("helmet"));
	}

	private static void initChestPlate() {
		chestplateItem = new ItemKingArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(chestplateItem, NAME + "_chestplate");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chestplateItem, 0, model("chestplate"));
	}

	private static ModelResourceLocation model(String model) {
		return new ModelResourceLocation(ToroBaseMod.MODID + ":" + NAME + "_" + model, "inventory");
	}
	
	public ItemKingArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.KING, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}
	
}
