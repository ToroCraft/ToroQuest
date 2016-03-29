package net.torocraft.games.chess.pieces.enities;

public interface IChessPiece {
	
	public static enum Side {WHITE, BLACK};
	
	Side getSide();
	
	void setSide(Side side);
	
	
}
