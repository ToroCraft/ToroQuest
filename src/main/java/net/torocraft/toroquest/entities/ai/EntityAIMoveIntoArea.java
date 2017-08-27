package net.torocraft.toroquest.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveIntoArea extends EntityAIBase {

	private final EntityCreature entity;

	private int radiusSq;
	private int centerX;
	private int centerZ;

	private double movePosX;
	private double movePosY;
	private double movePosZ;

	private final double movementSpeed;

	private boolean enabled = false;

	public EntityAIMoveIntoArea(EntityCreature entity, double speedIn, int radius) {
		this.entity = entity;
		this.movementSpeed = speedIn;
		this.radiusSq = radius * radius;

		this.setMutexBits(1);
	}

	public void enable() {
		enabled = true;
	}

	public void disable() {
		enabled = false;
	}

	public void setCenter(int centerX, int centerZ) {
		this.centerX = centerX;
		this.centerZ = centerZ;
		enable();
	}

	public boolean shouldExecute() {

		if (!enabled) {
			return false;
		}

		if (inCorrectPosition()) {
			return false;
		}

		Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 16, 7, new Vec3d((double) centerX, (double) entity.posY, (double) centerZ));

		if (vec3d == null) {
			return false;
		} else {
			this.movePosX = vec3d.x;
			this.movePosY = vec3d.y;
			this.movePosZ = vec3d.z;
			return true;
		}

	}

	public boolean inCorrectPosition() {
		double x = (double) centerX - entity.posX;
		double z = (double) centerZ - entity.posZ;
		double dist = x * x + z * z;
		return dist < radiusSq;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
	}
}