package net.torocraft.games.chess.pieces;

public interface IChessPiece {
	
	public static enum Side {WHITE, BLACK};
	
	Side getSide();
	
	void setSide(Side side);
	
	
}
