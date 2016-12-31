package net.torocraft.toroquest.entities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.civilization.player.IVillageLordInventory;
import net.torocraft.toroquest.civilization.quests.QuestBase;

public class VillageLordInventory extends InventoryBasic implements IVillageLordInventory {
	private final EntityVillageLord lord;

	public VillageLordInventory(EntityVillageLord lord, String inventoryTitle, int slotCount) {
		super(inventoryTitle, false, slotCount);
		this.lord = lord;
	}

	@SideOnly(Side.CLIENT)
	public VillageLordInventory(EntityVillageLord lord, ITextComponent inventoryTitle, int slotCount) {
		super(inventoryTitle, slotCount);
		this.lord = lord;
	}

	@Override
	public List<ItemStack> getGivenItems() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < 4; i++) {
			items.add(removeStackFromSlot(i));
		}
		return items;
	}

	@Override
	public void setGivenItems(List<ItemStack> items) {
		items = QuestBase.removeEmptyItemStacks(items);
		items = dropOverItems(items, 4);
		for (int i = 0; i < 4; i++) {
			setInventorySlotContents(i, items.get(i));
		}
	}

	@Override
	public List<ItemStack> getReturnItems() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < 4; i++) {
			items.add(removeStackFromSlot(i + 4));
		}
		return items;
	}

	@Override
	public void setReturnItems(List<ItemStack> items) {
		items = QuestBase.removeEmptyItemStacks(items);
		items = dropOverItems(items, 4);
		for (int i = 0; i < 4; i++) {
			setInventorySlotContents(i + 4, items.get(i));
		}
	}

	@Override
	public ItemStack getDonationItem() {
		return removeStackFromSlot(8);
	}

	@Override
	public void setDonationItem(ItemStack item) {
		if (item.isEmpty()) {
			return;
		}
		setInventorySlotContents(8, item);
	}

	private List<ItemStack> dropOverItems(List<ItemStack> items, int maxIndex) {
		if (items.size() <= maxIndex) {
			return items;
		}

		List<ItemStack> over = new ArrayList<ItemStack>();
		for (int i = maxIndex; i < items.size(); i++) {
			over.add(items.get(i));
		}

		for (ItemStack item : over) {
			items.remove(item);
		}

		dropItems(over);

		return items;
	}

	private void dropItems(List<ItemStack> items) {
		for (ItemStack stack : items) {
			EntityItem dropItem = new EntityItem(lord.world, lord.posX, lord.posY, lord.posZ, stack);
			dropItem.setNoPickupDelay();
			lord.world.spawnEntity(dropItem);
		}
	}
}