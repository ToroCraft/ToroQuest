package net.torocraft.toroquest.entities;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.ai.EntityAIStayCentered;
import net.torocraft.toroquest.entities.render.RenderRainbowGuard;

public class EntityRainbowGuard extends EntityMob {

	public static String NAME = "rainbow_guard";
	public static enum COLOR {
		RED(10040115),
		ORANGE(0xff9900),
		YELLOW(0xffff00),
		GREEN(6717235),
		BLUE(3361970),
		PURPLE(8339378);
		
		private int color;
		
		private COLOR (int color) {
			this.color = color;
		}
		
		public int getColor() {
			return color;
		}
	}
	private final COLOR color;

	private static final DataParameter<Boolean> AT_ATTENTION = EntityDataManager.<Boolean>createKey(EntityRainbowGuard.class, DataSerializers.BOOLEAN);
	private static final DataParameter<BlockPos> LOOK_AT = EntityDataManager.<BlockPos>createKey(EntityRainbowGuard.class, DataSerializers.BLOCK_POS);
	
	// TODO: add new data parameter for color when we stop using armor

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityRainbowGuard.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0xffffff, 0x909090);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRainbowGuard.class, new IRenderFactory<EntityRainbowGuard>() {
			@Override
			public Render<EntityRainbowGuard> createRenderFor(RenderManager manager) {
				return new RenderRainbowGuard(manager);
			}
		});
	}
	
	public EntityRainbowGuard(World world) {
		super(world);
		this.color = COLOR.RED;
	}

	public EntityRainbowGuard(World world, COLOR color) {
		super(world);
		this.color = color;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(AT_ATTENTION, Boolean.valueOf(true));
		this.dataManager.register(LOOK_AT, getPosition().add(4, 0, 0));
		this.setLeftHanded(false);
	}

	public boolean isAtAttention() {
		return dataManager.get(AT_ATTENTION);
	}

	private void setAtAttention(boolean b) {
		dataManager.set(AT_ATTENTION, Boolean.valueOf(b));
	}

	private void setLookAt(BlockPos pos) {
		dataManager.set(LOOK_AT, pos.add(0, getEyeHeight(), 0));
	}

	private BlockPos getLookAt() {
		return dataManager.get(LOOK_AT);
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD, 1));
		setCanPickUpLoot(false);
		addArmor();
		return livingdata;
	}

	protected void addArmor() {

		int color = this.color.getColor();

		setItemStackToSlot(EntityEquipmentSlot.HEAD, colorArmor(new ItemStack(Items.LEATHER_HELMET, 1), color));
		setItemStackToSlot(EntityEquipmentSlot.FEET, colorArmor(new ItemStack(Items.LEATHER_BOOTS, 1), color));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, colorArmor(new ItemStack(Items.LEATHER_LEGGINGS, 1), color));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, colorArmor(new ItemStack(Items.LEATHER_CHESTPLATE, 1), color));
	}

	protected ItemStack colorArmor(ItemStack stack, int color) {
		ItemArmor armor = (ItemArmor) stack.getItem();
		armor.setColor(stack, color);
		return stack;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		// this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
		// this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
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
		tasks.addTask(3, new EntityAIAtAttention(this));
		tasks.addTask(3, new EntityAIStayCentered(this));
		tasks.addTask(4, new EntityAIAttackMelee(this, 0.5D, false));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(5, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		List<EntityRainbowGuard> nearbyRainbowGuards = worldObj.getEntitiesWithinAABB(EntityRainbowGuard.class, getEntityBoundingBox().expand(5.0D, 4.0D, 2.0D));
		if (nearbyRainbowGuards != null && !nearbyRainbowGuards.isEmpty()) {
			setLookAt(nearbyRainbowGuards.get(0).getPosition());
		} else {
			setLookAt(getPosition().add(4, 0, 0));
		}
		wakeIfPlayerIsClose(true);
	}

	private static final int WAKE_DIAMETER = 8;

	// to be called by partner's waking function
	public void wokenByPartner() {
		wakeIfPlayerIsClose(false);
	}
	
	private void wakeIfPlayerIsClose(boolean wakePartner) {
		if (worldObj.getTotalWorldTime() % 30 != 0 || !isAtAttention()) {
			return;
		}

		// TODO check if visible?
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPosition()).expand(WAKE_DIAMETER, 6, WAKE_DIAMETER), EntitySelectors.CAN_AI_TARGET);

		if (players.size() < 1) {
			return;
		}

		if (wakePartner) {
			List<EntityRainbowGuard> nearbyRainbowGuards = worldObj.getEntitiesWithinAABB(EntityRainbowGuard.class, getEntityBoundingBox().expand(5.0D, 4.0D, 2.0D));
			if (nearbyRainbowGuards != null && !nearbyRainbowGuards.isEmpty()) {
				nearbyRainbowGuards.get(0).wokenByPartner();
			}
		}

		setAtAttention(false);
		tasks.removeTask(new EntityAIStayCentered(this));

		setAttackTarget(players.get(worldObj.rand.nextInt(players.size())));
	}

	protected void updateAITasks() {
		super.updateAITasks();
	}

	public class EntityAIAtAttention extends EntityAIBase {

		protected EntityRainbowGuard entity;

		public EntityAIAtAttention(EntityRainbowGuard entitylivingIn) {
			this.entity = entitylivingIn;
			this.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			return entity.isAtAttention();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting() {
			return entity.isAtAttention();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {

		}

		/**
		 * Resets the task
		 */
		public void resetTask() {

		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			entity.getLookHelper().setLookPosition(getLookAt().getX(), getLookAt().getY(), getLookAt().getZ(), (float) entity.getHorizontalFaceSpeed(), 0.0f);
		}
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!super.attackEntityFrom(source, amount)) {
			return false;
		}

		if (isAtAttention()) {
			setAtAttention(false);
		}

		EntityLivingBase attacker = this.getAttackTarget();
		if (attacker == null && source.getEntity() instanceof EntityLivingBase) {
			setAttackTarget((EntityLivingBase) source.getEntity());
		}
		return true;
	}

}
