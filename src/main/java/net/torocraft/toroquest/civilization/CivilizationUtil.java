package net.torocraft.toroquest.civilization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CivilizationUtil {

	public static Province getPlayerCurrentProvince(EntityPlayer player) {
		try {
			Province provice = new Province();
			provice.readNBT((NBTTagCompound) getToroQuestPlayerDataTag(player).getTag("inciv"));
			return provice;
		} catch (Exception e) {
			return null;
		}
	}

	public static void setPlayerChunk(EntityPlayer player, int chunkX, int chunkZ) {

		Province prev = CivilizationUtil.getPlayerCurrentProvince(player);
		Province curr = CivilizationUtil.getProvinceAt(player.worldObj, player.chunkCoordX, player.chunkCoordZ);

		if (equals(prev, curr)) {
			return;
		}

		try {
			System.out.println("prev [" + prev.civilization + "][" + prev.chunkX + "][" + prev.chunkZ + "]");
			System.out.println("curr [" + curr.civilization + "][" + curr.chunkX + "][" + curr.chunkZ + "]");
		} catch (Exception e) {

		}

		if (prev != null) {
			System.out.println("ProvinceEvent.Leave [" + prev.civilization + "]");
			MinecraftForge.EVENT_BUS.post(new ProvinceEvent.Leave(player, prev));
		}

		if (curr != null) {
			System.out.println("ProvinceEvent.Enter [" + curr.civilization + "]");
			getToroQuestPlayerDataTag(player).setTag("inciv", curr.writeNBT());
			MinecraftForge.EVENT_BUS.post(new ProvinceEvent.Enter(player, curr));
		} else {
			getToroQuestPlayerDataTag(player).removeTag("inciv");
		}
	}

	private static boolean equals(Province a, Province b) {

		CivilizationType civA = getCivilization(a);
		CivilizationType civB = getCivilization(b);

		if (civA == null && civB == null) {
			return true;
		}

		if (civA == null || civB == null) {
			return false;
		}

		return civA.equals(civB);
	}

	private static CivilizationType getCivilization(Province a) {
		if (a == null) {
			return null;
		}
		return a.civilization;
	}

	public static int getPlayerReputation(EntityPlayer player, CivilizationType civ) {
		try {
			return getToroQuestPlayerDataTag(player).getInteger("rep_" + civ);
		} catch (Exception e) {
			return 0;
		}
	}

	public static void setPlayerReputation(EntityPlayer player, CivilizationType civ, int amount) {
		try {
			getToroQuestPlayerDataTag(player).setInteger("rep_" + civ, amount);
		} catch (Exception e) {

		}
	}

	public static void adjustPlayerReputation(EntityPlayer player, Province civ, int amount) {
		if (amount == 0 || civ == null || civ.civilization == null || player == null) {
			return;
		}
		int newAmount = getPlayerReputation(player, civ.civilization) + amount;
		setPlayerReputation(player, civ.civilization, newAmount);
		MinecraftForge.EVENT_BUS.post(new ProvinceEvent.ReputationChange(player, civ, newAmount));
	}

	private static NBTTagCompound getToroQuestPlayerDataTag(EntityPlayer player) {
		NBTTagCompound tag = (NBTTagCompound) player.getEntityData().getTag("toroquest");
		if (tag == null) {
			tag = new NBTTagCompound();
			player.getEntityData().setTag("toroquest", tag);
		}
		return tag;
	}

	public static Province getProvinceAt(World world, int chunkX, int chunkZ) {
		return CivilizationsWorldSaveData.get(world).atLocation(chunkX, chunkZ);
	}

	public static Province registerNewCivilization(World world, int chunkX, int chunkZ) {
		return CivilizationsWorldSaveData.get(world).register(chunkX, chunkZ);
	}

}
