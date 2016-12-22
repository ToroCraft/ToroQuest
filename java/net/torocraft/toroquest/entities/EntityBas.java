package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
		this.experienceValue = 75;
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, colorArmor(new ItemStack(Items.LEATHER_HELMET, 1), 0xb0b0b0));
	}

	protected ItemStack colorArmor(ItemStack stack, int color) {
		ItemArmor armor = (ItemArmor) stack.getItem();
		armor.setColor(stack, color);
		stack.getTagCompound().setBoolean("Unbreakable", true);
		return stack;
	}

	@Override
	public float getEyeHeight() {
		return super.getEyeHeight() * 3f;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
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
		tasks.addTask(2, new EntityAIAttackMelee(this, 0.5D, false));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	private void spawnBats() {
		long start = System.currentTimeMillis();
		int playerCount = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPosition()).expand(40, 20, 40)).size();
		int batCount = worldObj.getEntitiesWithinAABB(EntityVampireBat.class, new AxisAlignedBB(getPosition()).expand(40, 20, 40)).size();

		if (batCount > 7 * playerCount) {
			return;
		}

		for (int i = 0; i < 3 + rand.nextInt(4); i++) {
			spawnBat();
		}
	}

	protected void spawnBat() {
		Entity mob = new EntityVampireBat(worldObj);
		mob.setPosition(posX + rand.nextInt(6) - 3, posY + 4, posZ + rand.nextInt(6) - 3);
		worldObj.spawnEntityInWorld(mob);
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (worldObj.getTotalWorldTime() % 100 == 0) {
			spawnBats();
		}

		if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float f = this.getBrightness(1.0F);
			BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos)) {
				boolean flag = true;
				ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

				if (!itemstack.func_190926_b()) {
					if (itemstack.isItemStackDamageable()) {
						itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

						if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
							this.renderBrokenItemStack(itemstack);
							this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.field_190927_a);
						}
					}

					flag = false;
				}

				if (flag) {
					this.setFire(8);
				}
			}
		}

	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!worldObj.isRemote) {
			dropLoot();
		}
	}

	private void dropLoot() {
		dropLootItem(Items.BONE, rand.nextInt(100));
		dropLootItem(Items.ARROW, rand.nextInt(20));
		dropLootItem(Items.DIAMOND, rand.nextInt(5));
		dropLootItem(Items.EMERALD, rand.nextInt(10));
		dropLootItem(Items.STICK, rand.nextInt(10));
	}

	private void dropLootItem(Item item, int amount) {
		if (amount == 0) {
			return;
		}

		for (int i = 0; i < amount; i++) {
			ItemStack stack = new ItemStack(item);
			EntityItem dropItem = new EntityItem(worldObj, posX, posY, posZ, stack.copy());
			dropItem.setNoPickupDelay();
			dropItem.motionY = rand.nextDouble();
			dropItem.motionZ = rand.nextDouble() - 0.5d;
			dropItem.motionX = rand.nextDouble() - 0.5d;
			worldObj.spawnEntityInWorld(dropItem);
		}

	}

}
