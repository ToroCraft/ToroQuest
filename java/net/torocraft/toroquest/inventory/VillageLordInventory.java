package net.torocraft.toroquest.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageSetItemReputationAmount;

public class VillageLordInventory implements IInventory {

	private static final int SUBMIT_ITEM_COUNT = 1;
	
	private Map<Item,Integer> itemReputations = new HashMap<Item,Integer>();
	
	private ItemStack[] itemStacks = new ItemStack[SUBMIT_ITEM_COUNT];
	
	private EntityPlayer player;
	
	public VillageLordInventory() {
		this.clear();
		loadItemList();
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
		
		ItemStack stackRemoved;
		if(stackSize <= count) {
			stackRemoved = slotStack;
			setInventorySlotContents(index, ItemStack.field_190927_a);
			
		} else {
			stackRemoved = slotStack.splitStack(count);
			if(stackSize == 0) {
				setInventorySlotContents(index, ItemStack.field_190927_a);
			}
		}
		
		markDirty();
		
		return stackRemoved;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.field_190927_a);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory()) {
            return;
		}

		int stackSize = stack.func_190916_E();
		
        if (stackSize > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }

        if (stackSize == 0) {
            stack = ItemStack.field_190927_a;
        }

        this.itemStacks[index] = stack;
        this.markDirty();
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
		Arrays.fill(itemStacks, ItemStack.field_190927_a);
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
	
	public void updateClientQuest(){
		// FIXME: send a quest update packet to the client (I started to created
		// MessageSetQuestInfo for this)

	}

	public void checkForReputation() {
		Integer reputation = itemReputations.get(itemStacks[0].getItem());
		if(reputation != null) {
			updateClientReputation(reputation * itemStacks[0].func_190916_E());
		} else {
			updateClientReputation(0);
		}
	}
	
	private void updateClientReputation(Integer rep) {
		ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetItemReputationAmount(rep), (EntityPlayerMP)this.player);
	}
	
	private void loadItemList() {
		itemReputations.put(Item.getItemById(3), 100);
	}

	@Override
	public boolean func_191420_l() {
		// TODO Auto-generated method stub
		return false;
	}
}
