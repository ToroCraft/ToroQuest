package net.torocraft.games.chess;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.games.checkerboard.CheckerBoard;
import net.torocraft.games.chess.pieces.Rook;

public class ChessGame {

	protected final CheckerBoard board;
	private final World world;
	private final BlockPos origin;

	private double x;
	private double y;
	private double z;

	public ChessGame(World world, BlockPos position) {
		this.board = new CheckerBoard(world, position);
		this.world = world;
		this.origin = position;
	}

	public void generate() {
		board.generate();
		y = 0;

		x = -3.5;
		z = -3.5;

		for (int i = 0; i < 8; i++) {
			placeEntity(new Rook(world));
			x++;
		}
		
		x += 2;
		
		placeEntity(new EntityVillager(world));
		
		/*
		 * EntityLiving e1 = new EntityVillager(world);
		 * e1.setPosition((double)position.getX() + 1.5, (double)position.getY()
		 * + 4, (double)position.getZ() + 1.5); world.spawnEntityInWorld(e1);
		 */

	}

	private void placeEntity(EntityLiving e) {
		e.setPosition((double) origin.getX() + x, (double) origin.getY() + y, (double) origin.getZ() + z);
		world.spawnEntityInWorld(e);
	}

}
