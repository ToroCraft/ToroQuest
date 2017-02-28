package net.torocraft.toroquest.civilization.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.torocraft.toroquest.civilization.CivilizationEvent;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.civilization.quests.QuestCourier;
import net.torocraft.toroquest.civilization.quests.QuestEnemyEncampment;
import net.torocraft.toroquest.civilization.quests.QuestFarm;
import net.torocraft.toroquest.civilization.quests.QuestGather;
import net.torocraft.toroquest.civilization.quests.QuestKillMobs;
import net.torocraft.toroquest.civilization.quests.QuestMine;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.QuestDelegator;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessagePlayerCivilizationSetInCiv;
import net.torocraft.toroquest.network.message.MessageSetPlayerReputation;

public class PlayerCivilizationCapabilityImpl extends PlayerCivilization implements PlayerCivilizationCapability {

	@CapabilityInject(PlayerCivilizationCapability.class)
	public static Capability<PlayerCivilizationCapability> INSTANCE = null;

	public static Achievement FRIEND_ACHIEVEMNT = new Achievement("civilization_friend", "civilization_friend", 0, 0, Items.DIAMOND_SWORD, null)
			.registerStat();
	public static Achievement ALLY_ACHIEVEMNT = new Achievement("civilization_ally", "civilization_ally", 0, 0, Items.DIAMOND_SWORD, null)
			.registerStat();
	public static Achievement HERO_ACHIEVEMNT = new Achievement("civilization_hero", "civilization_hero", 0, 0, Items.DIAMOND_SWORD, null)
			.registerStat();

	public static Achievement FIRST_QUEST_ACHIEVEMNT = new Achievement("first_quest", "first_quest", 0, 0, Items.DIAMOND_SWORD, null).registerStat();

	private final EntityPlayer player;

	public PlayerCivilizationCapabilityImpl(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void setReputation(CivilizationType civ, int amount) {
		if (civ == null) {
			return;
		}
		reputations.put(civ, amount);
		if (!player.getEntityWorld().isRemote) {
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetPlayerReputation(civ, amount), (EntityPlayerMP) player);
			MinecraftForge.EVENT_BUS.post(new CivilizationEvent.ReputationChange(player, civ, amount));

			ReputationLevel level = ReputationLevel.fromReputation(amount);

			if (ReputationLevel.FRIEND.equals(level)) {
				player.addStat(FRIEND_ACHIEVEMNT);
			} else if (ReputationLevel.ALLY.equals(level)) {
				player.addStat(ALLY_ACHIEVEMNT);
			} else if (ReputationLevel.HERO.equals(level)) {
				player.addStat(HERO_ACHIEVEMNT);
			}

		}
	}

	@Override
	public void adjustReputation(CivilizationType civ, int amount) {
		if (civ == null) {
			return;
		}
		if (reputations.get(civ) == null) {
			reputations.put(civ, 0);
		}
		setReputation(civ, reputations.get(civ) + amount);
	}

