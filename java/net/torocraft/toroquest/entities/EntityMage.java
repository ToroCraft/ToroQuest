package net.torocraft.toroquest.entities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.config.ToroQuestConfiguration;

/*
 * sweet drops
 * lots of xp
 * spawn mites and mobs
 * show bottle if held
 */

public class EntityMage extends EntityMob implements IRangedAttackMob {

	public static Achievement MAGE_ACHIEVEMNT = new Achievement("mage", "mage", 0, 0, Items.DIAMOND_SWORD, null).registerStat();

	private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);

	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean> createKey(EntityWitch.class, DataSerializers.BOOLEAN);

	private int attackTimer;
	private int staffTimer;

	public static String NAME = "mage";

	static {
		if (ToroQuestConfiguration.specificEntityNames) {
			NAME = ToroQuestEntities.ENTITY_PREFIX + NAME;
		}
	}

	private static final DataParameter<Boolean> STAFF_ATTACK = EntityDataManager.<Boolean> createKey(EntityMage.class, DataSerializers.BOOLEAN);

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityMage.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2,
				true, 0xff3024, 0xe0d6b9);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return null;
	}

	public EntityMage(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		this.experienceValue = 50;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			dropLoot();
			achievement(cause);
		}
	}

	protected void achievement(DamageSource cause) {
		if (world.isRemote) {
			return;
		}
		Entity entity = cause.getEntity();
		if (entity != null && entity instanceof EntityPlayer) {
			((EntityPlayer) entity).addStat(MAGE_ACHIEVEMNT);
		}
	}

	private void dropLoot() {
		dropLootItem(Items.SUGAR, rand.nextInt(20));
		dropLootItem(Items.GUNPOWDER, rand.nextInt(20));
		dropLootItem(Items.GLASS_BOTTLE, rand.nextInt(20));
		dropLootItem(Items.EMERALD, rand.nextInt(10));
		dropLootItem(Items.GLOWSTONE_DUST, rand.nextInt(20));
		dropLootItem(Items.REDSTONE, rand.nextInt(20));
		dropLootItem(Items.NETHER_WART, rand.nextInt(20));
		dropLootItem(Items.STICK, rand.nextInt(10));
		dropLootItem(Items.BONE, rand.nextInt(15));
	}

	private void dropLootItem(Item item, int amount) {
		if (amount == 0) {
			return;
		}

		for (int i = 0; i < amount; i++) {
			ItemStack stack = new ItemStack(item);
			EntityItem dropItem = new EntityItem(world, posX, posY, posZ, stack.copy());
			dropItem.setNoPickupDelay();
			dropItem.motionY = rand.nextDouble();
			dropItem.motionZ = rand.nextDouble() - 0.5d;
			dropItem.motionX = rand.nextDouble() - 0.5d;
			world.spawnEntity(dropItem);
		}

	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	protected void initEntityAI() {
		ai();
	}

	protected void ai() {
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 16, 10.0F));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void entityInit() {
		super.entityInit();
		getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
		dataManager.register(STAFF_ATTACK, Boolean.valueOf(false));
		isImmuneToFire = true;
	}

	public boolean isStaffAttacking() {
		return ((Boolean) this.dataManager.get(STAFF_ATTACK)).booleanValue();
	}

	private void setStaffAttacking(boolean b) {
		if (b) {
			staffTimer = 1;
		} else {
			staffTimer = 0;
		}
		dataManager.set(STAFF_ATTACK, b);
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
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
		if (this.world.isRemote) {
			return;
		}

		if (isDrinkingPotion()) {
			handleDrinkingPotionUpdate();
		} else {
			handleAttackLogicUpdate();
		}

		if (this.rand.nextFloat() < 7.5E-4F) {
			this.world.setEntityState(this, (byte) 15);
		}

		if (staffTimer > 0) {
			staffTimer++;
			if (staffTimer > 20) {
				setStaffAttacking(false);
			}
		}

	}

	protected void handleAttackLogicUpdate() {
		PotionType potiontype = null;

		if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
			potiontype = PotionTypes.WATER_BREATHING;
		} else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
			potiontype = PotionTypes.FIRE_RESISTANCE;
		} else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
			potiontype = PotionTypes.HEALING;
		} else if (this.rand.nextFloat() < 0.5F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED)
				&& this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
			potiontype = PotionTypes.SWIFTNESS;
		}

		if (potiontype != null) {
			this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F,
					0.8F + this.rand.nextFloat() * 0.4F);
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
			this.attackTimer = 10;
			this.setAggressive(true);
			IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			iattributeinstance.removeModifier(MODIFIER);
			iattributeinstance.applyModifier(MODIFIER);
		}
	}

	protected void handleDrinkingPotionUpdate() {
		if (this.attackTimer-- <= 0) {
			this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F,
					0.8F + this.rand.nextFloat() * 0.4F);
			this.setAggressive(false);
			ItemStack itemstack = this.getHeldItemOffhand();
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);

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
			spawnWitchParticles();
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@SideOnly(Side.CLIENT)
	protected void spawnWitchParticles() {
		for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
			this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D,
					this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D,
					this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
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

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {

		if (isDrinkingPotion()) {
			return;
		}

		int roll = rand.nextInt(100);

		setStaffAttacking(true);

		if (roll < 60) {
			attackWithPotion(target);

		} else if (roll < 90) {

			if (getDistanceSqToEntity(target) > 16 && rand.nextInt(100) > 60) {
				world.addWeatherEffect(new EntityLightningBolt(world, target.posX, target.posY, target.posZ, false));
			} else {
				attackWithForce(target);
			}

		} else {
			attackWithMobSpawn(target);
		}
	}

	private void attackWithMobSpawn(EntityLivingBase target) {

		int roll = rand.nextInt(100);

		Entity mob = null;

		if (roll < 45) {
			mob = new EntitySkeleton(world);
		} else if (roll < 90) {
			mob = new EntityZombie(world);
		} else {
			mob = new EntityWitch(world);
		}

		mob.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET, 1));

		mob.setPosition(posX, posY, posZ);
		world.spawnEntity(mob);
	}

	private void attackWithForce(EntityLivingBase target) {
		setStaffAttacking(true);

		double force = 8;

		double relX = target.posX - this.posX;
		double relZ = target.posZ - this.posZ;

		double mag = Math.sqrt((relX * relX) + (relZ * relZ));

		double forceX = force * (relX / mag);
		double forceZ = force * (relZ / mag);

		target.addVelocity(forceX, 1, forceZ);
		target.velocityChanged = true;

		this.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 1.0F, 0.5F);
	}

	private void spawnParticles(double xSpeed, double ySpeed, double zSpeed) {

		// TODO figure out how to spawn particles

		if (this.world.isRemote) {
			for (int i = 0; i < 32; ++i) {
				world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, xSpeed, ySpeed, zSpeed, new int[0]);
			}
		} else {
			this.world.setEntityState(this, (byte) 42);
		}

	}

	protected void attackWithPotion(EntityLivingBase target) {
		double targetY = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
		double targetX = target.posX + target.motionX - this.posX;
		double d2 = targetY - this.posY;
		double targetZ = target.posZ + target.motionZ - this.posZ;

		float f = MathHelper.sqrt(targetX * targetX + targetZ * targetZ);
		PotionType potiontype = PotionTypes.HARMING;

		if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS)) {
			potiontype = PotionTypes.SLOWNESS;
		} else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON)) {
			potiontype = PotionTypes.POISON;
		} else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
			potiontype = PotionTypes.WEAKNESS;
		}

		EntityPotion entitypotion = new EntityPotion(this.world, this,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		entitypotion.setThrowableHeading(targetX, d2 + (double) (f * 0.2F), targetZ, 0.75F, 8.0F);

		this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F,
				0.8F + this.rand.nextFloat() * 0.4F);
		this.world.spawnEntity(entitypotion);
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
		} else {
			amount = redirectAttack(source, amount);
		}

		return super.attackEntityFrom(source, amount);
	}

	protected float redirectAttack(DamageSource source, float amount) {
		float reflectFactor = rand.nextFloat();

		float reflectAmount = amount * reflectFactor;
		float passAmount = amount - reflectAmount;

		Entity attacker = source.getEntity();
		if (attacker != null) {
			attacker.attackEntityFrom(source, reflectAmount);
		}
		this.world.setEntityState(this, (byte) 15);
		return passAmount;
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
}
