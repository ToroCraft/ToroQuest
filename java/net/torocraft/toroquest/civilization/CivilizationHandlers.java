package net.torocraft.toroquest.civilization;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
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
import net.torocraft.toroquest.entities.EntitySentry;
import net.torocraft.toroquest.entities.EntityToroNpc;
import net.torocraft.toroquest.entities.EntityVampireBat;
import net.torocraft.toroquest.util.TaskRunner;

public class CivilizationHandlers {
	private static final float THEIFT_FACTOR = -1.2f;

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
		if (!event.isWasDeath()) {
			return;
		}

		PlayerCivilizationCapability newCap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		PlayerCivilizationCapability originalCap = PlayerCivilizationCapabilityImpl.get(event.getOriginal());

		if (newCap == null || originalCap == null) {
			return;
		}

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
		event.getEntityPlayer().getEntityData().setTag(ToroQuest.MODID + ".playerCivilization", cap.writeNBT());
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
		cap.readNBT((NBTTagCompound) event.getEntityPlayer().getEntityData().getTag(ToroQuest.MODID + ".playerCivilization"));
	}

	@SubscribeEvent
	public void onEntityLoad(final AttachCapabilitiesEvent.Entity event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntity();
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

		private final EntityPlayer player;
		private PlayerCivilizationCapability instance;

		public PlayerCivilizationCapabilityProvider(EntityPlayer player) {
			this.player = player;
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

	@SubscribeEvent
	public void checkForDonationsInCivilization(PlaceEvent event) {
		if (event.getPlayer() == null) {
			return;
		}

		int value = blockValue(event.getState().getBlock());

		if (value < 1) {
			return;
		}

		BlockPos pos = event.getBlockSnapshot().getPos();
		adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, value);
	}

	@SubscribeEvent
	public void checkForTheftInCivilization(BreakEvent event) {
		if (event.getPlayer() == null) {
			return;
		}

		int value = blockValue(event.getState().getBlock());

		if (value < 1) {
			return;
		}

		BlockPos pos = event.getPos();
		adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, (int) Math.ceil(value * THEIFT_FACTOR));
	}

	protected void adjustPlayerRep(EntityPlayer player, int chunkX, int chunkZ, int value) {
		Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), chunkX, chunkZ);
		if (province == null || province.civilization == null) {
			return;
		}
		PlayerCivilizationCapabilityImpl.get(player).adjustPlayerReputation(province.civilization, value);
	}

	private int blockValue(Block b) {
		if (b == Blocks.IRON_BLOCK) {
			return 10;
		} else if (b == Blocks.GOLD_BLOCK) {
			return 20;
		} else if (b == Blocks.DIAMOND_BLOCK) {
			return 50;
		} else if (b == Blocks.EMERALD_BLOCK) {
			return 60;
		} else if (b == Blocks.BEACON) {
			return 200;
		} else {
			return 0;
		}
	}

	@SubscribeEvent
	public void checkKillsInCivilization(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victum = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		}

		if (player == null) {
			return;
		}

		Province province = PlayerCivilizationCapabilityImpl.get(player).getPlayerInCivilization();

		if (province == null || province.civilization == null) {
			return;
		}

		PlayerCivilizationCapabilityImpl.get(player).adjustPlayerReputation(province.civilization, getRepuationAdjustmentFor(victum, province));

	}

	private int getRepuationAdjustmentFor(EntityLivingBase victim, Province province) {

		if (province == null || province.civilization == null) {
			return 0;
		}

		if (victim instanceof EntityVillager) {
			return -10;
		}

		if (isHostileMob(victim)) {
			return 1;
		}

		if (victim instanceof EntityToroNpc) {
			CivilizationType npcCiv = ((EntityToroNpc) victim).getCivilization();

			if (npcCiv == null) {
				return -1;
			}

			if (npcCiv.equals(province.civilization)) {
				return -10;
			} else {
				return 10;
			}
		}

		return -1;
	}

	private boolean isHostileMob(EntityLivingBase victim) {
		return victim instanceof EntityMob || victim instanceof EntityMagmaCube || victim instanceof EntityGhast || victim instanceof EntityShulker;
	}

	@SubscribeEvent
	public void handleEnteringProvince(EntityEvent.EnteringChunk event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		PlayerCivilizationCapabilityImpl.get(player).updatePlayerLocation(event.getNewChunkX(), event.getNewChunkZ());
	}

	private String leavingMessage(EntityPlayer player, CivilizationType civ) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ);
		if (rep >= 10) {
			return civ.getFriendlyLeavingMessage();
		} else if (rep <= -10) {
			return civ.getHostileLeavingMessage();
		} else {
			return civ.getNeutralLeavingMessage();
		}
	}

	private String enteringMessage(EntityPlayer player, CivilizationType civ) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ);
		if (rep >= 10) {
			return civ.getFriendlyEnteringMessage();
		} else if (rep <= -10) {
			return civ.getHostileEnteringMessage();
		} else {
			return civ.getNeutralEnteringMessage();
		}
	}

	private void chat(EntityPlayer player, String message) {
		player.addChatMessage(new TextComponentString(message));
	}

	@SubscribeEvent
	public void breed(BabyEntitySpawnEvent event) {
		if (!(event.getParentA() instanceof EntityAnimal)) {
			return;
		}

		if (!(event.getParentB() instanceof EntityAnimal)) {
			return;
		}

		EntityPlayer playerA = ((EntityAnimal) event.getParentA()).getPlayerInLove();
		EntityPlayer playerB = ((EntityAnimal) event.getParentB()).getPlayerInLove();

		if (playerA != null) {
			adjustPlayerRep(playerA, event.getParentA().chunkCoordX, event.getParentA().chunkCoordZ, 1);
		}

		if (playerB != null) {
			adjustPlayerRep(playerB, event.getParentB().chunkCoordX, event.getParentB().chunkCoordZ, 1);
		}
	}

	@SubscribeEvent
	public void farm(PlaceEvent event) {
		if (event.getPlayer() == null) {
			return;
		}

		if (event.getPlacedBlock().getBlock() instanceof BlockCrops) {

			/*
			 * if using bone meal, only give positive reputation 20% of the time
			 */
			if (event.getBlockSnapshot().getReplacedBlock().getBlock() instanceof BlockCrops) {
				if (event.getWorld().rand.nextInt(100) > 20) {
					return;
				}
			}

			BlockPos pos = event.getBlockSnapshot().getPos();
			adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, 1);
		}
	}

	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		if (event.getState().getBlock() instanceof BlockCrops) {
			BlockPos pos = event.getPos();
			AxisAlignedBB bb = new AxisAlignedBB(pos);
			List<EntityPlayer> players = event.getWorld().getEntitiesWithinAABB(EntityPlayer.class, bb);
			if (players != null && players.size() > 0) {
				for (EntityPlayer player : players) {
					adjustPlayerRep(player, pos.getX() / 16, pos.getZ() / 16, -1);
				}
			}
		}
	}

	@SubscribeEvent
	public void harvest(BreakEvent event) {
		if (event.getPlayer() == null) {
			return;
		}

		if (event.getState().getBlock() instanceof BlockCrops) {
			BlockPos pos = event.getPos();
			adjustPlayerRep(event.getPlayer(), pos.getX() / 16, pos.getZ() / 16, -1);
		}
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

		spawnSentry(player.getPosition(), world);
	}

	protected void spawnSentry(BlockPos position, World world) {
		BlockPos randomNearbySpot = position.add(randomSpawnDistance(world.rand), 0, randomSpawnDistance(world.rand));

		Province province = CivilizationUtil.getProvinceAt(world, randomNearbySpot.getX() / 16, randomNearbySpot.getZ() / 16);

		if (province == null) {
			return;
		}

		BlockPos spawnPos = findSpawnLocationFrom(world, randomNearbySpot);

		if (spawnPos == null) {
			return;
		}

		int localMobCount = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(spawnPos).expand(50, 16, 50)).size();
		int localSentryCount = world.getEntitiesWithinAABB(EntityVampireBat.class, new AxisAlignedBB(spawnPos).expand(50, 40, 50)).size();

		if (localSentryCount > 5 + (localMobCount / 10)) {
			return;
		}

		int count = world.rand.nextInt(3) + 1;

		for (int i = 0; i < count; i++) {
			EntitySentry e = new EntitySentry(world);
			e.setCivilization(province.civilization);
			e.setPosition(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
			e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), (IEntityLivingData) null);
			world.spawnEntityInWorld(e);
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
