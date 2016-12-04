package net.torocraft.toroquest.entities;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.torocraft.toroquest.entities.ai.EntityAIDefendAgainstMonsters;
import net.torocraft.toroquest.entities.ai.EntityAIPatrol;

public class EntitySentry extends EntityCreature implements IAnimals {

	public EntitySentry(World world) {
		super(world);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIMoveTowardsTarget(this, this.getBaseMoveSpeed() * 4.0D, 32.0F));
		this.tasks.addTask(2, new EntityAIPatrol(this));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, this.getBaseMoveSpeed() * 4.0D));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		
		this.targetTasks.addTask(1, new EntityAIDefendAgainstMonsters(this));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, 0, false, true, IMob.MOB_SELECTOR));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
	}
				
				
	public double getBaseMoveSpeed() {
		return this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
	}
}
