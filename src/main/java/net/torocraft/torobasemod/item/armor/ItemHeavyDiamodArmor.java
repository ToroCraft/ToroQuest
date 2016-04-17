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
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.material.ArmorMaterials;

public class ItemHeavyDiamodArmor extends ItemArmor {

	public static final String NAME = "heavyDiamond";

	public static ItemHeavyDiamodArmor helmetItem;
	public static ItemHeavyDiamodArmor chestplateItem;
	public static ItemHeavyDiamodArmor leggingsItem;
	public static ItemHeavyDiamodArmor bootsItem;

	public static void init() {

		initHelmet();
		initChestPlate();
		initLeggings();
		initBoots();

		MinecraftForge.EVENT_BUS.register(new EventHandlers());
	}

	private static void initBoots() {
		bootsItem = new ItemHeavyDiamodArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		GameRegistry.registerItem(bootsItem, NAME + "_boots");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bootsItem, 0, model("boots"));
	}

	private static void initLeggings() {
		leggingsItem = new ItemHeavyDiamodArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		GameRegistry.registerItem(leggingsItem, NAME + "_leggings");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(leggingsItem, 0, model("leggings"));
	}

	private static void initHelmet() {
		helmetItem = new ItemHeavyDiamodArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		GameRegistry.registerItem(helmetItem, NAME + "_helmet");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmetItem, 0, model("helmet"));
	}

	private static void initChestPlate() {
		chestplateItem = new ItemHeavyDiamodArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);
		GameRegistry.registerItem(chestplateItem, NAME + "_chestplate");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chestplateItem, 0, model("chestplate"));
	}

	private static ModelResourceLocation model(String model) {
		return new ModelResourceLocation(ToroBaseMod.MODID + ":" + NAME + "_" + model, "inventory");
	}

	public ItemHeavyDiamodArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.HEAVY_DIAMOND, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(8588);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

		if (player.isInWater()) {

		}

		// Minecraft.getMinecraft().thePlayer.movementInput =

		effectPlayer(player, MobEffects.moveSlowdown, 2);

		//effectPlayer(player, MobEffects.jump, -2);

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
		   
		   boolean hasHeavyArmor = false;
		   float reduction = 0;
		   
		   for(ItemStack armorStack : armorStacks){
			   if(isHeavyArmor(armorStack)){
				   if(source.isProjectile() || source.isExplosion()){
					   reduction += 0.2;
				   }
			   }
		   }
		   
		   if(reduction > 0){
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
