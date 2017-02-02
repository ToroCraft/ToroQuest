package net.torocraft.toroquest;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityToro;
import net.torocraft.toroquest.item.IExtendedReach;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageExtendedReachAttack;
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(MouseEvent event) {
		if (event.getButton() == 0 && event.isButtonstate()) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer thePlayer = mc.player;
			if (thePlayer != null) {
				ItemStack itemstack = thePlayer.getHeldItemMainhand();
				IExtendedReach ieri;
				if (itemstack != null) {
					if (itemstack.getItem() instanceof IExtendedReach) {
						ieri = (IExtendedReach) itemstack.getItem();
					} else {
						ieri = null;
					}

					if (ieri != null) {
						float reach = ieri.getReach();
						RayTraceResult mov = getMouseOverExtended(reach);

						if (mov != null) {
							if (mov.entityHit != null && mov.entityHit.hurtResistantTime == 0) {
								if (mov.entityHit != thePlayer) {
									ToroQuestPacketHandler.INSTANCE.sendToServer(new MessageExtendedReachAttack(mov.entityHit.getEntityId()));
								}
							}
						}
					}
				}
			}
		}
	}

	// This is mostly copied from the EntityRenderer#getMouseOver() method
	public static RayTraceResult getMouseOverExtended(float dist) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		Entity theRenderViewEntity = mc.getRenderViewEntity();
		AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.posX - 0.5D, theRenderViewEntity.posY - 0.0D,
				theRenderViewEntity.posZ - 0.5D, theRenderViewEntity.posX + 0.5D, theRenderViewEntity.posY + 1.5D, theRenderViewEntity.posZ + 0.5D);
		RayTraceResult returnMOP = null;
		if (mc.world != null) {
			double var2 = dist;
			returnMOP = theRenderViewEntity.rayTrace(var2, 0);
			double calcdist = var2;
			Vec3d pos = theRenderViewEntity.getPositionEyes(0);
			var2 = calcdist;
			if (returnMOP != null) {
				calcdist = returnMOP.hitVec.distanceTo(pos);
			}

			Vec3d lookvec = theRenderViewEntity.getLook(0);
			Vec3d var8 = pos.addVector(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2);
			Entity pointedEntity = null;
			float var9 = 1.0F;
			
			List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity,
					theViewBoundingBox.addCoord(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2).expand(var9, var9, var9));
			double d = calcdist;

			for (Entity entity : list) {
				if (entity.canBeCollidedWith()) {
					float bordersize = entity.getCollisionBorderSize();
					AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - entity.width / 2, entity.posY, entity.posZ - entity.width / 2,
							entity.posX + entity.width / 2, entity.posY + entity.height, entity.posZ + entity.width / 2);
					aabb.expand(bordersize, bordersize, bordersize);
					RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);

					if (aabb.isVecInside(pos)) {
						if (0.0D < d || d == 0.0D) {
							pointedEntity = entity;
							d = 0.0D;
						}
					} else if (mop0 != null) {
						double d1 = pos.distanceTo(mop0.hitVec);

						if (d1 < d || d == 0.0D) {
							pointedEntity = entity;
							d = d1;
						}
					}
				}
			}

			if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
				returnMOP = new RayTraceResult(pointedEntity);
			}
		}
		return returnMOP;
	}

}