	@Override
	public void syncClient() {
		if (!player.getEntityWorld().isRemote) {
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessagePlayerCivilizationSetInCiv(inCiv), (EntityPlayerMP) player);
			for (Entry<CivilizationType, Integer> entry : reputations.entrySet()) {
				ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetPlayerReputation(entry.getKey(), entry.getValue()), (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public ReputationLevel getReputationLevel(CivilizationType civ) {
		return ReputationLevel.fromReputation(getReputation(civ));
	}

	@Override
	public int getReputation(CivilizationType civ) {
		return i(reputations.get(civ));
	}

	@Override
	public void updatePlayerLocation(int chunkX, int chunkZ) {
		Province prev = inCiv;
		Province curr = CivilizationUtil.getProvinceAt(player.worldObj, chunkX, chunkZ);

		if (equals(prev, curr)) {
			return;
		}

		setInCivilization(curr);

		if (!player.getEntityWorld().isRemote) {
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessagePlayerCivilizationSetInCiv(inCiv), (EntityPlayerMP) player);
		}

		if (prev != null) {
			MinecraftForge.EVENT_BUS.post(new CivilizationEvent.Leave(player, prev));
		}

		if (curr != null) {
			PlayerCivilizationCapabilityImpl.get(player).setInCivilization(curr);
			MinecraftForge.EVENT_BUS.post(new CivilizationEvent.Enter(player, curr));
		}
	}

	@Override
	public void setInCivilization(Province civ) {
		inCiv = civ;
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

	@Override
	public Province getInCivilization() {
		return inCiv;
	}

	@Override
	public String toString() {
		return "Player Civilization Info: " + player.getName() + ": IN_CIV[" + inCiv + "]";
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(PlayerCivilizationCapability.class, new PlayerCivilizationStorage(),
				new Callable<PlayerCivilizationCapability>() {
					@Override
					public PlayerCivilizationCapability call() throws Exception {
						return null;
					}
				});
	}

	private int i(Integer integer) {
		if (integer == null) {
			return 0;
		}
		return integer;
	}

	public static PlayerCivilizationCapability get(EntityPlayer player) {
		if (player == null) {
			throw new NullPointerException("NULL player");
		}
		return player.getCapability(PlayerCivilizationCapabilityImpl.INSTANCE, null);
	}

	@Override
	public Set<QuestData> getCurrentQuests() {
		return quests;
	}

	private boolean removeQuest(QuestData quest) {
		return quests.remove(quest);
	}

	@Override
	public QuestData getCurrentQuestFor(Province province) {
		for (QuestData q : getCurrentQuests()) {
			if (q.getProvinceId().equals(province.id)) {
				return q;
			}
		}
		return null;
	}

	@Override
	public QuestData getNextQuestFor(Province province) {
		for (QuestData q : nextQuests) {
			if (q.getProvinceId().equals(province.id)) {
				return q;
			}
		}
		return generateNextQuestFor(province);
	}

	private QuestData generateNextQuestFor(Province province) {
		QuestData q;
		Random rand = new Random();

		List<Quest> possibleQuests = new ArrayList<Quest>();
		possibleQuests.add(QuestFarm.INSTANCE);
		possibleQuests.add(QuestGather.INSTANCE);
		possibleQuests.add(QuestMine.INSTANCE);
		possibleQuests.add(QuestKillMobs.INSTANCE);

		if (getReputation(province.civilization) > 100) {
			possibleQuests.add(QuestCourier.INSTANCE);
		}
		if (getReputation(province.civilization) > 200) {
			possibleQuests.add(QuestEnemyEncampment.INSTANCE);
		}
		if (getReputation(province.civilization) > 500) {
			possibleQuests.add(QuestEnemyEncampment.INSTANCE);
		}

		q = possibleQuests.get(rand.nextInt(possibleQuests.size())).generateQuestFor(player, province);

		if (q == null) {
			List<Quest> fallbackQuests = new ArrayList<Quest>();
			fallbackQuests.add(QuestFarm.INSTANCE);
			fallbackQuests.add(QuestGather.INSTANCE);
			q = fallbackQuests.get(rand.nextInt(fallbackQuests.size())).generateQuestFor(player, province);
		}

		// Testing Override
		// q = QuestCourier.INSTANCE.generateQuestFor(player, province);

		nextQuests.add(q);
		return q;
	}

	@Override
	public List<ItemStack> acceptQuest(List<ItemStack> in) {
		Province province = getInCivilization();
		if (province == null) {
			return null;
		}

		if (getCurrentQuestFor(province) != null) {
			return null;
		}

		QuestData data = getNextQuestFor(province);
		quests.add(data);
		nextQuests.remove(data);

		return new QuestDelegator(data).accept(in);
	}

	@Override
	public List<ItemStack> rejectQuest(List<ItemStack> in) {
		Province province = getInCivilization();
		if (province == null) {
			return null;
		}
		QuestData data = getCurrentQuestFor(province);
		if (data == null) {
			return null;
		}

		List<ItemStack> out = new QuestDelegator(data).reject(in);

		if (out == null) {
			return null;
		}

		if (removeQuest(data)) {
			if (getReputation(province.civilization) > 10) {
				adjustReputation(province.civilization, -10);
			}
			return out;
		}

		return null;
	}

	@Override
	public List<ItemStack> completeQuest(List<ItemStack> in) {
		Province province = getInCivilization();
		if (province == null) {
			return null;
		}
		QuestData data = getCurrentQuestFor(province);
		if (data == null) {
			return null;
		}

		List<ItemStack> reward = new QuestDelegator(data).complete(in);

		/*
		 * quest not completed check
		 */
		if (reward == null) {
			return null;
		}

		/*
		 * quest was not in quest list check
		 */
		if (!removeQuest(data)) {
			return null;
		}

		completedQuests++;

		if (completedQuestsByProvince.get(province.id) == null) {
			completedQuestsByProvince.put(province.id, 0);
		}
		completedQuestsByProvince.put(province.id, completedQuestsByProvince.get(province.id) + 1);

		player.addStat(FIRST_QUEST_ACHIEVEMNT);

		return reward;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

}
