package net.torocraft.toroquest.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_SPEED;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.ToroQuest;

public class ItemClaymore extends ItemSword {

	public static ItemClaymore INSTANCE;
	public static final String NAME = "claymore";

	private final float attackDamage;

	public static void init() {
		INSTANCE = new ItemClaymore();
		GameRegistry.register(INSTANCE, new ResourceLocation(ToroQuest.MODID, NAME));
	}

	public static void registerRenders() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0,
				new ModelResourceLocation(ToroQuest.MODID + ":" + NAME, "inventory"));
	}

	public ItemClaymore() {
		super(Item.ToolMaterial.IRON);
		setUnlocalizedName(NAME);
		this.attackDamage = 5.0F + Item.ToolMaterial.IRON.getDamageVsEntity();
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> m = super.getItemAttributeModifiers(equipmentSlot);

		//if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
		//	multimap.put(ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
		//	multimap.put(ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -8D, 0));
		
		
		m.removeAll(ATTACK_SPEED.getName());
		m.put(ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -9.5D, 0));
		
		
		for(AttributeModifier a : m.get(ATTACK_SPEED.getName())){
			System.out.println("AttributeModifier: " + a.toString());
		}
		
		
		
		return m;
	}
}
