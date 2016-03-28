package net.torocraft.games.chess;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.games.checkerboard.CheckerBoard;
import net.torocraft.games.chess.pieces.IChessPiece.Side;
import net.torocraft.games.chess.pieces.enities.EntityBishop;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;
import net.torocraft.games.chess.pieces.enities.EntityKing;
import net.torocraft.games.chess.pieces.enities.EntityKnight;
import net.torocraft.games.chess.pieces.enities.EntityPawn;
import net.torocraft.games.chess.pieces.enities.EntityQueen;
import net.torocraft.games.chess.pieces.enities.EntityRook;

public class ChessGame {

	private CheckerBoard board;
	private final World world;
	private final BlockPos origin;
	private Random rand = new Random();

	public ChessGame(World world, BlockPos position) {
		this.board = new CheckerBoard(world, position);
		this.world = world;
		this.origin = position;
	}

	public CheckerBoard getBoard() {
		if(board == null){
			board = new CheckerBoard(world, origin);
		}
		return board;
	}
	
	public String getPositionName(BlockPos pos) {
		return getBoard().getPositionName(pos);
	}

	public BlockPos getPosition(String name) {
		return getBoard().getPosition(name);
	}

	public void generate() {
		getBoard().generate();

		int i = 0;
		addWand(i++);

		placePieces();

	}

	public void placePieces() {
		addWand(0);
		placeEntity(new EntityPawn(world), Side.WHITE, "a2");
		placeEntity(new EntityPawn(world), Side.WHITE, "b2");
		placeEntity(new EntityPawn(world), Side.WHITE, "c2");
		placeEntity(new EntityPawn(world), Side.WHITE, "d2");
		placeEntity(new EntityPawn(world), Side.WHITE, "e2");
		placeEntity(new EntityPawn(world), Side.WHITE, "f2");
		placeEntity(new EntityPawn(world), Side.WHITE, "g2");
		placeEntity(new EntityPawn(world), Side.WHITE, "h2");

		placeEntity(new EntityRook(world), Side.WHITE, "a1");
		placeEntity(new EntityKnight(world), Side.WHITE, "b1");
		placeEntity(new EntityBishop(world), Side.WHITE, "c1");
		placeEntity(new EntityKing(world), Side.WHITE, "d1");
		placeEntity(new EntityQueen(world), Side.WHITE, "e1");
		placeEntity(new EntityBishop(world), Side.WHITE, "f1");
		placeEntity(new EntityKnight(world), Side.WHITE, "g1");
		placeEntity(new EntityRook(world), Side.WHITE, "h1");

		placeEntity(new EntityPawn(world), Side.BLACK, "a7");
		placeEntity(new EntityPawn(world), Side.BLACK, "b7");
		placeEntity(new EntityPawn(world), Side.BLACK, "c7");
		placeEntity(new EntityPawn(world), Side.BLACK, "d7");
		placeEntity(new EntityPawn(world), Side.BLACK, "e7");
		placeEntity(new EntityPawn(world), Side.BLACK, "f7");
		placeEntity(new EntityPawn(world), Side.BLACK, "g7");
		placeEntity(new EntityPawn(world), Side.BLACK, "h7");

		placeEntity(new EntityRook(world), Side.BLACK, "a8");
		placeEntity(new EntityKnight(world), Side.BLACK, "b8");
		placeEntity(new EntityBishop(world), Side.BLACK, "c8");
		placeEntity(new EntityKing(world), Side.BLACK, "d8");
		placeEntity(new EntityQueen(world), Side.BLACK, "e8");
		placeEntity(new EntityBishop(world), Side.BLACK, "f8");
		placeEntity(new EntityKnight(world), Side.BLACK, "g8");
		placeEntity(new EntityRook(world), Side.BLACK, "h8");
	}

	private void addWand(int index) {
		ItemChessControlWand wand = new ItemChessControlWand();
		wand.setChessControlBlockPosition(origin);
		ItemStack stack = new ItemStack(wand, 1);
		getBoard().getWhiteChest().setInventorySlotContents(index, stack);
	}

	private void placeEntity(EntityChessPiece e, Side side, String position) {
		int x = board.getA1Position().getX() + rand.nextInt(8);
		int z = board.getA1Position().getZ() + rand.nextInt(8);
		
		e.setChessPosition(position);
		e.setPosition(x, origin.getY() + 2, z);
		e.setSide(side);
		world.spawnEntityInWorld(e);
	}

}
