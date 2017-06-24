package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.config.ToroQuestConfiguration;
import net.torocraft.toroquest.entities.model.ModelToro;
import net.torocraft.toroquest.entities.render.RenderToro;
import net.torocraft.toroquest.item.ItemToroLeather;

public class EntityToro extends EntityTameable {

	public static String NAME = "toro";

	static {
		if (ToroQuestConfiguration.specificEntityNames) {
			NAME = ToroQuestEntities.ENTITY_PREFIX + NAME;
		}
	}

	private static final DataParameter<Boolean> CHARGING = EntityDataManager.<Boolean> createKey(EntityToro.class, DataSerializers.BOOLEAN);

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityToro.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2,
				true, 0x3f3024, 0xe0d6b9);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityToro.class, new IRenderFactory<EntityToro>() {
			@Override
			public Render<EntityToro> createRenderFor(RenderManager manager) {
				return new RenderToro(manager, new ModelToro(), 0.7F);
			}
		});
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CHARGING, Boolean.valueOf(false));
	}

	private void setCharging(boolean b) {
		dataManager.set(CHARGING, b);
	}

	public boolean isCharging() {
		return ((Boolean) this.dataManager.get(CHARGING)).booleanValue();
	}

	public EntityToro(World worldIn) {
		super(worldIn);
		setSize(0.7F, 1.8F);
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.75, false));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));

	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D * ToroQuestConfiguration.toroHealthMultiplier);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D * ToroQuestConfiguration.toroAttackDamageMultiplier);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_COW_AMBIENT;
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_COW_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_COW_DEATH;
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
	}

	public void notifyDataManagerChange(DataParameter<?> key) {
		if (CHARGING.equals(key) && this.isCharging() && world.isRemote) {
		}
		super.notifyDataManagerChange(key);
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (ignoreableAttack(source)) {
			setAttackTarget(getAttacker(source));
		}
		return super.attackEntityFrom(source, amount);
	}

	protected boolean ignoreableAttack(DamageSource source) {
		return !this.isEntityInvulnerable(source) && !(source instanceof EntityDamageSourceIndirect);
	}

	private EntityLivingBase getAttacker(DamageSource source) {
		try {
			return (EntityLivingBase) source.getTrueSource();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		try {
			if (world.getTotalWorldTime() % 100L == 0L) {
				syncChargingWithAttackTarget();
			}
		} catch (NullPointerException e) {
			/*
			 * https://github.com/ToroCraft/ToroQuest/issues/116
			 */
		}
	}

	protected void syncChargingWithAttackTarget() {
		setCharging(getAttackTarget() != null || getAttackTarget() != null);
	};

	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		super.setAttackTarget(entitylivingbaseIn);
		syncChargingWithAttackTarget();
	}

	@Override
	public boolean attackEntityAsMob(Entity victim) {

		float attackDamage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int knockback = 2;

		if (victim instanceof EntityLivingBase) {
			attackDamage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) victim).getCreatureAttribute());
			knockback += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean wasDamaged = victim.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);

		if (wasDamaged) {

			setRevengeTarget(getAttackTarget());
			setAttackTarget(null);

			if (knockback > 0 && victim instanceof EntityLivingBase) {
				((EntityLivingBase) victim).knockBack(this, (float) knockback * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F),
						(double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			if (victim instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) victim;
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

			this.applyEnchantments(this, victim);
		}

		return wasDamaged;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F;
	}

	public EntityCow createChild(EntityAgeable ageable) {
		return new EntityCow(this.world);
	}

	public float getEyeHeight() {
		return this.isChild() ? this.height : 1.3F;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			dropLoot();
		}
	}

	private void dropLoot() {
		dropBeef();
		dropLeather();
	}

	protected void dropBeef() {
		ItemStack stack = new ItemStack(Items.BEEF, rand.nextInt(3) + 2);
		EntityItem dropItem = new EntityItem(world, posX, posY, posZ, stack.copy());
		world.spawnEntity(dropItem);
	}

	protected void dropLeather() {
		ItemStack stack = new ItemStack(ItemToroLeather.INSTANCE, 1);
		EntityItem dropItem = new EntityItem(world, posX, posY, posZ, stack.copy());
		world.spawnEntity(dropItem);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return null;
	}
}