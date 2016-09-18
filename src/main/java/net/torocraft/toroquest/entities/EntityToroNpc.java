package net.torocraft.toroquest.entities;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.torocraft.toroquest.civilization.CivilizationHandlers;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;

public class EntityToroNpc extends EntityCreature {

	private static final DataParameter<String> CIV = EntityDataManager.<String>createKey(EntityEnderman.class, DataSerializers.STRING);

	public EntityToroNpc(World worldIn, Civilization civ) {
		super(worldIn);
		this.experienceValue = 5;
		setCivilization(civ);
	}

	public static void registerFixesMonster(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, "Monster");
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	public void setCivilization(Civilization civ) {
		if (civ == null) {
			dataManager.set(CIV, "");
		} else {
			dataManager.set(CIV, civ.toString());
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CIV, "");



	}

	public Civilization getCivilization() {
		return enumCiv(dataManager.get(CIV));
	}

	private Civilization enumCiv(String s) {
		try {
			return Civilization.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		this.updateArmSwingProgress();
		float f = this.getBrightness(1.0F);

		if (f > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();

		pledgeAllegianceIfUnaffiliated();

	}

	private void pledgeAllegianceIfUnaffiliated() {
		if (worldObj.isRemote || worldObj.getTotalWorldTime() % 80L != 0L || getCivilization() != null) {
			return;
		}
		Civilization civ = CivilizationHandlers.getCivilizationAt(worldObj, (int) posX / 16, (int) posZ / 16);
		System.out.println("EntityToroNpc.entityInit(): pos[x" + posX + " z" + posZ + "] set civ to " + civ);
		setCivilization(civ);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_HOSTILE_SWIM;
	}

	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_HOSTILE_SPLASH;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_HOSTILE_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_HOSTILE_DEATH;
	}

	protected SoundEvent getFallSound(int heightIn) {
		return heightIn > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int knockback = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
			knockback += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean successfulAttack = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (successfulAttack) {
			handleSuccessfulAttack(entityIn, knockback);
		}

		return successfulAttack;
	}

	protected void handleSuccessfulAttack(Entity entityIn, int knockback) {
		if (knockback > 0 && entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).knockBack(this, (float) knockback * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
			this.motionX *= 0.6D;
			this.motionZ *= 0.6D;
		}

		int j = EnchantmentHelper.getFireAspectModifier(this);

		if (j > 0) {
			entityIn.setFire(j * 4);
		}

		if (entityIn instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityIn;
			ItemStack itemstack = this.getHeldItemMainhand();
			ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;

			if (itemstack != null && itemstack1 != null && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
				float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

				if (this.rand.nextFloat() < f1) {
					entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
					this.worldObj.setEntityState(entityplayer, (byte) 30);
				}
			}
		}

		this.applyEnchantments(this, entityIn);
	}

	public float getBlockPathWeight(BlockPos pos) {
		return 0.5F - this.worldObj.getLightBrightness(pos);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere() {
		// check if in civ?
		return super.getCanSpawnHere();
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

	}

	/**
	 * Entity won't drop items or experience points if this returns false
	 */
	protected boolean canDropLoot() {
		return true;
	}
}
