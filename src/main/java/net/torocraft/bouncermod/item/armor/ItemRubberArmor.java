package net.torocraft.bouncermod.item.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.torocraft.bouncermod.item.BounceModItems;
import net.torocraft.bouncermod.material.ArmorMaterials;

public class ItemRubberArmor extends ItemArmor {
	
	public ItemRubberArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.RUBBER, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
	    if (itemStack.getItem() == BounceModItems.rubberBootsItem) {
	        effectPlayer(player, MobEffects.jump,4);
	    }
	} 

	private void effectPlayer(EntityPlayer player, Potion potion, int amplifier) {
	    //Always effect for 8 seconds, then refresh
	    if (player.getActivePotionEffect(potion) == null || player.getActivePotionEffect(potion).getDuration() <= 1)
	        player.addPotionEffect(new PotionEffect(potion, 159, amplifier, true, true));
	}
}
