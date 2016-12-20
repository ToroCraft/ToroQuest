package net.torocraft.toroquest.inventory;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class VillageLordInventory implements IInventory {

	private static final int SUBMIT_ITEM_COUNT = 3;
	private static final int OUTPUT_ITEM_COUNT = 1;
	private static final int REWARD_OUTPUT_INDEX = 3;
	private static final int TOTAL_SLOT_COUNT = SUBMIT_ITEM_COUNT + OUTPUT_ITEM_COUNT;
	
	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOT_COUNT];
	
	private ItemStack lastModifiedStack = null;
	private int lastModifiedIndex = 0;
	
	private EntityPlayer player = null;
	
	@Override
	public String getName() {
		return "Bailey's Inventory";
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
		return itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemStacks[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack slotStack = getStackInSlot(index);
		int stackSize = slotStack.func_190916_E();
		
		if(slotStack == null) {
			return null;
		}
		
		ItemStack stackRemoved;
		if(stackSize <= count) {
			stackRemoved = slotStack;
			setInventorySlotContents(index, null);
			
		} else {
			stackRemoved = slotStack.splitStack(count);
			if(stackSize == 0) {
				setInventorySlotContents(index, null);
			}
		}
		
		markDirty();
		
		return stackRemoved;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		if(itemStack != null) {
			setInventorySlotContents(index, null);
		}
		
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		itemStacks[index] = stack;
		int stackSize = stack.func_190916_E();
		if(stack != null && stackSize > getInventoryStackLimit()) {
			stackSize = getInventoryStackLimit();
		}
		
		if(stack != null && index != REWARD_OUTPUT_INDEX) {
			this.lastModifiedIndex = index;
		}
		
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
		for(int x = 0; x < getSizeInventory(); x++) {
			if(itemStacks[x] != null) {
				player.dropItem(itemStacks[x], false);
			}
		}
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
		Arrays.fill(itemStacks, null);
	}
	
	@Override
	public void markDirty() {
		
	}
	
	public static void logItemStack(ItemStack stack) {
		if (stack == null) {
			return;
		}
		System.out.println("LOGGING ITEM STACK");
		System.out.println("type:" + Item.getIdFromItem(stack.getItem()));
		System.out.println("subType:" + stack.getMetadata());
		System.out.println("NBT: " + String.valueOf(stack.getTagCompound()));
	}
	
	private void syncProgress(final String questId, final int progress) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				
			}

		}).start();
	}
	
	private void updateClient(final EntityPlayer player) {

	}

	@Override
	public boolean func_191420_l() {
		// TODO Auto-generated method stub
		return false;
	}
}
