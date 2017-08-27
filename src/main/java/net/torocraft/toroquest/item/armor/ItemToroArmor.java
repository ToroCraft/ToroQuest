package net.torocraft.toroquest.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.item.ItemToroLeather;
import net.torocraft.toroquest.material.ArmorMaterials;
import net.torocraft.toroquest.util.ToroBaseUtils;

@Mod.EventBusSubscriber
public class ItemToroArmor extends ItemArmor {

	public static final String NAME = "toro_armor";

	public static ItemToroArmor helmetItem;
	public static ItemToroArmor chestplateItem;
	public static ItemToroArmor leggingsItem;
	public static ItemToroArmor bootsItem;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		bootsItem = new ItemToroArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		leggingsItem = new ItemToroArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		helmetItem = new ItemToroArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		chestplateItem = new ItemToroArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);

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

	public ItemToroArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.TORO, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}

}
