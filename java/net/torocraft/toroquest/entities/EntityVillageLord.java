package net.torocraft.toroquest.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.render.RenderVillageLord;
import net.torocraft.toroquest.gui.VillageLordGuiHandler;
import net.torocraft.toroquest.item.armor.ItemRoyalArmor;

/*
 * on damage, remove rep
 * 
 * 
 * 
 */

public class EntityVillageLord extends EntityToroNpc {

	public static String NAME = "village_lord";

	public EntityVillageLord(World world) {
		super(world, null);
	}

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityVillageLord.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0xeca58c, 0xba12c8);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityVillageLord.class, new IRenderFactory<EntityVillageLord>() {
			@Override
			public Render<EntityVillageLord> createRenderFor(RenderManager manager) {
				return new RenderVillageLord(manager);
			}
		});
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.isEntityAlive() && !this.isChild()) {
			if (!this.world.isRemote) {
				player.openGui(ToroQuest.INSTANCE, VillageLordGuiHandler.getGuiID(), world,
						player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			}
			return true;
		} else {
			return false;
		}
	}

	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.0D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		setCanPickUpLoot(false);
		addArmor();
		return livingdata;
	}

	protected void addArmor() {
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemRoyalArmor.helmetItem, 1));
		setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(ItemRoyalArmor.bootsItem, 1));
		setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(ItemRoyalArmor.leggingsItem, 1));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ItemRoyalArmor.chestplateItem, 1));
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}

		dropRepTo(source.getEntity());

		if (source.getEntity() instanceof EntityLivingBase) {
			callForHelp((EntityLivingBase) source.getEntity());
		}

		return super.attackEntityFrom(source, amount);
	}

	private void dropRepTo(Entity entity) {
		if (entity == null) {
			return;
		}

		if (!(entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;

		CivilizationType civ = getCivilization();
		if (civ == null) {
			return;
		}

		PlayerCivilizationCapabilityImpl.get(player).adjustReputation(civ, -5);
	}

	private void callForHelp(EntityLivingBase attacker) {
		List<EntityToroNpc> help = world.getEntitiesWithinAABB(EntityToroNpc.class, new AxisAlignedBB(getPosition()).expand(16, 6, 16), new Predicate<EntityToroNpc>() {
			public boolean apply(@Nullable EntityToroNpc entity) {
				if (!(entity instanceof EntityGuard || entity instanceof EntitySentry)) {
					return false;
				}

				CivilizationType civ = entity.getCivilization();

				if (civ == null) {
					return false;
				}

				return civ.equals(EntityVillageLord.this.getCivilization());
			}
		});

		for (EntityToroNpc entity : help) {
			entity.setAttackTarget(attacker);
		}
	}

}
