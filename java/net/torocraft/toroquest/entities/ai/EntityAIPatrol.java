package net.torocraft.toroquest.entities.ai;

import net.minecraft.entity.ai.EntityAITarget;
import net.torocraft.toroquest.entities.EntitySentry;

public class EntityAIPatrol extends EntityAITarget {

	EntitySentry sentry;
	
	public EntityAIPatrol(EntitySentry sentry) {
		super(sentry, false, true);
		this.sentry = sentry;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void startExecuting() {
		super.startExecuting();
	}
}
