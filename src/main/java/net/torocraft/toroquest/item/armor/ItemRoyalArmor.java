package net.torocraft.toroquest.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.material.ArmorMaterials;
import net.torocraft.toroquest.util.ToroBaseUtils;

@Mod.EventBusSubscriber
public class ItemRoyalArmor extends ItemArmor {

	public static final String NAME = "royal";

	public static ItemRoyalArmor helmetItem;
	public static ItemRoyalArmor chestplateItem;
	public static ItemRoyalArmor leggingsItem;
	public static ItemRoyalArmor bootsItem;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		bootsItem = new ItemRoyalArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		leggingsItem = new ItemRoyalArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		helmetItem = new ItemRoyalArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		chestplateItem = new ItemRoyalArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);

		bootsItem.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME + "_boots"));
		event.getRegistry().register(bootsItem);

		leggingsItem.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME + "_leggings"));
		event.getRegistry().register(leggingsItem);

		helmetItem.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME + "_helmet"));
		event.getRegistry().register(helmetItem);

		chestplateItem.setRegistryName(new ResourceLocation(ToroQuest.MODID, NAME + "_chestplate"));
		event.getRegistry().register(chestplateItem);
	}

	public static void registerRenders() {
		registerRendersHelmet();
		registerRendersChestPlate();
		registerRendersLeggings();
		registerRendersBoots();
	}

	private static void registerRendersBoots() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bootsItem, 0, model("boots"));
	}

	private static void registerRendersLeggings() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(leggingsItem, 0, model("leggings"));
	}

	private static void registerRendersHelmet() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmetItem, 0, model("helmet"));
	}

	private static void registerRendersChestPlate() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chestplateItem, 0, model("chestplate"));
	}

	private static ModelResourceLocation model(String model) {
		return new ModelResourceLocation(ToroQuest.MODID + ":" + NAME + "_" + model, "inventory");
	}

	public ItemRoyalArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.ROYAL, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}

}
