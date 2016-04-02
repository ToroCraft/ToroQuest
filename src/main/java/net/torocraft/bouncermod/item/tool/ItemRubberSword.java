package net.torocraft.bouncermod.item.tool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.torocraft.bouncermod.material.ToolMaterials;
import net.torocraft.bouncermod.util.KnockbackEffect;

public class ItemRubberSword extends ItemSword {

	public final static String NAME = "rubberSword";
	
	public ItemRubberSword() {
		super(ToolMaterials.RUBBER);
		this.setUnlocalizedName(NAME);
		this.setKnockback(1.0);
		this.setCreativeTab(CreativeTabs.tabCombat);
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