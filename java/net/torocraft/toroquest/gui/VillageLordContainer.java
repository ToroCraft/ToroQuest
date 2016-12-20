package net.torocraft.toroquest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.torocraft.toroquest.inventory.VillageLordInventory;

public class VillageLordContainer extends Container {

	private final int HOTBAR_SLOT_COUNT = 9;
	private final int INVENTORY_ROW_COUNT = 3;
	private final int INVENTORY_COLUMN_COUNT = 9;
	private final int SUBMIT_ITEM_ROW_COUNT = 1;
	private final int SUBMIT_ITEM_COLUMN_COUNT = 3;
	
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + (INVENTORY_COLUMN_COUNT * INVENTORY_ROW_COUNT);
	private final int LORD_INVENTORY_SLOT_COUNT = 3;
	
	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	private final int LORD_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	
	private final int SLOT_X_SPACING = 18;
    private final int SLOT_Y_SPACING = 18;
	
    private final int HOTBAR_XPOS = 8;
	private final int HOTBAR_YPOS = 106;
	
	private final int INVENTORY_XPOS = 8;
	private final int INVENTORY_YPOS = 48;;
	
	private final int SUBMIT_ITEM_XPOS = 30;
	private final int SUBMIT_ITEM_YPOS = 17;
	
	private final int OUTPUT_ITEM_XPOS = 107;
	private final int OUTPUT_ITEM_YPOS = 17;
	
	private final VillageLordInventory inventory;
	
	public VillageLordContainer(EntityPlayer player, VillageLordInventory inventory, World world) {
		this.inventory = inventory;
		this.inventory.openInventory(player);
		
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			addSlotToContainer(new Slot(player.inventory, x, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}
		
		for (int x = 0; x < INVENTORY_ROW_COUNT; x++) {
			for (int y = 0; y < INVENTORY_COLUMN_COUNT; y++) {
				int slotNumber = HOTBAR_SLOT_COUNT + x * INVENTORY_COLUMN_COUNT + y;
				int xPos = INVENTORY_XPOS + y * SLOT_X_SPACING;
				int yPos = INVENTORY_YPOS + x * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(player.inventory, slotNumber,  xPos, yPos));
			}
		}
		
		for (int x = 0; x < SUBMIT_ITEM_ROW_COUNT; x++) {
			for(int y = 0; y < SUBMIT_ITEM_COLUMN_COUNT; y++) {
				int slotNumber = x * SUBMIT_ITEM_COLUMN_COUNT + y;
				int xPos = SUBMIT_ITEM_XPOS + y * SLOT_X_SPACING;
				int yPos = SUBMIT_ITEM_YPOS + x * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(inventory, slotNumber, xPos, yPos));
			}
		}
		
		addSlotToContainer(new SlotOutput(inventory, 3, OUTPUT_ITEM_XPOS, OUTPUT_ITEM_YPOS));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot slot = (Slot)this.inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()) {
        	return null;
        }
        
        ItemStack sourceStack = slot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();
        
        if(indexIsForAVanillaSlot(index)) {
        	if(!mergeItemStack(sourceStack, LORD_INVENTORY_FIRST_SLOT_INDEX, LORD_INVENTORY_FIRST_SLOT_INDEX + LORD_INVENTORY_SLOT_COUNT, false)) {
        		return null;
        	}
        } else if(indexIsForALordInventorySlot(index) || indexIsForLordOutputSlot(index)) {
        	if(!mergeStackFromLordToPlayer(sourceStack)) {
        		return null;
        	}
        } else {
        	return null;
        }
        
        int stackSize = sourceStack.func_190916_E();
        
        if(stackSize == 0) {
        	slot.putStack(null);
        } else {
        	slot.onSlotChanged();
        }
        
        //slot.onPickupFromSlot(player, sourceStack);
        return copyOfSourceStack;
	}

	private boolean mergeStackFromLordToPlayer(ItemStack sourceStack) {
		return mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false);
	}

	private boolean indexIsForAVanillaSlot(int index) {
		return index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	}
	
	private boolean indexIsForALordInventorySlot(int index) {
		return index >= LORD_INVENTORY_FIRST_SLOT_INDEX && index < LORD_INVENTORY_FIRST_SLOT_INDEX + LORD_INVENTORY_SLOT_COUNT;
	}
	
	private boolean indexIsForLordOutputSlot(int index) {
		return index == LORD_INVENTORY_FIRST_SLOT_INDEX + LORD_INVENTORY_SLOT_COUNT;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}
	
	public class SlotOutput extends Slot {

		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
