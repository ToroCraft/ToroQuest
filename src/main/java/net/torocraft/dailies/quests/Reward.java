package net.torocraft.dailies.quests;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Reward extends TypedInteger {

	public void reward(EntityPlayer player) {
		ItemStack stack = new ItemStack(Item.getItemById(type));
		for (int i = 0; i < quantity; i++) {
			EntityItem dropItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, stack.copy());
			dropItem.setNoPickupDelay();
			player.worldObj.spawnEntityInWorld(dropItem);
		}
	}

}