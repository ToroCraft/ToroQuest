package net.torocraft.toroquest.entities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.civilization.player.IVillageLordInventory;
import net.torocraft.toroquest.civilization.quests.QuestBase;

public class VillageLordInventory extends InventoryBasic implements IVillageLordInventory {
	private final EntityVillageLord lord;
	private static int DONATE_BOX_INDEX = 8;

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
		dropItems(getGivenItems());
		items = QuestBase.removeEmptyItemStacks(items);
		items = dropOverItems(items, 4);
		for (int i = 0; i < 4; i++) {
			if (i >= items.size()) {
				setInventorySlotContents(i, ItemStack.EMPTY);
			} else {
				setInventorySlotContents(i, items.get(i));
			}
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
		dropItems(getReturnItems());
		items = QuestBase.removeEmptyItemStacks(items);
		items = dropOverItems(items, 4);
		for (int i = 0; i < 4; i++) {
			if (i >= items.size()) {
				setInventorySlotContents(i + 4, ItemStack.EMPTY);
			} else {
				setInventorySlotContents(i + 4, items.get(i));
			}
		}
	}

	@Override
	public ItemStack getDonationItem() {
		return getStackInSlot(DONATE_BOX_INDEX);
	}

	@Override
	public void setDonationItem(ItemStack item) {
		if (item.isEmpty()) {
			return;
		}
		setInventorySlotContents(DONATE_BOX_INDEX, item);
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

	public NBTTagList saveAllItems() {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack itemstack = (ItemStack) getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound);
				list.appendTag(nbttagcompound);
			}
		}
		return list;
	}

	public void loadAllItems(NBTTagList list) {
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			int slot = nbttagcompound.getByte("Slot") & 255;
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, new ItemStack(nbttagcompound));
			}
		}
	}
}