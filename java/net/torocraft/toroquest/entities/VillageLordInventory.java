package net.torocraft.toroquest.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.IVillageLordInventory;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.QuestBase;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageSetItemReputationAmount;
import net.torocraft.toroquest.network.message.MessageSetQuestInfo;

public class VillageLordInventory extends InventoryBasic implements IVillageLordInventory {
	private final EntityVillageLord lord;
	private static int DONATE_BOX_INDEX = 8;
	
	private Map<Item,Integer> itemReputations = new HashMap<Item,Integer>();
	
	private EntityPlayer player;
	
	public VillageLordInventory(EntityVillageLord lord, String inventoryTitle, int slotCount) {
		super(inventoryTitle, false, slotCount);
		this.lord = lord;
		loadItemList();
	}

	@SideOnly(Side.CLIENT)
	public VillageLordInventory(EntityVillageLord lord, ITextComponent inventoryTitle, int slotCount) {
		super(inventoryTitle, slotCount);
		this.lord = lord;
		loadItemList();
	}

	@Override 
	public void openInventory(EntityPlayer player) {
		super.openInventory(player);
		this.player = player;
		
		if(player != null && !player.world.isRemote)
			updateClientQuest();
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
		return removeStackFromSlot(DONATE_BOX_INDEX);
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

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		if(player != null && !player.world.isRemote)
			checkForReputation();
	}
	
	private void checkForReputation() {
		Integer reputation = itemReputations.get(getStackInSlot(DONATE_BOX_INDEX).getItem());
		if(reputation != null) {
			updateClientReputation(reputation * getStackInSlot(DONATE_BOX_INDEX).getCount());
		} else {
			updateClientReputation(0);
		}
	}
	
	private void updateClientReputation(Integer rep) {
		ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetItemReputationAmount(rep), (EntityPlayerMP)player);
	}
	
	private void updateClientQuest(){
		Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
		QuestData currentQuest = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
		QuestData nextQuest = PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province);
		ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, currentQuest, nextQuest), (EntityPlayerMP) player);
	}
	
	private void loadItemList() {
		itemReputations.put(Item.getItemById(3), 100);
	}
}