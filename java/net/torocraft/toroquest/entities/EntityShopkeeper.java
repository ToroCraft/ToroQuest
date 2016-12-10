package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.render.RenderShopkeeper;
import net.torocraft.toroquest.item.armor.ItemKingArmor;

public class EntityShopkeeper extends EntityVillager implements IMerchant {

	public static String NAME = "shopkeeper";

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityShopkeeper.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0x000000, 0xe0d6b9);
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

	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		boolean flag = stack != null && stack.getItem() == Items.SPAWN_EGG;

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {

			if (!this.worldObj.isRemote) {
				this.setCustomer(player);
				player.displayVillagerTradeGui(this);
			}

			player.addStat(StatList.TALKED_TO_VILLAGER);
			return true;
		} else {
			return super.processInteract(player, hand, stack);
		}
	}

	public void setCustomer(EntityPlayer player) {
		super.setCustomer(player);
	};

	public EntityPlayer getCustomer() {
		return super.getCustomer();
	};

	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return createTradesBaseOnRep(player);
	}

	protected MerchantRecipeList createTradesBaseOnRep(EntityPlayer player) {

		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (player == null) {
			System.out.println("Not player to trade with");
			return recipeList;
		}
		Province civ = CivilizationUtil.getProvinceAt(worldObj, chunkCoordX, chunkCoordZ);
		
		if (civ == null) {
			System.out.println("Not in a civilization");
			return recipeList;
		}

		int rep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ.civilization);

		System.out.println("Basing trades of shopkeeper for CIV[" + civ.civilization + "] and REP[" + rep + "]");

		if (rep > 5) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.STICK), ItemKingArmor.helmetItem));
		} else {
			chat(player, "I don't trade with drifters.");
		}

		return recipeList;
	};

	/**
	 * Get the formatted ChatComponent that will be used for the sender's
	 * username in chat
	 */
	public ITextComponent getDisplayName() {
		TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(NAME, new Object[0]);
		textcomponenttranslation.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponenttranslation.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponenttranslation;
	};

	private void chat(EntityPlayer player, String message) {
		player.addChatMessage(new TextComponentString(message));
	}

}