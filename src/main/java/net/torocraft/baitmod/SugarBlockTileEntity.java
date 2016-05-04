package net.torocraft.baitmod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class SugarBlockTileEntity extends TileEntity implements ITickable {

	private List<EntityAIMoveToPosition> activeTasks = new ArrayList<EntityAIMoveToPosition>();
	
	private static final double RANGE = 20;
	
	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		if (!isRunTick()) {
			return;
		}
		findEntities();
	}

	private boolean isRunTick() {

		return false; // FIXME
		// return worldObj.getTotalWorldTime() % 160L == 0L;
	}

	private void log(String message) {
		if (!worldObj.isRemote) {
			System.out.println(message);
		}
	}

	public void findEntities() {
		int radius = 50;

		AxisAlignedBB attractorBounds = new AxisAlignedBB(pos.getX() - RANGE, pos.getY() - RANGE, pos.getZ() - RANGE,
				pos.getX() + 50d, pos.getY() + 50d, pos.getZ() + 50d);

		List<EntityCreature> entsInBounds = worldObj.getEntitiesWithinAABB(EntityCreature.class, attractorBounds);

		for (EntityCreature e : entsInBounds) {
			attractUsingAITask(e);
		}
	}



	private void attractUsingAITask(EntityCreature entity) {
		if (hasMoveToTask(entity)) {
			return;
		}
		addTask(entity);
	}

	private void addTask(EntityCreature entity) {
		EntityAIMoveToPosition task = new EntityAIMoveToPosition(entity, pos);
		activeTasks.add(task);
		entity.tasks.addTask(0, task);
	}
	
	private boolean hasMoveToTask(EntityLiving entity) {
		for (EntityAITaskEntry task : entity.tasks.taskEntries) {
			if (task.action instanceof EntityAIMoveToPosition) {
				return true;
			}
		}
		return false;
	}

	public void cleanUp() {
		for(EntityAIMoveToPosition task : activeTasks) {
			task.getEntity().tasks.removeTask(task);
		}
		activeTasks.clear();
	}

	
	

}
