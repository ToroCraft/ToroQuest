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
import net.minecraft.util.text.translation.I18n;

public class DailyQuest implements IDailyQuest {

	public String type;
	public TypedInteger target;
	public Reward reward;
	public int currentQuantity;

	public String targetName;

	@Override
	public String getStatusMessage() {
		return getDisplayName() + " (" + Math.round(100 * currentQuantity / target.quantity) + "% complete)";
	}

	private String targetItemName() {
		if (targetName == null) {

			if (isGatherQuest()) {
				targetName = I18n.translateToLocal(Item.getItemById(target.type).getUnlocalizedName());
			} else if (isHuntQuest()) {

				// TODO: find a way to decode mob name even if we just maintain
				// our own mapping
				targetName = "Mob[" + target.type + "]";

				// targetName = EntityList.
				// //Item.getItemById(target.type).getUnlocalizedName();
			}

		}
		return targetName;
	}

	@Override
	public String getDisplayName() {
		if (isGatherQuest()) {
			return "⇓ Gather Quest: collect " + target.quantity + " pieces of " + targetItemName();
		} else if (isHuntQuest()) {
			return "⚔ Hunt Quest: kill " + target.quantity + " " + targetItemName() + " mobs";
		}
		return "...";
	}

	private boolean isHuntQuest() {
		return "hunt".equals(type);
	}

	private boolean isGatherQuest() {
		return "gather".equals(type);
	}

	@Override
	public boolean gather(EntityPlayer player, EntityItem item) {
		if (!isGatherQuest()) {
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
		if (!isHuntQuest() || mob == null) {
			return false;
		}

		int id = EntityList.getEntityID(mob);// mob.getEntityId();


		if (id != target.type) {
			return false;
		}

		currentQuantity++;
		player.addChatMessage(new TextComponentString(TextFormatting.RED + "" + getStatusMessage()));

		return true;
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
