package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.entities.ai.EntityAINearestAttackableCivTarget;
import net.torocraft.toroquest.entities.render.RenderSentry;

public class EntitySentry extends EntityToroNpc {

	public static String NAME = "sentry";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntitySentry.class, NAME, entityId, ToroQuest.INSTANCE, 80, 2, true, 0x3f3024, 0xe0d6b9);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySentry.class, new IRenderFactory<EntitySentry>() {
			@Override
			public Render<EntitySentry> createRenderFor(RenderManager manager) {
				return new RenderSentry(manager);
			}
		});
	}

	public EntitySentry(World world, CivilizationType civ) {
		super(world, civ);
	}

	public EntitySentry(World world) {
		super(world, null);
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackMelee(this, 0.6D, false));
		tasks.addTask(7, new EntityAIWander(this, 0.35D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));

		targetTasks.addTask(2, new EntityAINearestAttackableCivTarget(this));
		targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityMob.class, 2, false, false, new Predicate<EntityMob>() {
			@Override
			public boolean apply(EntityMob target) {
				return !(target instanceof EntityCreeper);
			}
		}));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		// TODO call for backup
		// this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble()
		// *
		// net.minecraftforge.common.ForgeModContainer.zombieSummonBaseChance);
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

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		setCanPickUpLoot(true);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);

		setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD, 1));

		addArmor();

		return livingdata;
	}

	protected void addArmor() {
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET, 1));
		setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS, 1));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS, 1));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE, 1));
	}

}
