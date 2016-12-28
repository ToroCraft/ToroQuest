package net.torocraft.toroquest;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.torocraft.toroquest.entities.EntityToro;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageRequestPlayerCivilizationSync;
import net.torocraft.toroquest.util.TaskRunner;

public class EventHandlers {


	@SubscribeEvent
	public void handleWorldTick(WorldTickEvent event) {

	}

	@SubscribeEvent
	public void handleWorldTick2(ClientTickEvent event) {
		TaskRunner.run();
	}

	public static class SyncTask implements Runnable {
		public void run() {
			ToroQuestPacketHandler.INSTANCE.sendToServer(new MessageRequestPlayerCivilizationSync());
		}
	}

	@SubscribeEvent
	public void spawnToroWhenCowPackSpawns(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote) {
			return;
		}

		Entity entity = event.getEntity();
		if (entity == null || !(entity instanceof EntityCow)) {
			return;
		}

		if (entity.getEntityData().getBoolean("AddedToWorld")) {
			return;
		}

		entity.getEntityData().setBoolean("AddedToWorld", true);
		
		World world = event.getWorld();

		if (world.rand.nextInt(8) != 0) {
			return;
		}

		BlockPos spawnPoint = findSurface(world, entity.getPosition());

		if (spawnPoint == null) {
			return;
		}
		
		
		Entity toro = new EntityToro(world);

		toro.setPosition(spawnPoint.getX(), spawnPoint.getY() + 1, spawnPoint.getZ());
		world.spawnEntity(toro);
	}

	@SubscribeEvent
	public void toroDontLikeYouHurtingCows(LivingHurtEvent event) {
		EntityLivingBase victim = event.getEntityLiving();
		EntityLivingBase attacker = getAttacker(event);

		if (victim == null || attacker == null || !(victim instanceof EntityCow)) {
			return;
		}

		List<EntityToro> nearbyToros = victim.getEntityWorld().getEntitiesWithinAABB(EntityToro.class,
				victim.getEntityBoundingBox().expand(40.0D, 10.0D, 40.0D));
		for (EntityToro toro : nearbyToros) {
			toro.setAttackTarget(attacker);
		}
	}

	private EntityLivingBase getAttacker(LivingHurtEvent event) {
		try {
			return (EntityLivingBase) event.getSource().getEntity();
		} catch (Exception e) {
			return null;
		}
	}

	private BlockPos findSurface(World world, BlockPos start) {

		int minY = world.getActualHeight();

		BlockPos pos;

		IBlockState blockState;

		for (int y = world.getActualHeight(); y > 0; y--) {

			pos = new BlockPos(start.getX(), y, start.getZ());
			blockState = world.getBlockState(pos);

			if (isLiquid(blockState)) {
				return null;
			}

			if (isGroundBlock(blockState)) {
				if (y < minY) {
					minY = y;
				}

				break;
			}
		}

		return new BlockPos(start.getX(), minY, start.getZ());
	}

	private boolean isLiquid(IBlockState blockState) {
		return blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA;
	}

	private boolean isGroundBlock(IBlockState blockState) {

		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG
				|| blockState.getBlock() instanceof BlockBush) {
			return false;
		}

		return blockState.isOpaqueCube();

	}

}
