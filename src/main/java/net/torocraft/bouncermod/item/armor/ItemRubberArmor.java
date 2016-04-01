package net.torocraft.bouncermod.item.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.torocraft.bouncermod.BouncerMod;
import net.torocraft.bouncermod.item.BounceModItems;
import net.minecraftforge.common.util.EnumHelper;

public class ItemRubberArmor extends ItemArmor {

	private static String MODID = BouncerMod.MODID;
	
	public static ArmorMaterial RUBBER = EnumHelper.addArmorMaterial("RUBBER", MODID + ":rubberArmor", 15, new int[]{2, 5, 4, 1}, 12, null);

	public ItemRubberArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(RUBBER, renderIndexIn, equipmentSlotIn);
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
