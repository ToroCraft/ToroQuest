package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.config.ToroQuestConfiguration;
import net.torocraft.toroquest.entities.ai.EntityAIStayCentered;

public class EntityMonolithEye extends EntityMob implements IRangedAttackMob {

	private int clientSideAttackTime;
	private static final DataParameter<Byte> STATUS = EntityDataManager.<Byte> createKey(EntityMonolithEye.class, DataSerializers.BYTE);
	private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.<Integer> createKey(EntityMonolithEye.class,
			DataSerializers.VARINT);
	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean> createKey(EntityMonolithEye.class,
			DataSerializers.BOOLEAN);

	public static String NAME = "monolitheye";

	static {
		if (ToroQuestConfiguration.specificEntityNames) {
			NAME = ToroQuestEntities.ENTITY_PREFIX + NAME;
		}
	}

	private EntityLivingBase targetedEntity;
	private float field_175485_bl;
	private float field_175486_bm;
	private boolean isStunned;

	public BlockPos pos;

	public EntityMonolithEye(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);

	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);

		float damage = 6f;

		DamageSource ds = DamageSource.causeIndirectMagicDamage(this, this);

		entityIn.attackEntityFrom(ds, damage);
		entityIn.attackEntityFrom(DamageSource.causeMobDamage(this),
				(float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
		this.world.setEntityState(this, (byte) 15);
		this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));

	}

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityMonolithEye.class, NAME, entityId, ToroQuest.INSTANCE, 60,
				2, true);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	protected void initEntityAI() {
		tasks.addTask(1, new EntityAIStayCentered(this));
		tasks.addTask(4, new AIAttack(this));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));
		targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new MonolithEyeTargetSelector(this)));
	}

	static class MonolithEyeTargetSelector implements Predicate<EntityLivingBase> {
		private final EntityMonolithEye parentEntity;

		public MonolithEyeTargetSelector(EntityMonolithEye guardian) {
			this.parentEntity = guardian;
		}

		public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
			return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid)
					&& p_apply_1_.getDistanceSqToEntity(this.parentEntity) > 9.0D;
		}
	}

	static class AIAttack extends EntityAIBase {
		private final EntityMonolithEye theEntity;
		private int tickCounter;

		public AIAttack(EntityMonolithEye guardian) {
			this.theEntity = guardian;
			this.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
			return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting() {
			return super.continueExecuting() && (theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) > 9.0D);
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.tickCounter = -4;
			this.theEntity.getNavigator().clearPathEntity();
			this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 90.0F, 90.0F);
			this.theEntity.isAirBorne = true;
		}

		/**
		 * Resets the task
		 */
		public void resetTask() {
			this.theEntity.setTargetedEntity(0);
			this.theEntity.setAttackTarget((EntityLivingBase) null);
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
			this.theEntity.getNavigator().clearPathEntity();
			this.theEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);

			if (!this.theEntity.canEntityBeSeen(entitylivingbase)) {
				this.theEntity.setAttackTarget((EntityLivingBase) null);
			} else {
				++this.tickCounter;

				if (this.tickCounter == 0) {
					this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
					// this.theEntity.world.setEntityState(this.theEntity,
					// (byte) 21);
				} else if (this.tickCounter >= this.theEntity.getAttackDuration()) {
					float damage = 3.0F;

					if (this.theEntity.world.getDifficulty() == EnumDifficulty.HARD) {
						damage += 3.0F;
					}

					DamageSource ds = DamageSource.causeIndirectMagicDamage(this.theEntity, this.theEntity);

					entitylivingbase.attackEntityFrom(ds, damage);
					entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity),
							(float) this.theEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
					theEntity.attackedTarget = theEntity.getAttackTarget();
					theEntity.setAttackTarget((EntityLivingBase) null);
					theEntity.world.setEntityState(theEntity, (byte) 15);
					theEntity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F / (theEntity.getRNG().nextFloat() * 0.4F + 0.8F));
				}

				super.updateTask();
			}
		}
	}

	public EntityLivingBase attackedTarget;

	public int getAttackDuration() {
		return 10;
	}

	protected void entityInit() {
		super.entityInit();
		getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
		pos = getPosition();
		this.dataManager.register(STATUS, Byte.valueOf((byte) 0));
		this.dataManager.register(TARGET_ENTITY, Integer.valueOf(0));
	}

	private void setTargetedEntity(int entityId) {
		this.dataManager.set(TARGET_ENTITY, Integer.valueOf(entityId));
	}

	public boolean hasTargetedEntity() {
		return ((Integer) this.dataManager.get(TARGET_ENTITY)).intValue() != 0;
	}

	public EntityLivingBase getTargetedEntity() {
		if (!this.hasTargetedEntity()) {
			return null;
		} else if (this.world.isRemote) {
			if (this.targetedEntity != null) {
				return this.targetedEntity;
			} else {
				Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(TARGET_ENTITY)).intValue());

				if (entity instanceof EntityLivingBase) {
					this.targetedEntity = (EntityLivingBase) entity;
					return this.targetedEntity;
				} else {
					return null;
				}
			}
		} else {
			return this.getAttackTarget();
		}
	}
	/*
	 * public void notifyDataManagerChange(DataParameter<?> key) {
	 * super.notifyDataManagerChange(key);
	 * 
	 * if (STATUS.equals(key)) { if (this.isElder() && this.width < 1.0F) {
	 * this.setSize(1.9975F, 1.9975F); } } else if (TARGET_ENTITY.equals(key)) {
	 * this.clientSideAttackTime = 0; this.targetedEntity = null; }
	 */

	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		pos = getPosition();
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITCH_AMBIENT;
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_WITCH_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITCH_DEATH;
	}

	public void setAggressive(boolean aggressive) {
		this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
	}

	public void onLivingUpdate() {
		updateLogic();
		if (this.world.isRemote) {
			spawnAuraParticle();
		}

		super.onLivingUpdate();
	}

	protected void updateLogic() {
		if (this.world.isRemote) {
			return;
		}

		handleAttachLogicUpdate();

		if (this.rand.nextFloat() < 7.5E-4F) {
			this.world.setEntityState(this, (byte) 15);
		}

		if (world.getTotalWorldTime() % 10L == 0L) {
			BlockPos down = pos.down();
			if (world.getBlockState(down).getBlock() != Blocks.OBSIDIAN) {
				setHealth(0);
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public void spawnAuraParticle() {
		if (this.rand.nextDouble() <= 0.36D) {

			this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, this.posX + this.rand.nextGaussian() * 0.06999999523162842D,
					this.posY + 1.5D + this.rand.nextGaussian() * 0.20999999523162842D, this.posZ + this.rand.nextGaussian() * 0.06999999523162842D,
					0.0D, 0.0D, 0.0D, new int[0]);

		}

	}

	@SideOnly(Side.CLIENT)
	public void spawnAttackParticles() {
		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + 1.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	protected void handleAttachLogicUpdate() {

	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 15) {
			spawnAttackParticles();
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_GUARDIAN;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
	}

	private void spawnParticles(double xSpeed, double ySpeed, double zSpeed) {
		if (this.world.isRemote) {
			for (int i = 0; i < 32; ++i) {
				world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, xSpeed, ySpeed, zSpeed, new int[0]);
			}
		} else {
			this.world.setEntityState(this, (byte) 42);
		}

	}

	public float getEyeHeight() {
		return 1.62F;
	}

	private boolean hitByRangedAttack(DamageSource source) {
		return source instanceof EntityDamageSourceIndirect;
	}

	public boolean getIsStunned() {
		return this.isStunned;
	}

	public void setIsStunned(Boolean bool) {
		this.isStunned = bool;
	}

	public void moveEntityWithHeading(float strafe, float forward) {
		if (this.isInWater()) {
			this.moveRelative(strafe, forward, 0.02F);
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
		} else if (this.isInLava()) {
			this.moveRelative(strafe, forward, 0.02F);
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
		} else {
			float f = 0.91F;

			if (this.onGround) {
				f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1,
						MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
			}

			float f1 = 0.16277136F / (f * f * f);
			this.moveRelative(strafe, forward, this.onGround ? 0.1F * f1 : 0.02F);
			f = 0.91F;

			if (this.onGround) {
				f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1,
						MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
			}

			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double) f;
			this.motionY *= (double) f;
			this.motionZ *= (double) f;
		}

		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d1 = this.posX - this.prevPosX;
		double d0 = this.posZ - this.prevPosZ;
		float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}

		if (!world.isRemote) {
			if (source instanceof EntityDamageSourceIndirect) {
				redirectArrowAtAttacker(source);
			} else {
				redirectAttack(source, amount);
			}
		}

		return false;
	}

	protected void redirectAttack(DamageSource source, float amount) {
		Entity attacker = source.getEntity();
		if (attacker != null) {
			attacker.attackEntityFrom(source, amount);
		}
	}

	protected void redirectArrowAtAttacker(DamageSource source) {
		if ("arrow".equals(source.getDamageType())) {

			if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
				attackWithArrow((EntityLivingBase) source.getEntity());
			}

			if (source.getSourceOfDamage() != null) {
				source.getSourceOfDamage().setDead();
			}

		}
	}

	protected void attackWithArrow(EntityLivingBase target) {

		int charge = 2 + rand.nextInt(10);

		EntityArrow entityarrow = new EntityTippedArrow(this.world, this);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
				(float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
		int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
		int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
		entityarrow.setDamage((double) (charge * 2.0F) + this.rand.nextGaussian() * 0.25D
				+ (double) ((float) this.world.getDifficulty().getDifficultyId() * 0.11F));

		if (i > 0) {
			entityarrow.setDamage(entityarrow.getDamage() + (double) i * 0.5D + 0.5D);
		}

		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}

		if (rand.nextBoolean()) {
			entityarrow.setFire(100);
		}

		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}
}
