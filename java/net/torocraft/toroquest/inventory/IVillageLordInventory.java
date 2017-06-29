package net.torocraft.toroquest.inventory;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.torocraft.toroquest.civilization.Province;

public interface IVillageLordInventory extends IInventory {
	
	List<ItemStack> getGivenItems();

	void setGivenItems(List<ItemStack> items);

	List<ItemStack> getReturnItems();

	void setReturnItems(List<ItemStack> items);

	ItemStack getDonationItem();

	void setDonationItem(ItemStack item);

	Province getProvince();

	int getEntityId();

}
