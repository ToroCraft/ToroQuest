package net.torocraft.toroquest.civilization;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.torocraft.toroquest.EventHandlers.SyncTask;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapability;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.config.ToroQuestConfiguration;
import net.torocraft.toroquest.entities.EntityFugitive;
import net.torocraft.toroquest.entities.EntitySentry;
import net.torocraft.toroquest.entities.EntityToroNpc;
import net.torocraft.toroquest.entities.EntityVillageLord;
import net.torocraft.toroquest.util.TaskRunner;

public class CivilizationHandlers {

	@SubscribeEvent
	public void handleReputationChange(CivilizationEvent.ReputationChange event) {

	}

	@SubscribeEvent
	public void handleEnterProvince(CivilizationEvent.Enter event) {

	}

	@SubscribeEvent
	public void handleLeaveProvince(CivilizationEvent.Leave event) {

	}

	@SubscribeEvent
	public void onDeath(PlayerEvent.Clone event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}

		PlayerCivilizationCapability newCap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		PlayerCivilizationCapability originalCap = PlayerCivilizationCapabilityImpl.get(event.getOriginal());

		if (originalCap == null) {
			return;
		}

		if (newCap == null) {
			throw new NullPointerException("missing player capability during clone");
		}

		// System.out.println("CLONE: " + originalCap.writeNBT());

