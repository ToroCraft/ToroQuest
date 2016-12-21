package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.render.RenderRainbowKing;

public class EntityRainbowKing extends EntityMob {

	public static String NAME = "rainbow_king";
	
	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityRainbowKing.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 10040115, 3361970);
	}
	
	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRainbowKing.class, new IRenderFactory<EntityRainbowKing>() {
			@Override
			public Render<? super EntityRainbowKing> createRenderFor(RenderManager manager) {
				return new RenderRainbowKing(manager);
			}
		});
	}
	
	public EntityRainbowKing(World worldIn) {
		super(worldIn);
		this.setSize(0.6F * 2, 1.99F * 2);
		this.experienceValue = 75;
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, colorArmor(new ItemStack(Items.LEATHER_HELMET, 1), 8339378));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, colorArmor(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 3361970));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, colorArmor(new ItemStack(Items.LEATHER_LEGGINGS, 1), 0xffff00));
		setItemStackToSlot(EntityEquipmentSlot.FEET, colorArmor(new ItemStack(Items.LEATHER_BOOTS, 1), 10040115));
	}
	
	protected ItemStack colorArmor(ItemStack stack, int color) {
		ItemArmor armor = (ItemArmor) stack.getItem();
		armor.setColor(stack, color);
		stack.getTagCompound().setBoolean("Unbreakable", true);
		return stack;
	}
	
	@Override
	public float getEyeHeight() {
		return super.getEyeHeight() * 2f;
	}
	
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		setEquipmentBasedOnDifficulty(difficulty);
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
}
