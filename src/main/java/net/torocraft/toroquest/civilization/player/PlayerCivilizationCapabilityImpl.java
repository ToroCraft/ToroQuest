package net.torocraft.toroquest.civilization.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessagePlayerCivilizationSetInCiv;

public class PlayerCivilizationCapabilityImpl implements PlayerCivilizationCapability {

	@CapabilityInject(PlayerCivilizationCapability.class)
	public static Capability<PlayerCivilizationCapability> INSTANCE = null;

	private Map<CivilizationType, Integer> reputations = new HashMap<CivilizationType, Integer>();
	private Province inCiv;

	private final EntityPlayer player;

	public PlayerCivilizationCapabilityImpl(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void setPlayerReputation(CivilizationType civ, int amount) {
		if (civ == null) {
			return;
		}
		reputations.put(civ, amount);
	}

	@Override
	public void adjustPlayerReputation(CivilizationType civ, int amount) {
		if (civ == null) {
			return;
		}
		if (reputations.get(civ) == null) {
			reputations.put(civ, 0);
		}
		reputations.put(civ, reputations.get(civ) + amount);
	}

	@Override
	public int getPlayerReputation(CivilizationType civ) {
		return i(reputations.get(civ));
	}

	@Override
	public void setPlayerInCivilization(Province civ) {
		inCiv = civ;

		if (player == null) {
			return;
		}

		if (!player.getEntityWorld().isRemote) {
			System.out.println("Transmit setPlayerInCivilization [" + s(civ) + "]");
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessagePlayerCivilizationSetInCiv(civ), (EntityPlayerMP) player);
		}
	}

	private String s(Province civ) {
		if (civ == null) {
			return null;
		}
		return civ.toString();
	}

	@Override
	public Province getPlayerInCivilization() {
		return inCiv;
	}

	@Override
	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("reputations", buildNBTReputationList());
		if (inCiv != null) {
			c.setTag("inCiv", inCiv.writeNBT());
		} else {
			c.removeTag("inCiv");
		}
		return c;
	}

	private String s(CivilizationType civ) {
		try {
			return civ.toString();
		} catch (Exception e) {
			return "";
		}
	}

	protected NBTTagList buildNBTReputationList() {
		NBTTagList repList = new NBTTagList();
		for (Entry<CivilizationType, Integer> rep : reputations.entrySet()) {
			if (rep.getValue() == null || rep.getKey() == null) {
				continue;
			}
			repList.appendTag(buildNBTReputationListItem(rep));
		}
		return repList;
	}

	private NBTBase buildNBTReputationListItem(Entry<CivilizationType, Integer> rep) {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("civ", s(rep.getKey()));
		c.setInteger("amount", rep.getValue());
		return c;
	}

	@Override
	public void readNBT(NBTBase nbt) {
		if (nbt == null || !(nbt instanceof NBTTagCompound)) {
			reputations = new HashMap<CivilizationType, Integer>();
			inCiv = null;
			return;
		}

		NBTTagCompound b = (NBTTagCompound) nbt;
		reputations = readNBTReputationList(b.getTag("reputations"));

		NBTBase civTag = b.getTag("inCiv");
		if (civTag != null && civTag instanceof NBTTagCompound) {
			inCiv = new Province();
			inCiv.readNBT((NBTTagCompound) civTag);
		} else {
			inCiv = null;
		}

		System.out.println("*** Loaded Cap: " + toString());
	}

	private Map<CivilizationType, Integer> readNBTReputationList(NBTBase tag) {
		Map<CivilizationType, Integer> reputations = new HashMap<CivilizationType, Integer>();
		if (tag == null || !(tag instanceof NBTTagList)) {
			return reputations;
		}
		NBTTagList list = (NBTTagList) tag;
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound c = list.getCompoundTagAt(i);
			reputations.put(e(c.getString("civ")), c.getInteger("amount"));
		}
		return reputations;
	}

	@Override
	public String toString() {
		if (player == null) {
			return "Player Civilization Info NULL PLAYER";
		}
		return "Player Civilization Info: " + player.getName() + ": IN_CIV[" + inCiv + "]";
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	public static void register() {
		System.out.println("registering cap");

		CapabilityManager.INSTANCE.register(PlayerCivilizationCapability.class, new PlayerCivilizationStorage(), PlayerCivilizationCapabilityImpl.class);
		/*
		 * CapabilityManager.INSTANCE.register(PlayerCivilizationCapability.
		 * class, new PlayerCivilizationStorage(), new
		 * Callable<PlayerCivilizationCapability>() {
		 * 
		 * @Override public PlayerCivilizationCapability call() throws Exception
		 * { return null; } });
		 */
		System.out.println("value of instance: " + PlayerCivilizationCapabilityImpl.INSTANCE);
	}

	private int i(Integer integer) {
		if (integer == null) {
			return 0;
		}
		return integer;
	}

	public static PlayerCivilizationCapability get(EntityPlayer player) {
		return player.getCapability(PlayerCivilizationCapabilityImpl.INSTANCE, null);
	}

	public static class PlayerCivilizationStorage implements IStorage<PlayerCivilizationCapability> {

		@Override
		public NBTBase writeNBT(Capability<PlayerCivilizationCapability> capability, PlayerCivilizationCapability instance, EnumFacing side) {
			return instance.writeNBT();
		}

		@Override
		public void readNBT(Capability<PlayerCivilizationCapability> capability, PlayerCivilizationCapability instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT(nbt);
		}

	}

}
