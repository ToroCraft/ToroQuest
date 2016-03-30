package net.torocraft.bouncermod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.torocraft.bouncermod.util.KnockbackEffect;

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
		KnockbackEffect.getEntityKnockbackSpeed(target, attacker, knockback);
		return true;
	}
	
	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		KnockbackEffect.getEntityKnockbackSpeed(player, entity, knockback*.25);
        return false;
    }

}