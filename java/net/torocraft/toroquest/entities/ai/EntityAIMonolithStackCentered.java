package net.torocraft.toroquest.entities.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.torocraft.toroquest.entities.EntityMonolithEye;

public class EntityAIMonolithStackCentered extends EntityAIBase {

	private EntityMonolithEye entity;

	public EntityAIMonolithStackCentered(EntityMonolithEye entity) {
		super();
		this.entity = entity;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return entity.getDistanceSqToCenter(entity.pos) > 0.5;
	}

	@Override
	public boolean continueExecuting() {
		entity.setPosition(entity.pos.getX() + 0.5d, entity.pos.getY(), entity.pos.getZ() + 0.5d);
		return false;
	}

}
