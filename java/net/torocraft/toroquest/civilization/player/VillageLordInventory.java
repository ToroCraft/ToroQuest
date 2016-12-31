package net.torocraft.toroquest.civilization.player;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.toroquest.civilization.quests.QuestBase;

public class VillageLordInventory implements IVillageLordInventory {

	private final List<ItemStack> itemStacks;
	private final EntityPlayer player;
	
	public VillageLordInventory(EntityPlayer player, List<ItemStack> items) {
		this.player = player;
		this.itemStacks = items;
		sizeList(items, 9);

		// TODO auto drop items over index 9
	}
	
	private void sizeList(List<?> list, int size) {
		while (list.size() < size) {
			list.add(null);
		}
	}

	@Override
	public String getName() {
		return "Village Lord Inventory";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack slotStack = getStackInSlot(index);
		int stackSize = slotStack.getCount();
		
		ItemStack stackRemoved;
		if(stackSize <= count) {
			stackRemoved = slotStack;
			setInventorySlotContents(index, ItemStack.EMPTY);
			
		} else {
			stackRemoved = slotStack.splitStack(count);
			if(stackSize == 0) {
				setInventorySlotContents(index, ItemStack.EMPTY);
			}
		}
		
		markDirty();
		
		return stackRemoved;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.EMPTY);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory()) {
            return;
		}

		int stackSize = stack.getCount();
		
        if (stackSize > this.getInventoryStackLimit()) {
			// TODO drop the rest?
			stack.setCount(this.getInventoryStackLimit());
        }

        if (stackSize == 0) {
			stack = ItemStack.EMPTY;
        }

		itemStacks.set(index, stack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		// TODO update?
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO save?
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		itemStacks.clear();
	}
	
	@Override
	public void markDirty() {
		// TODO save?
	}

	@Override
	public boolean isEmpty() {
		return false;
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
			EntityItem dropItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
			dropItem.setNoPickupDelay();
			player.world.spawnEntity(dropItem);
		}
	}
}
