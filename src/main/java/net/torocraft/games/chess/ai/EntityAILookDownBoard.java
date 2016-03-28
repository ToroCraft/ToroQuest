package net.torocraft.games.chess.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.torocraft.games.chess.pieces.IChessPiece.Side;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;

public class EntityAILookDownBoard extends EntityAIBase {

	protected EntityChessPiece entity;

	public EntityAILookDownBoard(EntityChessPiece entitylivingIn) {
		this.entity = entitylivingIn;
		this.setMutexBits(2);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		return entity.getRNG().nextFloat() >= 0.75;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return true;
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

	/**
	 * Updates the task
	 */
	public void updateTask() {

		double lookToX = entity.posX;
		double lookToY = entity.posY + entity.getEyeHeight();

		//System.out.println("Look side [" + entity.getSide() + " ]");
		
		double lookToZ;
		if (Side.BLACK.equals(entity.getSide())) {
			lookToZ = entity.posZ - 200;
		} else {
			lookToZ = entity.posZ + 200;
		}


		entity.getLookHelper().setLookPosition(lookToX, lookToY, lookToZ, (float) entity.func_184649_cE(),
				(float) this.entity.getVerticalFaceSpeed());

	}
}
