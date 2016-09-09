package net.torocraft.toroquest.entities.ai;

import net.minecraft.entity.ai.EntityAITarget;
import net.torocraft.toroquest.entities.EntitySentry;

public class EntityAIDefendAgainstMonsters extends EntityAITarget {

	EntitySentry sentry;
	
	public EntityAIDefendAgainstMonsters(EntitySentry sentry) {
		super(sentry, false, true);
		this.sentry = sentry;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		
		return false;
	}
	
	public void startExecuting() {
		
		super.startExecuting();
	}
}
