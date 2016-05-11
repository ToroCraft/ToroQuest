package net.torocraft.dailies.quests;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class DailyQuest {

	public String type;
	public TypedInteger target;
	public Reward reward;
	public int currentQuantity;
	public String id;
	public long date;

	public transient String targetName;

	public String getStatusMessage() {
		return getDisplayName() + " (" + Math.round(100 * currentQuantity / target.quantity) + "% complete)";
	}

	public DailyQuest clone() {
		DailyQuest quest = new DailyQuest();
		quest.readNBT(writeNBT());
		return quest;
	}

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
	
	private String targetItemName() {
		if (targetName == null) {
			if (isGatherQuest()) {
				targetName = decodeItem(target.type);
			} else if (isHuntQuest()) {
				targetName = decodeMob(target.type);
			}
		}
		return targetName;
	}
	
	private String decodeItem(int entityId) {
		return I18n.translateToLocal(Item.getItemById(entityId).getUnlocalizedName() + ".name");
	}

	private String decodeMob(int entityId) {
		String langKey = entityIdToLangKey(entityId);
		return I18n.translateToLocal(langKey);
	}

	private String entityIdToLangKey(int entityId) {
		Class<? extends Entity> entityClass = EntityList.idToClassMapping.get(entityId);
		String entityName = EntityList.classToStringMapping.get(entityClass);
		
		if (entityName == null || entityName.length() == 0) {
			entityName = "generic";
		}
		
		return "entity." + entityName + ".name";
	}

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

		int stackSize = item.getEntityItem().stackSize;

		int remainingTarget = target.quantity - currentQuantity;

		int leftOver = stackSize - remainingTarget;

		if (leftOver > 0) {
			dropNewStack(player, item, leftOver);
		} else {
			leftOver = 0;
		}

		currentQuantity += stackSize - leftOver;

		player.addChatMessage(new TextComponentString(TextFormatting.RED + "" + getStatusMessage()));

		return true;
	}

	public void dropNewStack(EntityPlayer player, EntityItem item, int amount) {
		ItemStack stack = item.getEntityItem().copy();
		stack.stackSize = amount;
		EntityItem dropItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, stack);
		dropItem.setNoPickupDelay();
		player.worldObj.spawnEntityInWorld(dropItem);
	}

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

	public String getName() {
		return "";
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

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
		c.setLong("date", date);
		c.setString("id", id);
		return c;
	}

	public void readNBT(NBTTagCompound c) {
		if (c == null) {
			return;
		}
		type = c.getString("type");
		currentQuantity = c.getInteger("currentQuantity");
		date = c.getLong("date");
		id = c.getString("id");

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

	public boolean isComplete() {
		return currentQuantity >= target.quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyQuest other = (DailyQuest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
