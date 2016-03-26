package net.torocraft.baitmod;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.util.math.BlockPos;
import sun.util.logging.resources.logging;

public class EntityAIMoveToPosition extends EntityAIBase {

	private final EntityCreature entity;
	private final double speed;
	private int timeoutCounter;
	
	private boolean done = false;

	/** Controls task execution delay */
	protected int runDelay;

	protected BlockPos destination;

	EntityAIMoveToPosition(EntityCreature creature, BlockPos destination, double speedIn) {
		this.entity = creature;
		this.speed = speedIn;
		this.destination = destination;
		this.setMutexBits(5);
	}
	
	private void log(String message) {
		if (!entity.worldObj.isRemote) {
			System.out.println(message);
		}
	}
	
	@Override
	public boolean shouldExecute() {
		if( isTimedout() || done){
			//log("Timedout");
			//remove();
			return false;
		}
		return true;
	}

	private boolean isTimedout() {
		return timeoutCounter > 1200;
	}

	@Override
	public void startExecuting() {
		moveToDestination();
	}

	public void remove() {
		//entity.tasks.removeTask(this);
		this.done = true;
	}

	public void updateTask() {
		timeoutCounter++;
		if (isAboveDestination()) {
			if (this.timeoutCounter % 40 == 0) {
				moveToDestination();
			}
		}
	}

	private void moveToDestination() {
		entity.getNavigator().tryMoveToXYZ(getTargetX(), getTargetY(), getTargetZ(), speed);
	}
	
	private double getTargetZ() {
		return (double) ((float) this.destination.getZ()) + 0.5D;
	}

	private double getTargetY() {
		return (double) (this.destination.getY() + 1);
	}

	private double getTargetX() {
		return (double) ((float) this.destination.getX()) + 0.5D;
	}

	private boolean isAboveDestination() {
		return entity.getDistanceSqToCenter(destination.up()) > 1.0D;
	}

	

}