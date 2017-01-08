package net.torocraft.toroquest.entities;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;

public class EntityToroNpc extends EntityCreature {

	private static final DataParameter<String> CIV = EntityDataManager.<String>createKey(EntityEnderman.class, DataSerializers.STRING);

	public EntityToroNpc(World worldIn, CivilizationType civ) {
		super(worldIn);
		this.experienceValue = 5;
		setCivilization(civ);
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		String civ = s(getCivilization());
		if (isSet(civ)) {
			compound.setString("civilization", s(getCivilization()));
		} else {
			compound.removeTag("civilization");
		}
	}

	private boolean isSet(String civ) {
		return civ != null && civ.length() > 0;
	}

	@Override
	public void setAttackTarget(EntityLivingBase e) {
		if (!isCivilizationAlly(e)) {
			super.setAttackTarget(e);
		}
	}

	public boolean isCivilizationAlly(EntityLivingBase e) {
		if (!(e instanceof EntityToroNpc)) {
			return false;
		}

		CivilizationType otherCiv = ((EntityToroNpc) e).getCivilization();

		if (otherCiv == null) {
			return false;
		}

		return otherCiv.equals(getCivilization());
	}

	private String s(CivilizationType civilization) {
		if (civilization == null) {
			return null;
		}
		return civilization.toString();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setCivilization(e(compound.getString("civilization")));
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	public void setCivilization(CivilizationType civ) {
		if (civ == null) {
			dataManager.set(CIV, "");
		} else {
			dataManager.set(CIV, civ.toString());
		}
		dataManager.setDirty(CIV);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CIV, "");
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	public CivilizationType getCivilization() {
		return enumCiv(dataManager.get(CIV));
	}

	private CivilizationType enumCiv(String s) {
		try {
			CivilizationType civ = CivilizationType.valueOf(s);
			return civ;
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

		if (world.getTotalWorldTime() % 80L != 0L) {
			return;
		}

		if (getCivilization() != null) {
			return;
		}

		if (world.isRemote) {
			return;
		}

		Province civ = CivilizationUtil.getProvinceAt(world, chunkCoordX, chunkCoordZ);

		if (civ == null || civ.civilization == null) {
			return;
		}

		setCivilization(civ.civilization);
		onPledgeAllegiance();
	}

	public void onPledgeAllegiance() {

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
					this.world.setEntityState(entityplayer, (byte) 30);
				}
			}
		}

		this.applyEnchantments(this, entityIn);
	}

	public float getBlockPathWeight(BlockPos pos) {
		return 0.5F - this.world.getLightBrightness(pos);
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
