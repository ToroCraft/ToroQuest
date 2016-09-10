package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.model.ModelToro;
import net.torocraft.toroquest.entities.render.RenderToro;

public class EntityToro extends EntityTameable {

	public static String NAME = "toro";

	private static final DataParameter<Boolean> CHARGING = EntityDataManager.<Boolean>createKey(EntityToro.class, DataSerializers.BOOLEAN);

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityToro.class, ToroQuest.MODID + NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0xFF0000, 0x0000FF);
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

	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		if (isCharging()) {
			System.out.println("Reset Charge");
			dataManager.set(CHARGING, false);
		} else {
			System.out.println("Set Charge");
			dataManager.set(CHARGING, true);
		}
		return true;
	}

	public boolean isCharging() {
		return ((Boolean) this.dataManager.get(CHARGING)).booleanValue();
	}

	public EntityToro(World worldIn) {
		super(worldIn);
		setSize(1.8F, 1.6F);
	}

	public static void registerFixesCow(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, "Cow");
	}

	protected void initEntityAI() {
		/*
		 * this.tasks.addTask(0, new EntityAISwimming(this));
		 * this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
		 * this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		 * this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.WHEAT,
		 * false)); this.tasks.addTask(4, new EntityAIFollowParent(this,
		 * 1.25D)); this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		 * this.tasks.addTask(6, new EntityAIWatchClosest(this,
		 * EntityPlayer.class, 6.0F)); this.tasks.addTask(7, new
		 * EntityAILookIdle(this));
		 */
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
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

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_COW;
	}

	public EntityCow createChild(EntityAgeable ageable) {
		return new EntityCow(this.worldObj);
	}

	public float getEyeHeight() {
		return this.isChild() ? this.height : 1.3F;
	}
}