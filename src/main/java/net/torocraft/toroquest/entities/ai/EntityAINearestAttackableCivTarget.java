package net.torocraft.toroquest.entities.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
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

	private final int targetChance;
	protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	protected final Predicate<? super EntityPlayer> targetEntitySelector;
	protected EntityLivingBase targetEntity;

	protected EntityToroNpc taskOwner;

	public EntityAINearestAttackableCivTarget(EntityToroNpc npc) {
		super(npc, false, false);

		this.taskOwner = npc;
		this.targetChance = 10;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(npc);
		this.setMutexBits(1);
		
		final Predicate<? super EntityLivingBase> targetSelector = (Predicate<? super EntityLivingBase>) null;
		
		this.targetEntitySelector = new Predicate<EntityLivingBase>() {
			public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
				return p_apply_1_ == null ? false
						: (targetSelector != null && !targetSelector.apply(p_apply_1_) ? false : (!EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) ? false : EntityAINearestAttackableCivTarget.this.isSuitableTarget(p_apply_1_, false)));
			}
		};
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (noTargetChance() || taskOwner.getCivilization() == null) {
			return false;
		}

		if (shouldExecuteNonPlayer()) {
			System.out.println("EntityAINearestAttackableCivTarget: atttacking player: " + targetEntity.getName());
			return true;
		}

		if (shouldExecutePlayer()) {
			System.out.println("EntityAINearestAttackableCivTarget: atttacking NON player: " + targetEntity.getName());
			return true;
		}

		return false;
	}

	protected boolean noTargetChance() {
		return this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0;
	}

	protected boolean shouldExecutePlayer() {
		this.targetEntity = taskOwner.worldObj.getNearestAttackablePlayer(taskOwner.posX, taskOwner.posY + (double) taskOwner.getEyeHeight(), taskOwner.posZ, this.getTargetDistance(), getTargetDistance(), createFindPlayerFunction(),
				(Predicate<EntityPlayer>) targetEntitySelector);
		return this.targetEntity != null;
	}

	protected boolean shouldExecuteNonPlayer() {
		List<EntityToroNpc> list = taskOwner.worldObj.<EntityToroNpc>getEntitiesWithinAABB(EntityToroNpc.class, getTargetableArea(getTargetDistance()));

		if (list.isEmpty()) {
			return false;
		} else {
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			
			Civilization ownerCiv = taskOwner.getCivilization();

			for(EntityToroNpc npc : list){
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

	protected Function<EntityPlayer, Double> createFindPlayerFunction() {
		return new Function<EntityPlayer, Double>() {
			@Nullable
			public Double apply(@Nullable EntityPlayer player) {

				if (!(EntityAINearestAttackableCivTarget.this.taskOwner instanceof EntityToroNpc)) {
					return 0d;
				}

				EntityToroNpc npc = (EntityToroNpc) EntityAINearestAttackableCivTarget.this.taskOwner;

				Civilization civ = npc.getCivilization();

				if (civ == null) {
					return 0d;
				}

				int rep = CivilizationHandlers.getPlayerRep(player, civ);

				if (rep < -5) {
					return 0.25d;
				} else if (rep < -10) {
					return 0.5d;
				} else if (rep < -20) {
					return 1d;
				} else {
					return 0d;
				}

			}
		};
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