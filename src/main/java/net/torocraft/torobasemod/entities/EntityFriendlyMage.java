package net.torocraft.torobasemod.entities;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torobasemod.ToroBaseMod;

public class EntityFriendlyMage extends EntityCreature {

	public EntityFriendlyMage(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	public static String NAME = "friendly_mage";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityFriendlyMage.class, NAME, entityId, ToroBaseMod.instance, 60, 2, true);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	public void onLivingUpdate() {
		updateLogic();
		super.onLivingUpdate();
	}

	protected void updateLogic() {
		if (this.worldObj.isRemote) {
			return;
		}

		// this.worldObj.setEntityState(this, (byte) 15); //spawn particles

	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount / 2);
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		/*
		 * spawn particles if (id == 15) { for (int i = 0; i <
		 * this.rand.nextInt(35) + 10; ++i) {
		 * this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX
		 * + this.rand.nextGaussian() * 0.12999999523162842D,
		 * this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() *
		 * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() *
		 * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]); }
		 */
		super.handleStatusUpdate(id);
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected float applyPotionDamageCalculations(DamageSource source, float damage) {
		damage = super.applyPotionDamageCalculations(source, damage);
		/*
		 * if (source.getEntity() == this) { damage = 0.0F; }
		 * 
		 * if (source.isMagicDamage()) { damage = (float) ((double) damage *
		 * 0.15D); }
		 */

		return damage;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	protected void attackWithArrow(EntityLivingBase target) {

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

	private void attackWithForce(EntityLivingBase target) {

		double force = 10;

		double relX = target.posX - this.posX;
		double relZ = target.posZ - this.posZ;

		double mag = Math.sqrt((relX * relX) + (relZ * relZ));

		double forceX = force * (relX / mag);
		double forceZ = force * (relZ / mag);

		target.addVelocity(forceX, 1, forceZ);
		target.velocityChanged = true;

		this.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, 1.5F, 0.5F);

		// spawnParticles(forceX, 1, forceZ);
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}

		// Minecraft.getMinecraft().setIngameNotInFocus();

		if (source instanceof EntityDamageSourceIndirect) {
			redirectArrowAtAttacker(source);
			return true;
		}

		if (source.getEntity() != null) {
			// TODO: this is not working, replace with AI task?
			faceEntity(source.getEntity(), 360F, 360F);
		}

		if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
			source.getEntity().attackEntityFrom(source, amount);
			attackWithForce((EntityLivingBase) source.getEntity());
		}

		return true;
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
