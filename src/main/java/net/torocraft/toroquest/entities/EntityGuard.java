package net.torocraft.toroquest.entities;

import java.util.Calendar;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.render.RenderGuard;

public class EntityGuard extends EntityToroNpc {

	/** The width of the entity */
	private float zombieWidth = -1.0F;
	/** The height of the the entity. */
	private float zombieHeight;

	public static String NAME = "guard";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityGuard.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0x3f3024, 0xe0d6b9);
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
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	protected void initEntityAI() {
		/// zombieAi();

		tasks.addTask(7, new EntityAIWander(this, 0.5D));
		// tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class,
		// 8.0F));
		// tasks.addTask(8, new EntityAILookIdle(this));
		applyEntityAI();
	}

	protected void zombieAi() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		// this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	protected void applyEntityAI() {

		// zombieAttack();
	}

	protected void zombieAttack() {
		this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	protected void entityInit() {
		super.entityInit();

		// this.getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
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

	public boolean attackEntityAsMob(Entity victum) {

		if (!super.attackEntityAsMob(victum)) {
			return false;
		}

		return true;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_ZOMBIE;
	}

	/**
	 * Called only once on an entity when first time spawned, via egg, mob
	 * spawner, natural spawning etc, but not called when entity is reloaded
	 * from nbt. Mainly used for initializing attributes and inventory
	 */
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		this.setCanPickUpLoot(true);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);

		if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null) {
			Calendar calendar = this.worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
				this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
				this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
			}
		}

		return livingdata;
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
