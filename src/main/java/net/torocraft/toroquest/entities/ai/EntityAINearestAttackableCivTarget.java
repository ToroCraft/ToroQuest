package net.torocraft.toroquest.entities.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.torocraft.toroquest.civilization.CivilizationHandlers;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;
import net.torocraft.toroquest.entities.EntityToroNpc;

public class EntityAINearestAttackableCivTarget extends EntityAITarget {

	protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	protected final Predicate<EntityPlayer> targetEntitySelector;
	protected EntityLivingBase targetEntity;

	protected EntityToroNpc taskOwner;

	public EntityAINearestAttackableCivTarget(EntityToroNpc npc) {
		super(npc, false, false);

		// (Predicate<EntityPlayer>)

		this.taskOwner = npc;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(npc);
		this.setMutexBits(1);

		final Predicate<? super EntityLivingBase> targetSelector = (Predicate<? super EntityLivingBase>) null;

		this.targetEntitySelector = new Predicate<EntityPlayer>() {
			public boolean apply(@Nullable EntityPlayer target) {

				if (target == null) {
					return false;
				}

				if (!EntitySelectors.NOT_SPECTATING.apply(target)) {
					return false;
				}

				if (!isSuitableTarget(taskOwner, target, false, true)) {
					return false;
				}

				return shouldAttackPlayerBasedOnCivilization(target);
			}
		};
	}

	protected boolean shouldAttackPlayerBasedOnCivilization(EntityPlayer target) {
		if (!(EntityAINearestAttackableCivTarget.this.taskOwner instanceof EntityToroNpc)) {
			return false;
		}

		if (!(target instanceof EntityPlayer)) {
			return false;
		}

		EntityToroNpc npc = (EntityToroNpc) EntityAINearestAttackableCivTarget.this.taskOwner;

		Civilization civ = npc.getCivilization();

		if (civ == null) {
			return false;
		}

		int rep = CivilizationHandlers.getPlayerRep(target, civ);

		return rep < -10;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (taskOwner.getCivilization() == null) {
			return false;
		}

		/*
		 * if (shouldExecuteNonPlayer()) { System.out.println(
		 * "EntityAINearestAttackableCivTarget: atttacking player: " +
		 * targetEntity.getName()); return true; }
		 */

		if (shouldExecutePlayer()) {
			System.out.println("EntityAINearestAttackableCivTarget: atttacking player: " + targetEntity.getName());
			return true;
		}

		return false;
	}

	protected boolean shouldExecutePlayer() {

		double maxXZDistance = getTargetDistance();
		double maxYDistance = getTargetDistance();

		targetEntity = taskOwner.worldObj.getNearestAttackablePlayer(taskOwner.posX, taskOwner.posY + (double) taskOwner.getEyeHeight(), taskOwner.posZ, maxXZDistance, maxYDistance, null, targetEntitySelector);

		return targetEntity != null;
	}

	protected boolean shouldExecuteNonPlayer() {
		List<EntityToroNpc> list = taskOwner.worldObj.<EntityToroNpc>getEntitiesWithinAABB(EntityToroNpc.class, getTargetableArea(getTargetDistance()));

		if (list.isEmpty()) {
			return false;
		} else {
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			Civilization ownerCiv = taskOwner.getCivilization();

			for (EntityToroNpc npc : list) {
				Civilization npcCiv = npc.getCivilization();
				if (npcCiv != null && !npcCiv.equals(ownerCiv)) {
					targetEntity = npc;
					System.out.println("EntityAINearestAttackableCivTarget: attacking npc: " + targetEntity.getName());
					return true;
				}
			}
			return false;
		}
	}

	protected AxisAlignedBB getTargetableArea(double targetDistance) {
		return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	public static class Sorter implements Comparator<Entity> {
		private final Entity theEntity;

		public Sorter(Entity theEntityIn) {
			this.theEntity = theEntityIn;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}
	}
}