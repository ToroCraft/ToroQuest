package net.torocraft.toroquest.entities;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.entities.ai.EntityAIMoveIntoArea;
import net.torocraft.toroquest.entities.ai.EntityAINearestAttackableCivTarget;
import net.torocraft.toroquest.entities.render.RenderGuard;

public class EntityGuard extends EntityToroNpc {

	/** The width of the entity */
	private float zombieWidth = -1.0F;
	/** The height of the the entity. */
	private float zombieHeight;

	/**
	 * animation counters
	 */
	public float capeAni = 0;
	public boolean capeAniUp = true;

	public static String NAME = "guard";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityGuard.class, NAME, entityId, ToroQuest.INSTANCE, 80, 2, true, 0x3f3024, 0xe0d6b9);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityGuard.class, new IRenderFactory<EntityGuard>() {
			@Override
			public Render<EntityGuard> createRenderFor(RenderManager manager) {
				return new RenderGuard(manager);
			}
		});
	}

	public EntityGuard(World worldIn) {
		this(worldIn, null);
	}

	public EntityGuard(World worldIn, CivilizationType civ) {
		super(worldIn, civ);
		setSize(0.6F, 1.95F);
		Arrays.fill(inventoryArmorDropChances, 0.25F);
		Arrays.fill(inventoryHandsDropChances, 0.25F);
	}

	private EntityAIMoveIntoArea areaAI;

	protected void initEntityAI() {
		areaAI = new EntityAIMoveIntoArea(this, 0.5D, 30);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackMelee(this, 0.6D, false));
		tasks.addTask(4, areaAI);
		tasks.addTask(7, new EntityAIWander(this, 0.35D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));

		targetTasks.addTask(2, new EntityAINearestAttackableCivTarget(this));
		targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityMob.class, 10, false, false, new Predicate<EntityMob>() {
			@Override
			public boolean apply(EntityMob target) {
				return !(target instanceof EntityCreeper);
			}
		}));
	}

	@Override
	public void setCivilization(CivilizationType civ) {
		super.setCivilization(civ);

		if (areaAI != null) {
			Province border = CivilizationUtil.getProvinceAt(worldObj, (int) posX / 16, (int) posZ / 16);
			if (border != null) {
				areaAI.setCenter(border.chunkX * 16, border.chunkZ * 16);
			}
		}

	}

	protected void entityInit() {

		super.entityInit();

	}

	public void onLivingUpdate() {

		super.onLivingUpdate();
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!super.attackEntityFrom(source, amount)) {
			return false;
		}

		EntityLivingBase attacker = this.getAttackTarget();
		if (attacker == null && source.getEntity() instanceof EntityLivingBase) {
			setAttackTarget((EntityLivingBase) source.getEntity());
		}
		return true;
	}

	public void onUpdate() {
		super.onUpdate();
	}

	public void onUpdate_OFF() {
		super.onUpdate();

		// this.renderOffsetY = 0.0F;
		super.onUpdate();
		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d0 = this.posX - this.prevPosX;
		double d1 = this.posZ - this.prevPosZ;
		float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if (f > 1.0F) {
			f = 1.0F;
		}

		this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	public boolean attackEntityAsMob(Entity victum) {
		attackTargetEntityWithCurrentItem(victum);
		return true;
	}

	public void attackTargetEntityWithCurrentItem(Entity targetEntity) {

		if (targetEntity.canBeAttackedWithItem()) {
			if (!targetEntity.hitByEntity(this)) {
				float attackDamage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float modifierForCreature;

				if (targetEntity instanceof EntityLivingBase) {
					modifierForCreature = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
				} else {
					modifierForCreature = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
				}

				if (attackDamage > 0.0F || modifierForCreature > 0.0F) {

					int i = 0;
					i = i + EnchantmentHelper.getKnockbackModifier(this);

					boolean criticalHit = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && targetEntity instanceof EntityLivingBase;
					criticalHit = criticalHit && !this.isSprinting();

					if (criticalHit) {
						attackDamage *= 1.5F;
					}

					attackDamage = attackDamage + modifierForCreature;
					boolean swordSweep = false;
					double d0 = (double) (this.distanceWalkedModified - this.prevDistanceWalkedModified);

					if (!criticalHit && this.onGround && d0 < (double) this.getAIMoveSpeed()) {
						ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);

						if (itemstack != null && itemstack.getItem() instanceof ItemSword) {
							swordSweep = true;
						}
					}

					float targetHealth = 0.0F;
					boolean setFireToTarget = false;
					int fireAspectModiferOfGuard = EnchantmentHelper.getFireAspectModifier(this);

					if (targetEntity instanceof EntityLivingBase) {
						targetHealth = ((EntityLivingBase) targetEntity).getHealth();

						if (fireAspectModiferOfGuard > 0 && !targetEntity.isBurning()) {
							setFireToTarget = true;
							targetEntity.setFire(1);
						}
					}

					double targetMotionX = targetEntity.motionX;
					double targetMotionY = targetEntity.motionY;
					double targetMotionZ = targetEntity.motionZ;

					boolean successfulAttack = targetEntity.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);

					if (successfulAttack) {
						if (i > 0) {
							if (targetEntity instanceof EntityLivingBase) {
								((EntityLivingBase) targetEntity).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
							} else {
								targetEntity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * (float) i * 0.5F));
							}

							this.motionX *= 0.6D;
							this.motionZ *= 0.6D;
							this.setSprinting(false);
						}

						if (swordSweep) {
							for (EntityLivingBase entitylivingbase : this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
								if (entitylivingbase != this && entitylivingbase != targetEntity && !this.isOnSameTeam(entitylivingbase) && this.getDistanceSqToEntity(entitylivingbase) < 9.0D) {
									entitylivingbase.knockBack(this, 0.4F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this), 1.0F);
								}
							}

							worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
							this.spawnSweepParticles();
						}

						if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
							((EntityPlayerMP) targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
							targetEntity.velocityChanged = false;
							targetEntity.motionX = targetMotionX;
							targetEntity.motionY = targetMotionY;
							targetEntity.motionZ = targetMotionZ;
						}

						if (criticalHit) {
							this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
							this.onCriticalHit(targetEntity);
						}

						if (!criticalHit && !swordSweep) {
							this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
						}

						if (modifierForCreature > 0.0F) {
							this.onEnchantmentCritical(targetEntity);
						}

						if (!worldObj.isRemote && targetEntity instanceof EntityPlayer) {
							EntityPlayer entityplayer = (EntityPlayer) targetEntity;
							ItemStack itemstack2 = this.getHeldItemMainhand();
							ItemStack itemstack3 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;

							if (itemstack2 != null && itemstack3 != null && itemstack2.getItem() instanceof ItemAxe && itemstack3.getItem() == Items.SHIELD) {
								float f3 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
								if (this.rand.nextFloat() < f3) {
									entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
									this.worldObj.setEntityState(entityplayer, (byte) 30);
								}
							}
						}

						this.setLastAttacker(targetEntity);

						if (targetEntity instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, this);
						}

						EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
						ItemStack mainhandItem = this.getHeldItemMainhand();
						Entity entity = targetEntity;

						if (targetEntity instanceof EntityDragonPart) {
							IEntityMultiPart ientitymultipart = ((EntityDragonPart) targetEntity).entityDragonObj;

							if (ientitymultipart instanceof EntityLivingBase) {
								entity = (EntityLivingBase) ientitymultipart;
							}
						}

						if (mainhandItem != null && entity instanceof EntityLivingBase) {
							mainhandItem.getItem().hitEntity(mainhandItem, (EntityLivingBase) entity, this);

							if (mainhandItem.stackSize <= 0) {
								this.setHeldItem(EnumHand.MAIN_HAND, (ItemStack) null);
							}
						}

						if (targetEntity instanceof EntityLivingBase) {
							float damageDealt = targetHealth - ((EntityLivingBase) targetEntity).getHealth();

							if (fireAspectModiferOfGuard > 0) {
								targetEntity.setFire(fireAspectModiferOfGuard * 4);
							}

							if (worldObj instanceof WorldServer && damageDealt > 2.0F) {
								int k = (int) ((double) damageDealt * 0.5D);
								((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double) (targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D,
										new int[0]);
							}
						}

					} else {
						this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);

						if (setFireToTarget) {
							targetEntity.extinguish();
						}
					}
				}
			}
		}
	}

	public void onCriticalHit(Entity entityHit) {
	}

	public void onEnchantmentCritical(Entity entityHit) {
	}

	public void spawnSweepParticles() {
		double d0 = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F));
		double d1 = (double) MathHelper.cos(this.rotationYaw * 0.017453292F);

		if (this.worldObj instanceof WorldServer) {
			((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + (double) this.height * 0.5D, this.posZ + d1, 0, d0, 0.0D, d1, 0.0D, new int[0]);
		}
	}

	/**
	 * Called only once on an entity when first time spawned, via egg, mob
	 * spawner, natural spawning etc, but not called when entity is reloaded
	 * from nbt. Mainly used for initializing attributes and inventory
	 */
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		setCanPickUpLoot(true);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);

		setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD, 1));
		setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.SHIELD, 1));

		// addArmor();

		return livingdata;
	}

	protected void addArmor() {
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET, 1));
		setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS, 1));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS, 1));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE, 1));
	}

	protected final void setSize(float width, float height) {
		boolean flag = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
		this.zombieWidth = width;
		this.zombieHeight = height;

		if (!flag) {
			this.multiplySize(1.0F);
		}
	}

	/**
	 * Multiplies the height and width by the provided float.
	 */
	protected final void multiplySize(float size) {
		super.setSize(this.zombieWidth * size, this.zombieHeight * size);
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset() {
		return this.isChild() ? 0.0D : -0.35D;
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
	}
}
