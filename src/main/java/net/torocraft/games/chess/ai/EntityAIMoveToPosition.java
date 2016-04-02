package net.torocraft.games.chess.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.torocraft.games.checkerboard.CheckerBoard;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;
import sun.util.logging.resources.logging;

public class EntityAIMoveToPosition extends EntityAIBase {
	private final EntityChessPiece entity;
	private final double movementSpeed;
	/** Controls task execution delay */
	protected int runDelay;
	private int timeoutCounter;
	private int field_179490_f;
	/** Block to move to */

	protected String chessPosition;
	protected BlockPos destination;
	private double targetX;
	private double targetY;
	private double targetZ;

	private boolean isAboveDestination;

	public EntityAIMoveToPosition(EntityChessPiece creature) {
		this.entity = creature;
		this.movementSpeed = 1;
		this.setMutexBits(5);
	}

	private void updateDestination() {

		if (entity.getGamePosition() == null || entity.getChessPosition() == null) {
			destination = null;
			return;
		}

		if (entity.getChessPosition().equals(chessPosition)) {
			return;
		}

		determineDestination();
	}

	private void determineDestination() {
		if (entity.getGamePosition() == null) {
			return;
		}
		// isAboveDestination = false;
		chessPosition = entity.getChessPosition();
		destination = CheckerBoard.getPosition(entity.getGamePosition(), chessPosition);
		targetX = (double) this.destination.getX() + 0.5d;
		targetY = (double) this.destination.getY();
		targetZ = (double) this.destination.getZ() + 0.5d;
	}

	// private double distance = 100;

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		if (entity.hasMoved()) {
			entity.resetMovedFlag();
			isAboveDestination = false;
			moving = false;
			return true;
		}
		return false;

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

		/*
		 * this.entity.getNavigator().tryMoveToXYZ((double) ((float)
		 * this.destination.getX()) + 0.5D, (double) (this.destination.getY()),
		 * (double) ((float) this.destination.getZ()) + 0.5D,
		 * this.movementSpeed); this.timeoutCounter = 0; this.field_179490_f =
		 * this.entity.getRNG().nextInt(this.entity.getRNG().nextInt(1200) +
		 * 1200) + 1200;
		 */
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

		updateDestination();

		if (destination == null) {
			isAboveDestination = true;
			timeoutCounter = 0;
			return;
		}

		double distance = distanceFromDestination();

		if (distance <= 0.02) {
			isAboveDestination = true;
			timeoutCounter = 0;
			return;
		}

		if (distance < 1) {
			entity.setPosition(targetX, entity.posY, targetZ);
			timeoutCounter = 0;
			isAboveDestination = true;
			return;
		}
		;

		timeoutCounter++;
		if (moving && timeoutCounter < 40) {
			return;
		}

		// double speed = Math.min(0.2 + distance * 0.05, 2);
		double speed = 0.5;

		moving = entity.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, speed);

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
	/*
	 * public boolean tryMoveToXYZ(double x, double y, double z, double speedIn)
	 * {
	 * 
	 * //PathEntity pathentity = entity.getNavigator().getPathToXYZ(x, y, z);
	 * 
	 * 
	 * PathEntity pathentity = entity.getNavigator().getPathToPos(x, y, z);
	 * 
	 * 
	 * return entity.getNavigator().setPath(pathentity, speedIn); }
	 */

}