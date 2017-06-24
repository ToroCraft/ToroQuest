package net.torocraft.toroquest.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStayCentered extends EntityAIBase {

	private EntityLivingBase entity;

	public EntityAIStayCentered(EntityLivingBase entity) {
		super();
		this.entity = entity;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return entity.getDistanceSqToCenter(entity.getPosition()) > 0.5;
	}

	@Override
	public boolean shouldContinueExecuting() {
		entity.setPosition(entity.getPosition().getX() + 0.5d, entity.getPosition().getY(), entity.getPosition().getZ() + 0.5d);
		return false;
	}

}
