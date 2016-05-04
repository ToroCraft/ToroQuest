package net.torocraft.baitmod;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveToPosition extends EntityAIBase {
	private final EntityLiving entity;
	private final double movementSpeed;
	/** Controls task execution delay */
	protected int runDelay;
	private int timeoutCounter;
	private int field_179490_f;
	/** Block to move to */

	
	private double targetX;
	private double targetY;
	private double targetZ;

	private boolean isAboveDestination;

	public EntityAIMoveToPosition(EntityLiving creature, BlockPos target) {
		this.entity = creature;
		this.movementSpeed = 0.75;
		this.setMutexBits(5);

		targetX = target.getX() + 0.5d;
		targetY = target.getY() + 0.5d;
		targetZ = target.getZ() + 0.5d;
	}

	public EntityLiving getEntity() {
		return entity;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		return !isAboveDestination;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !isAboveDestination;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {

	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
	}

	boolean moving = false;

	/**
	 * Updates the task
	 */
	public void updateTask() {

		double distance = distanceFromDestination();

		if (distance <= 1) {
			isAboveDestination = true;
			timeoutCounter = 0;
			return;
		}

		timeoutCounter++;
		if (moving && timeoutCounter < 40) {
			return;
		}

		moving = entity.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, movementSpeed);

		/// !this.entity.getNavigator().noPath()

		timeoutCounter = 0;
	}

	private double distanceFromXDesination() {
		return entity.posX - targetX;
	}

	private double distanceFromZDesination() {
		return entity.posZ - targetZ;
	}

	private double distanceFromDestination() {
		double x = distanceFromXDesination();
		double z = distanceFromZDesination();
		return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
	}

	protected boolean getIsAboveDestination() {
		return this.isAboveDestination;
	}


}