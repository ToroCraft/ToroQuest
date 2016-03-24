package com.frodare.baitmod;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;

public class SugarBlockTileEntity extends TileEntity implements ITickable {

	@Override
	public void update() {
		if (!isRunTick()) {
			return;
		}
		log("Sugar Block Tick");

		findEntities();
	}

	private boolean isRunTick() {
		return this.worldObj.getTotalWorldTime() % 80L == 0L;
	}

	private void log(String message) {
		if (!this.worldObj.isRemote) {
			System.out.println(message);
		}
	}

	public void findEntities() {

		int radius = 50;

		AxisAlignedBB attractorBounds = new AxisAlignedBB(pos.getX() - 50d, pos.getY() - 50d, pos.getZ() - 50d,
				pos.getX() + 50d, pos.getY() + 50d, pos.getZ() + 50d);

		List<EntityLiving> entsInBounds = worldObj.getEntitiesWithinAABB(EntityLiving.class, attractorBounds);

		for (EntityLiving e : entsInBounds) {
			log("Found entity [" + e.getClass().getSimpleName() + "]");
		}

	}

	private boolean attractyUsingAITask(EntityLiving ent) {
		// tracking.add(ent);
		
		/*
		List<EntityAITaskEntry> entries = ent.tasks.taskEntries;
		boolean hasTask = false;
		EntityAIBase remove = null;
		boolean isTracked;

		for (EntityAITaskEntry entry : entries) {
			if (entry.action instanceof AttractTask) {
				AttractTask at = (AttractTask) entry.action;
				if (at.coord.equals(new BlockCoord(this)) || !at.continueExecuting()) {
					remove = entry.action;
				} else {
					return false;
				}
			}
		}
		if (remove != null) {
			ent.tasks.removeTask(remove);
		}
		cancelCurrentTasks(ent);
		ent.tasks.addTask(0, new AttractTask(ent, getTarget(), new BlockCoord(this)));
		*/
		return true;
	}

	private static class AttractTask extends EntityAIBase {

		private EntityLiving mob;
		
		private double x;
		private double y;
		private double z;

		private FakePlayer target;
		private String entityId;
		private int updatesSincePathing;

		private boolean started = false;

		private AttractTask(EntityLiving mob, FakePlayer target, double x, double y, double z) {
			this.mob = mob;
			this.x = x;
			this.y = y;
			this.z = z;
			this.target = target;
			entityId = EntityList.getEntityString(mob);
		}

		@Override
		public boolean shouldExecute() {
			return continueExecuting();
		}

		@Override
		public void resetTask() {
			started = false;
			updatesSincePathing = 0;
		}

		@Override
		public boolean continueExecuting() {
			/*
			boolean res = false;
			TileEntity te = mob.worldObj.getTileEntity(new BlockPos(x, y, z));
			if (te instanceof TileAttractor) {
				TileAttractor attractor = (TileAttractor) te;
				res = attractor.canAttract(entityId, mob);
			}
			return res;
			*/
			return true;
		}

		@Override
		public boolean isInterruptible() {
			return true;
		}

		@Override
		public void updateTask() {
			if (!started || updatesSincePathing > 20) {
				started = true;
				int speed = 1;
				//mob.getNavigator().setAvoidsWater(false);
				boolean res = mob.getNavigator().tryMoveToEntityLiving(target, speed);
				if (!res) {
					mob.getNavigator().tryMoveToXYZ(target.posX, target.posY + 1, target.posZ, speed);
				}
				updatesSincePathing = 0;
			} else {
				updatesSincePathing++;
			}
		}

	}

}
