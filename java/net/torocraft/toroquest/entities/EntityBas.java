package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.render.RenderBas;

public class EntityBas extends EntitySkeleton {

	public static String NAME = "bas";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityBas.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0xffffff, 0x909090);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBas.class, new IRenderFactory<EntityBas>() {
			@Override
			public Render<EntityBas> createRenderFor(RenderManager manager) {
				return new RenderBas(manager);
			}
		});
	}

	public EntityBas(World world) {
		super(world);
		this.setSize(0.6F * 3, 1.99F * 3);
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
	}

	@Override
	public float getEyeHeight() {
		return super.getEyeHeight() * 3f;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
	}

	/**
	 * Called only once on an entity when first time spawned, via egg, mob
	 * spawner, natural spawning etc, but not called when entity is reloaded
	 * from nbt. Mainly used for initializing attributes and inventory
	 */
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		/*
		 * this.setEquipmentBasedOnDifficulty(difficulty);
		 * this.setEnchantmentBasedOnDifficulty(difficulty);
		 * this.setCombatTask(); this.setCanPickUpLoot(this.rand.nextFloat() <
		 * 0.55F * difficulty.getClampedAdditionalDifficulty());
		 * 
		 * if
		 * (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b())
		 * { Calendar calendar = this.worldObj.getCurrentDate();
		 * 
		 * if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 &&
		 * this.rand.nextFloat() < 0.25F) {
		 * this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new
		 * ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN :
		 * Blocks.PUMPKIN));
		 * this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] =
		 * 0.0F; } }
		 */

		return livingdata;
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
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

}
