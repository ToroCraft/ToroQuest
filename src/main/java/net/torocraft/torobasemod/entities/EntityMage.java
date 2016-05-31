package net.torocraft.torobasemod.entities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMage extends EntityMob implements IRangedAttackMob {

	private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);

	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityWitch.class, DataSerializers.BOOLEAN);

	private int attackTimer;

	public EntityMage(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);

	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 30, 10.0F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
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

	/**
	 * Set whether this witch is aggressive at an entity.
	 */
	public void setAggressive(boolean aggressive) {
		this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
	}

	public boolean isDrinkingPotion() {
		return ((Boolean) this.getDataManager().get(IS_AGGRESSIVE)).booleanValue();
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		updateLogic();

		super.onLivingUpdate();
	}

	protected void updateLogic() {
		if (this.worldObj.isRemote) {
			return;
		}

		if (isDrinkingPotion()) {
			handleDrinkingPotionUpdate();
		} else {
			handleAttachLogicUpdate();
		}

		if (this.rand.nextFloat() < 7.5E-4F) {
			this.worldObj.setEntityState(this, (byte) 15);
		}

	}

	protected void handleAttachLogicUpdate() {
		PotionType potiontype = null;

		if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
			potiontype = PotionTypes.WATER_BREATHING;
		} else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
			potiontype = PotionTypes.FIRE_RESISTANCE;
		} else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
			potiontype = PotionTypes.HEALING;
		} else if (this.rand.nextFloat() < 0.5F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
			potiontype = PotionTypes.SWIFTNESS;
		}

		if (potiontype != null) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
			this.attackTimer = 8;
			this.setAggressive(true);
			this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
			IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			iattributeinstance.removeModifier(MODIFIER);
			iattributeinstance.applyModifier(MODIFIER);
		}
	}

	protected void handleDrinkingPotionUpdate() {
		if (this.attackTimer-- <= 0) {
			this.setAggressive(false);
			ItemStack itemstack = this.getHeldItemMainhand();
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);

			if (itemstack != null && itemstack.getItem() == Items.POTIONITEM) {
				List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);

				if (list != null) {
					for (PotionEffect potioneffect : list) {
						this.addPotionEffect(new PotionEffect(potioneffect));
					}
				}
			}

			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 15) {
			for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D,
						this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected float applyPotionDamageCalculations(DamageSource source, float damage) {
		damage = super.applyPotionDamageCalculations(source, damage);

		if (source.getEntity() == this) {
			damage = 0.0F;
		}

		if (source.isMagicDamage()) {
			damage = (float) ((double) damage * 0.15D);
		}

		return damage;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
		if (this.isDrinkingPotion()) {
			return;
		}
		if (rand.nextInt(10) > 6) {
			attackWithPotion(target);
		} else {
			attackWithForce(target);
		}
	}

	private void attackWithForce(EntityLivingBase target) {

		double force = 10;

		double relX = target.posX - this.posX;
		double relZ = target.posZ - this.posZ;

		double mag = Math.sqrt((relX * relX) + (relZ * relZ));

		double forceX = force * (relX / mag);
		double forceZ = force * (relZ / mag);

		target.addVelocity(forceX, 1, forceZ);
		target.velocityChanged = true;

		this.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 1.0F, 0.5F);

		// spawnParticles(forceX, 1, forceZ);
	}

	private void spawnParticles(double xSpeed, double ySpeed, double zSpeed) {

		// TODO figure out how to spawn particles

		if (this.worldObj.isRemote) {
			for (int i = 0; i < 32; ++i) {
				worldObj.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, xSpeed, ySpeed, zSpeed, new int[0]);
			}
		} else {
			this.worldObj.setEntityState(this, (byte) 42);
		}

	}

	protected void attackWithPotion(EntityLivingBase target) {
		double targetY = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
		double targetX = target.posX + target.motionX - this.posX;
		double d2 = targetY - this.posY;
		double targetZ = target.posZ + target.motionZ - this.posZ;

		float f = MathHelper.sqrt_double(targetX * targetX + targetZ * targetZ);
		PotionType potiontype = PotionTypes.HARMING;

		if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS)) {
			potiontype = PotionTypes.SLOWNESS;
		} else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON)) {
			potiontype = PotionTypes.POISON;
		} else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
			potiontype = PotionTypes.WEAKNESS;
		}

		EntityPotion entitypotion = new EntityPotion(this.worldObj, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		entitypotion.setThrowableHeading(targetX, d2 + (double) (f * 0.2F), targetZ, 0.75F, 8.0F);

		this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
		this.worldObj.spawnEntityInWorld(entitypotion);
	}

	protected void attachWithArrow(EntityLivingBase target) {

		int charge = 2 + rand.nextInt(10);

		EntityArrow entityarrow = new EntityTippedArrow(this.worldObj, this);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
		int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
		int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
		entityarrow.setDamage((double) (charge * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.getDifficulty().getDifficultyId() * 0.11F));

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
		this.worldObj.spawnEntityInWorld(entityarrow);
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}

		if (source instanceof EntityDamageSourceIndirect) {
			redirectArrowAtAttacker(source);
			return false;
		}

		return super.attackEntityFrom(source, amount);
	}

	protected void redirectArrowAtAttacker(DamageSource source) {
		if ("arrow".equals(source.getDamageType())) {

			if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
				attachWithArrow((EntityLivingBase) source.getEntity());
			}

			if (source.getSourceOfDamage() != null) {
				source.getSourceOfDamage().setDead();
			}

		}
	}
}
