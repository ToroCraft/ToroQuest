package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.render.RenderShopkeeper;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForEarth;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForFire;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForMoon;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForSun;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForWater;
import net.torocraft.toroquest.entities.trades.ShopkeeperTradesForWind;

public class EntityShopkeeper extends EntityVillager implements IMerchant {

	public static String NAME = "shopkeeper";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityShopkeeper.class, NAME, entityId, ToroQuest.INSTANCE, 60,
				2, true, 0x000000, 0xe0d6b9);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityShopkeeper.class, new IRenderFactory<EntityShopkeeper>() {
			@Override
			public Render<EntityShopkeeper> createRenderFor(RenderManager manager) {
				return new RenderShopkeeper(manager);
			}
		});
	}

	public EntityShopkeeper(World worldIn) {
		super(worldIn, 3);
	}

	@Override
	public IEntityLivingData finalizeMobSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_) {
		return p_190672_2_;
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		boolean flag = stack != null && stack.getItem() == Items.SPAWN_EGG;

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {

			if (!this.world.isRemote) {

				RepData rep = getReputation(player);

				if (rep.rep.equals(ReputationLevel.OUTCAST) || rep.rep.equals(ReputationLevel.ENEMY) || rep.rep.equals(ReputationLevel.VILLAIN)) {
					chat(player, "I WILL NOT TRADE WITH A " + rep.rep);
				} else {
					this.setCustomer(player);
					player.displayVillagerTradeGui(this);
				}

			}

			player.addStat(StatList.TALKED_TO_VILLAGER);
			return true;
		} else {
			return super.processInteract(player, hand);
		}
	}

	public void setCustomer(EntityPlayer player) {
		super.setCustomer(player);
	}

	public EntityPlayer getCustomer() {
		return super.getCustomer();
	}

	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return createTradesBaseOnRep(player);
	}

	protected MerchantRecipeList createTradesBaseOnRep(EntityPlayer player) {
		RepData rep = getReputation(player);
		switch (rep.civ) {
		case WIND:
			return ShopkeeperTradesForWind.trades(player, rep.rep);
		case EARTH:
			return ShopkeeperTradesForEarth.trades(player, rep.rep);
		case FIRE:
			return ShopkeeperTradesForFire.trades(player, rep.rep);
		case MOON:
			return ShopkeeperTradesForMoon.trades(player, rep.rep);
		case SUN:
			return ShopkeeperTradesForSun.trades(player, rep.rep);
		case WATER:
			return ShopkeeperTradesForWater.trades(player, rep.rep);
		default:
			return new MerchantRecipeList();
		}
	};

	/**
	 * Get the formatted ChatComponent that will be used for the sender's
	 * username in chat
	 */
	public ITextComponent getDisplayName() {
		TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("entity.toroquest.shopkeeper.name", new Object[0]);
		textcomponenttranslation.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponenttranslation.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponenttranslation;
	};

	private void chat(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(message));
	}

	private static class RepData {
		CivilizationType civ = CivilizationType.EARTH;
		ReputationLevel rep = ReputationLevel.DRIFTER;
	}

	private RepData getReputation(EntityPlayer player) {
		RepData rep = new RepData();

		if (player == null) {
			return rep;
		}

		Province province = CivilizationUtil.getProvinceAt(world, chunkCoordX, chunkCoordZ);

		if (province == null) {
			return rep;
		}

		rep.civ = province.civilization;

		if (rep.civ == null) {
			return rep;
		}
		rep.rep = ReputationLevel.fromReputation(PlayerCivilizationCapabilityImpl.get(player).getReputation(rep.civ));
		return rep;
	}

}