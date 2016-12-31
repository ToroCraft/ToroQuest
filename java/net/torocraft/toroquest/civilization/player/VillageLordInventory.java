package net.torocraft.toroquest.civilization.player;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class VillageLordInventory implements IVillageLordInventory {

	private final List<ItemStack> itemStacks;
	private final EntityPlayer player;
	
	public VillageLordInventory(EntityPlayer player, List<ItemStack> items) {
		this.player = player;
		this.itemStacks = items;
		// TODO auto drop items over index 9
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGivenItems(List<ItemStack> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ItemStack> getReturnItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReturnItems(List<ItemStack> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getDonationItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDonationItem(ItemStack item) {
		// TODO Auto-generated method stub

	}
}
