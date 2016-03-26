package net.torocraft.games.chess.pieces;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class Rook extends EntityVillager {

	public Rook(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {

	}
	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

}