		newCap.readNBT(originalCap.writeNBT());
	}

	@SubscribeEvent
	public void onSave(PlayerEvent.SaveToFile event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}
		PlayerCivilizationCapability cap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		if (cap == null) {
			return;
		}

		NBTTagCompound civData = cap.writeNBT();

		// System.out.println("SAVE: " + civData);

		if (civData == null || civData.getTag("reputations") == null || ((NBTTagList) civData.getTag("reputations")).tagCount() < 1) {
			// System.out.println("******************Not writing empty ToroQuest
			// data for player " + event.getEntityPlayer().getName());
			return;
		}

		event.getEntityPlayer().getEntityData().setTag(ToroQuest.MODID + ".playerCivilization", civData);
	}

	@SubscribeEvent
	public void onLoad(PlayerEvent.LoadFromFile event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}

		PlayerCivilizationCapability cap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		if (cap == null) {
			return;
		}

		NBTTagCompound c = event.getEntityPlayer().getEntityData().getCompoundTag(ToroQuest.MODID + ".playerCivilization");

		if (c == null) {
			// System.out.println("******************Missing civ data on load");
		} else {
			System.out.println("LOAD: " + c.toString());
		}

		cap.readNBT(c);
	}

	@SubscribeEvent
	public void onEntityLoad(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getObject();
		event.addCapability(new ResourceLocation(ToroQuest.MODID, "playerCivilization"), new PlayerCivilizationCapabilityProvider(player));
		syncClientCapability(player);
	}

	private void syncClientCapability(EntityPlayer player) {
		if (player.getEntityWorld().isRemote) {
			TaskRunner.queueTask(new SyncTask(), 30);
		}
	}

	public static class PlayerCivilizationCapabilityProvider implements ICapabilityProvider {

		@CapabilityInject(PlayerCivilizationCapability.class)
		public static final Capability<PlayerCivilizationCapability> CAP = null;

		private PlayerCivilizationCapability instance;

		public PlayerCivilizationCapabilityProvider(EntityPlayer player) {
			instance = new PlayerCivilizationCapabilityImpl(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CAP;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (CAP != null && capability == CAP) {
				return PlayerCivilizationCapabilityImpl.INSTANCE.cast(instance);
			}
			return null;
		}
	}

	public static void adjustPlayerRep(EntityPlayer player, int chunkX, int chunkZ, int value) {
		Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), chunkX, chunkZ);
		if (province == null || province.civilization == null) {
			return;
		}
		PlayerCivilizationCapabilityImpl.get(player).adjustReputation(province.civilization, value);
	}

	@SubscribeEvent
	public void checkKillsInCivilization(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victim = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getTrueSource() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getTrueSource();
		}

		if (player == null) {
			return;
		}

		Province province = PlayerCivilizationCapabilityImpl.get(player).getInCivilization();

		if (province == null || province.civilization == null) {
			return;
		}

		PlayerCivilizationCapabilityImpl.get(player).adjustReputation(province.civilization, getRepuationAdjustmentFor(victim, province));

	}

	private int getRepuationAdjustmentFor(EntityLivingBase victim, Province province) {

		if (province == null || province.civilization == null) {
			return 0;
		}

		if (victim instanceof EntityFugitive) {
			return 5;
		}

		if (victim.getClass().getName().equals("net.minecraft.entity.passive.EntityVillager")) {
			return -10;
		}

		if (isHostileMob(victim)) {
			return 1;
		}

		if (victim instanceof EntityToroNpc) {
			CivilizationType npcCiv = ((EntityToroNpc) victim).getCivilization();

			int amount = 0;

			if (npcCiv == null) {
				amount = -1;
			} else if (npcCiv.equals(province.civilization)) {
				amount = -10;
			} else {
				amount = 10;
			}

			if (victim instanceof EntityVillageLord) {
				amount *= 10;
			}

			return amount;
		}

		if (victim instanceof EntityBat) {
			return 0;
		}

		if (ToroQuestConfiguration.animalsAffectRep && isAnimal(victim)) {
			return -1;
		} else {
			return 0;
		}

	}

	private boolean isAnimal(EntityLivingBase victim) {
		return victim instanceof EntityCow ||
				victim instanceof EntityHorse ||
				victim instanceof EntityPig ||
				victim instanceof EntityDonkey ||
				victim instanceof EntityChicken ||
				victim instanceof EntitySheep;
	}

	private boolean isHostileMob(EntityLivingBase victim) {
		return victim instanceof EntityMob ||
				victim instanceof EntitySlime ||
				victim instanceof EntityMagmaCube ||
				victim instanceof EntityGhast ||
				victim instanceof EntityShulker;
	}

	@SubscribeEvent
	public void handleEnteringProvince(EntityEvent.EnteringChunk event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		PlayerCivilizationCapabilityImpl.get(player).updatePlayerLocation(event.getNewChunkX(), event.getNewChunkZ());
	}

	public static TextComponentString leavingMessage(EntityPlayer player, Province province) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getReputation(province.civilization);
		String s;
		if (rep >= 10) {
			s = province.civilization.getFriendlyLeavingMessage(province);
		} else if (rep <= -10) {
			s = province.civilization.getHostileLeavingMessage(province);
		} else {
			s = province.civilization.getNeutralLeavingMessage(province);
		}
		return new TextComponentString(s);
	}

	public static TextComponentString enteringMessage(EntityPlayer player, Province province) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getReputation(province.civilization);
		String s;
		if (rep >= 10) {
			s = province.civilization.getFriendlyEnteringMessage(province);
		} else if (rep <= -10) {
			s = province.civilization.getHostileEnteringMessage(province);
		} else {
			s = province.civilization.getNeutralEnteringMessage(province);
		}
		return new TextComponentString(s);
	}

	@SubscribeEvent
	public void breed(BabyEntitySpawnEvent event) {
		if (!ToroQuestConfiguration.animalsAffectRep) {
			return;
		}
		if (!(event.getParentA() instanceof EntityAnimal)) {
			return;
		}

		if (!(event.getParentB() instanceof EntityAnimal)) {
			return;
		}

		EntityPlayer playerA = ((EntityAnimal) event.getParentA()).getLoveCause();
		EntityPlayer playerB = ((EntityAnimal) event.getParentB()).getLoveCause();

		if (playerA != null) {
			adjustPlayerRep(playerA, event.getParentA().chunkCoordX, event.getParentA().chunkCoordZ, 1);
		}

		if (playerB != null) {
			adjustPlayerRep(playerB, event.getParentB().chunkCoordX, event.getParentB().chunkCoordZ, 1);
		}
	}

	private int getFarmRepAmount(Random rand) {
		return rand.nextInt(5) == 0 ? 1 : 0;
	}

	@SubscribeEvent
	public void farm(PlaceEvent event) {
		if (!ToroQuestConfiguration.cropsAffectRep) {
			return;
		}
		if (event.getPlayer() == null) {
			return;
		}

		if (isCrop(event.getState().getBlock())) {
			/*
			 * if using bone meal, only give positive reputation 20% of the time
			 */
			if (isCrop(event.getBlockSnapshot().getReplacedBlock().getBlock())) {
				if (event.getWorld().rand.nextInt(100) > 20) {
					return;
				}
			}

			BlockPos pos = event.getBlockSnapshot().getPos();
			adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, getFarmRepAmount(event.getWorld().rand));

		}
	}

	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		if (!ToroQuestConfiguration.cropsAffectRep) {
			return;
		}
		if (isCrop(event.getState().getBlock())) {
			BlockPos pos = event.getPos();
			AxisAlignedBB bb = new AxisAlignedBB(pos);
			List<EntityPlayer> players = event.getWorld().getEntitiesWithinAABB(EntityPlayer.class, bb);
			if (players != null && players.size() > 0) {
				for (EntityPlayer player : players) {
					adjustPlayerRep(player, pos.getX() / 16, pos.getZ() / 16, -getFarmRepAmount(event.getWorld().rand));
				}
			}
		}
	}

	@SubscribeEvent
	public void harvest(BreakEvent event) {
		if (!ToroQuestConfiguration.cropsAffectRep) {
			return;
		}
		if (event.getPlayer() == null) {
			return;
		}

		if (isCrop(event.getState().getBlock())) {
			BlockPos pos = event.getPos();
			adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, -getFarmRepAmount(event.getWorld().rand));
		}
	}

	public static boolean isCrop(Block block) {
		return block instanceof BlockCrops || block instanceof BlockStem;
	}

	private int randomSpawnDistance(Random rand) {
		int d = rand.nextInt(20) + 20;
		if (rand.nextBoolean()) {
			d = -d;
		}
		return d;
	}

	@SubscribeEvent
	public void spawnSentries(PlayerTickEvent event) {
		if (TickEvent.Phase.START.equals(event.phase)) {
			return;
		}
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();

		if (world.isRemote || world.getTotalWorldTime() % 200 != 0) {
			return;
		}

		spawnSentry(player, player.getPosition(), world);
		spawnFugitive(player.getPosition(), world);
	}

	private void spawnFugitive(BlockPos position, World world) {
		BlockPos randomNearbySpot = position.add(randomSpawnDistance(world.rand), 0, randomSpawnDistance(world.rand));

		if (world.rand.nextInt(200) != 0) {
			return;
		}

		Province province = CivilizationUtil.getProvinceAt(world, randomNearbySpot.getX() / 16, randomNearbySpot.getZ() / 16);

		if (province == null) {
			return;
		}

		BlockPos spawnPos = findSpawnLocationFrom(world, randomNearbySpot);

		if (spawnPos == null) {
			return;
		}

		int localFugitiveCount = world.getEntitiesWithinAABB(EntityFugitive.class, new AxisAlignedBB(spawnPos).expand(50, 40, 50)).size();

		if (localFugitiveCount > 1) {
			return;
		}

		EntityFugitive e = new EntityFugitive(world);
		e.setPosition(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
		e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), (IEntityLivingData) null);
		world.spawnEntity(e);
	}

	protected void spawnSentry(EntityPlayer player, BlockPos position, World world) {
		if (world == null || world.provider == null) {
			return;
		}

		if (world.provider.getDimension() != 0) {
			return;
		}
		BlockPos randomNearbySpot = position.add(randomSpawnDistance(world.rand), 0, randomSpawnDistance(world.rand));

		Province province = CivilizationUtil.getProvinceAt(world, randomNearbySpot.getX() / 16, randomNearbySpot.getZ() / 16);

		if (province == null) {
			return;
		}

		if (!province.hasLord) {
			return;
		}

		BlockPos spawnPos = findSpawnLocationFrom(world, randomNearbySpot);

		if (spawnPos == null) {
			return;
		}

		int localSentryCount = world.getEntitiesWithinAABB(EntitySentry.class, new AxisAlignedBB(spawnPos).expand(50, 40, 50)).size();

		int rep = PlayerCivilizationCapabilityImpl.get(player).getReputation(province.civilization);

		int extraSpawns = 0;

		if (rep < -10) {
			extraSpawns = Math.round(-rep / 50f);
		}

		if (localSentryCount > (5 + extraSpawns)) {
			return;
		}

		int count = world.rand.nextInt(3) + 1;

		for (int i = 0; i < count; i++) {
			EntitySentry e = new EntitySentry(world);
			e.setCivilization(province.civilization);
			e.setPosition(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
			e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), (IEntityLivingData) null);
			world.spawnEntity(e);
		}

	}

	private BlockPos findSpawnLocationFrom(World world, BlockPos from) {
		BlockPos spawnPos = from.add(0, 20, 0);
		boolean[] airSpace = { false, false };
		IBlockState blockState;

		// TODO improve so sentries don't always spawn at the highest possible
		// location
		for (int i = 0; i < 40; i++) {
			blockState = world.getBlockState(spawnPos);

			if (isAir(blockState)) {
				if (airSpace[0]) {
					airSpace[1] = true;
				} else {
					airSpace[0] = true;
				}
			} else if (isGroundBlock(blockState)) {
				if (airSpace[0] && airSpace[1]) {
					return spawnPos.up();
				} else {
					airSpace[0] = false;
					airSpace[1] = false;
				}
			} else {
				airSpace[0] = false;
				airSpace[1] = false;
			}

			spawnPos = spawnPos.down();
		}

		return null;
	}

	private boolean isAir(IBlockState blockState) {
		return blockState.getBlock() == Blocks.AIR;
	}

	private boolean isGroundBlock(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

}
