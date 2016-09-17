package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

		tasks.addTask(2, new EntityAIAttackMelee(this, 0.6D, false));
		tasks.addTask(7, new EntityAIWander(this, 0.5D));

		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGuard.class, true));

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

		setCanPickUpLoot(true);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);

		setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD, 1));
		setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.SHIELD, 1));

		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET, 1));
		setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS, 1));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS, 1));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE, 1));


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
