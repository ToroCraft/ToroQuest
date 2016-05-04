package net.torocraft.dailies.quests;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DailyQuest implements IDailyQuest {

	public String type;
	public TypedInteger target;
	public Reward reward;
	public int currentQuantity;

	public String targetName;

	@Override
	public String getStatusMessage() {

		if (targetName == null) {
			targetName = Item.getItemById(target.type).getUnlocalizedName();
		}


		return type + " " + targetName + " " + currentQuantity + " of  " + target.quantity;
	}

	@Override
	public boolean gather(EntityPlayer player, EntityItem item) {
		if (!"gather".equals(type)) {
			return false;
		}

		if (item == null || item.isDead) {
			return false;
		}

		int itemId = Item.getIdFromItem(item.getEntityItem().getItem());

		if (itemId != target.type) {
			return false;
		}

		currentQuantity++;
		player.addChatMessage(new TextComponentString(TextFormatting.RED + "" + getStatusMessage()));


		return true;
	}

	@Override
	public boolean hunt(EntityPlayer player, EntityLivingBase mob) {
		if (!"hunt".equals(type) || mob == null) {
			return false;
		}

		int id = EntityList.getEntityID(mob);// mob.getEntityId();

		System.out.println("Entity [" + mob.getName() + "] [" + id + "]");


		if (id != target.type) {
			return false;
		}

		currentQuantity++;
		player.addChatMessage(new TextComponentString(TextFormatting.RED + "" + getStatusMessage()));

		return false;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void reward(EntityPlayer player) {
		if (reward != null) {
			reward.reward(player);
		}
	}
	
	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("type", type);
		c.setInteger("currentQuantity", currentQuantity);
		c.setTag("target", target.writeNBT());
		c.setTag("reward", reward.writeNBT());
		return c;
	}

	public void readNBT(NBTTagCompound c) {
		if (c == null) {
			return;
		}
		type = c.getString("type");
		currentQuantity = c.getInteger("currentQuantity");

		target = new TypedInteger();
		reward = new Reward();

		target.readNBT(cast(c.getTag("target")));
		reward.readNBT(cast(c.getTag("reward")));
	}

	private NBTTagCompound cast(NBTBase c) {
		if (c == null) {
			return null;
		}
		return (NBTTagCompound) c;
	}

	@Override
	public boolean isComplete() {
		return currentQuantity >= target.quantity;
	}

}
