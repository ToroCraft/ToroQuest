package net.torocraft.bouncermod.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemRubberSword extends ItemSword {

	public ItemRubberSword(String unlocalizedName, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setKnockback(1.0);
	}

	public Double knockback;

	public void setKnockback(Double knockback) {
		this.knockback = knockback;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		target.motionX = Math.signum(target.posX - attacker.posX) * knockback;
		target.motionZ = Math.signum(target.posZ - attacker.posZ) * knockback;

		target.motionY = Math.signum(target.posY - attacker.posY) * knockback;
		if (target.motionY == 0) {
			target.motionY = knockback;
		}
		return true;
	}

}
