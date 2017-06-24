package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.QuestBase;
import net.torocraft.toroquest.config.ToroQuestConfiguration;
import net.torocraft.toroquest.entities.render.RenderFugitive;

public class EntityFugitive extends EntityVillager implements IMerchant {

	public static String NAME = "fugitive";

	static {
		if (ToroQuestConfiguration.specificEntityNames) {
			NAME = ToroQuestEntities.ENTITY_PREFIX + NAME;
		}
	}
	//TODO
	//public static Achievement BOUNTY_HUNTER_ACHIEVEMNT = new Achievement("bounty_hunter", "bounty_hunter", 0, 0, Items.DIAMOND_SWORD, null)
	//		.registerStat();

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityFugitive.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2,
				true, 0x000000, 0xe0d6b9);
	}

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFugitive.class, new IRenderFactory<EntityFugitive>() {
			@Override
			public Render<EntityFugitive> createRenderFor(RenderManager manager) {
				return new RenderFugitive(manager);
			}
		});
	}

	public EntityFugitive(World worldIn) {
		super(worldIn, 3);
	}

	public boolean canBeLeashedTo(EntityPlayer player) {
		return true;
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return false;
	}

	@Override
	public IEntityLivingData finalizeMobSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_) {
		return p_190672_2_;
	}

	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 1, 0.5));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPlayer.class, PLAYER_WITH_LEAD, 60.0F, 1.5D, 1D));
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		achievement(cause);

		if (!world.isRemote) {
			dropLoot();
		}
	}

	protected void achievement(DamageSource cause) {
		if (world.isRemote) {
			return;
		}
		Entity entity = cause.getTrueSource();
		if (entity != null && entity instanceof EntityPlayer) {
			//((EntityPlayer) entity).addStat(BOUNTY_HUNTER_ACHIEVEMNT);
		}
	}

	private void dropLoot() {
		if (rand.nextInt(4) == 0) {
			dropItem(randomStolenItem());
		}
	}

	private static final Item[] STOLEN_ITEMS = { Items.DIAMOND_AXE, Items.IRON_AXE, Items.DIAMOND_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_APPLE,
			Items.GOLD_INGOT, Items.DIAMOND, Items.BOW, Items.SHIELD, Items.BANNER, Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.CHAINMAIL_HELMET,
			Items.DIAMOND_BOOTS };

	private ItemStack randomStolenItem() {
		ItemStack stolenItem = new ItemStack(STOLEN_ITEMS[rand.nextInt(STOLEN_ITEMS.length)]);
		stolenItem = EnchantmentHelper.addRandomEnchantment(rand, stolenItem, 30, true);
		setProvince(stolenItem);
		return stolenItem;
	}

	protected void setProvince(ItemStack stolenItem) {
		Province stolenFrom = QuestBase.chooseRandomProvince(null, world, true);
		if (stolenFrom == null) {
			return;
		}

		if (!stolenItem.hasTagCompound()) {
			stolenItem.setTagCompound(new NBTTagCompound());
		}

		String id = stolenFrom.id.toString();
		stolenItem.getTagCompound().setString("provinceId", id);
		stolenItem.getTagCompound().setBoolean("isStolen", true);
		stolenItem.setStackDisplayName("Stolen " + stolenItem.getDisplayName() + " of " + stolenFrom.name);
	}

	private void dropItem(ItemStack stack) {
		EntityItem dropItem = new EntityItem(world, posX, posY, posZ, stack);
		dropItem.setNoPickupDelay();
		world.spawnEntity(dropItem);
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's
	 * username in chat
	 */
	public ITextComponent getDisplayName() {
		TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("entity.toroquest.fugitive.name", new Object[0]);
		textcomponenttranslation.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponenttranslation.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponenttranslation;
	};

	public static final Predicate<EntityPlayer> PLAYER_WITH_LEAD = new Predicate<EntityPlayer>() {
		public boolean apply(@Nullable EntityPlayer player) {
			return player.isEntityAlive() && holdingLead(player);
		}

		private boolean holdingLead(EntityPlayer player) {
			return holdingLeadIn(player, EntityEquipmentSlot.MAINHAND) || holdingLeadIn(player, EntityEquipmentSlot.MAINHAND);
		}

		private boolean holdingLeadIn(EntityPlayer player, EntityEquipmentSlot mainhand) {
			ItemStack itemstack = player.getItemStackFromSlot(mainhand);
			return !itemstack.isEmpty() && itemstack.getItem() == Items.LEAD;
		}
	};

}