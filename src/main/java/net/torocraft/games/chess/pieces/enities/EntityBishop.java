package net.torocraft.games.chess.pieces.enities;

import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.torocraft.games.chess.ai.EntityAILookDownBoard;

public class EntityBishop extends EntityChessPiece implements IChessPiece {

	public EntityBishop(World worldIn) {
		super(worldIn);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

}
