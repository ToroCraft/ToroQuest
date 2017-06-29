package net.torocraft.toroquest.item.armor;

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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.material.ArmorMaterials;
import net.torocraft.toroquest.util.ToroBaseUtils;

@Mod.EventBusSubscriber
public class ItemReinforcedDiamondArmor extends ItemArmor {

	public static final String NAME = "reinforced_diamond";

	public static ItemReinforcedDiamondArmor helmetItem;
	public static ItemReinforcedDiamondArmor chestplateItem;
	public static ItemReinforcedDiamondArmor leggingsItem;
	public static ItemReinforcedDiamondArmor bootsItem;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Item> event) {
		bootsItem = new ItemReinforcedDiamondArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		leggingsItem = new ItemReinforcedDiamondArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		helmetItem = new ItemReinforcedDiamondArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		chestplateItem = new ItemReinforcedDiamondArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);

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

	public ItemReinforcedDiamondArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.REINFORCED_DIAMOND, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

		if (player.isInWater()) {

		}

		// Minecraft.getMinecraft().thePlayer.movementInput =

		effectPlayer(player, MobEffects.SLOWNESS, 2);

		// effectPlayer(player, MobEffects.jump, -2);

		if (player.isSwingInProgress) {
			player.addExhaustion(0.025f);
		}
	}

	private void effectPlayer(EntityPlayer player, Potion potion, int amplifier) {
		// Always effect for 8 seconds, then refresh
		if (player.getActivePotionEffect(potion) == null || player.getActivePotionEffect(potion).getDuration() <= 1) {
			player.addPotionEffect(new PotionEffect(potion, 159, amplifier, true, false));
		}
	}

	public static class EventHandlers {
		@SubscribeEvent
		public void postInit(LivingHurtEvent e) {

			DamageSource source = e.getSource();

			Iterable<ItemStack> armorStacks = e.getEntityLiving().getArmorInventoryList();

			// boolean hasHeavyArmor = false;
			float reduction = 0;

			for (ItemStack armorStack : armorStacks) {
				if (isHeavyArmor(armorStack)) {
					if (source.isProjectile() || source.isExplosion()) {
						reduction += 0.2;
					}
				}
			}

			if (reduction > 0) {
				float newDamage = (1 - reduction) * e.getAmount();
				System.out.println("Heavy armor reduction: [" + reduction + "] IN[" + e.getAmount() + "] OUT[" + newDamage + "]");
				e.setAmount(newDamage);
			}
		}

		private boolean isHeavyArmor(ItemStack armorStack) {
			if (armorStack == null || armorStack.getItem() == null) {
				return false;
			}

			Item item = armorStack.getItem();
			return item == helmetItem || item == chestplateItem || item == leggingsItem || item == bootsItem;
		}
	}

}
