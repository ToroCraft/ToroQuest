package net.torocraft.toroquest.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.CivilizationDataAccessor;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.render.RenderVillageLord;
import net.torocraft.toroquest.gui.VillageLordGuiHandler;
import net.torocraft.toroquest.inventory.IVillageLordInventory;
import net.torocraft.toroquest.inventory.VillageLordInventory;
import net.torocraft.toroquest.item.armor.ItemRoyalArmor;

public class EntityVillageLord extends EntityToroNpc implements IInventoryChangedListener {

	public static String NAME = "village_lord";
	public static Achievement LORD_ACHIEVEMNT = new Achievement("village_lord", "village_lord", 0, 0, Items.DIAMOND_SWORD, null).registerStat();

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
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (world.getTotalWorldTime() % 1000 == 0 && !isDead) {
			setHasLord(true);
		}
	}

	protected Map<UUID, VillageLordInventory> inventories = new HashMap<UUID, VillageLordInventory>();

	public EntityVillageLord(World world) {
		super(world, null);
		initInventories();
	}

	protected int getInventorySize() {
		return 9;
	}

	public IVillageLordInventory getInventory(UUID playerId) {
		if (inventories.get(playerId) == null) {
			inventories.put(playerId, new VillageLordInventory(this, "VillageLordInventory", this.getInventorySize()));
		}
		return inventories.get(playerId);
	}

	protected void initInventories() {
		Map<UUID, VillageLordInventory> newInventories = new HashMap<UUID, VillageLordInventory>();
		for (UUID playerId : inventories.keySet()) {
			newInventories.put(playerId, initInventory(inventories.get(playerId)));
		}
	}

	protected VillageLordInventory initInventory(VillageLordInventory prevInventory) {
		VillageLordInventory newInventory = new VillageLordInventory(this, "VillageLordInventory", this.getInventorySize());
		newInventory.setCustomName(this.getName());

		if (prevInventory != null) {
			prevInventory.removeInventoryChangeListener(this);
			int i = Math.min(prevInventory.getSizeInventory(), newInventory.getSizeInventory());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = prevInventory.getStackInSlot(j);

				if (!itemstack.isEmpty()) {
					newInventory.setInventorySlotContents(j, itemstack.copy());
				}
			}
		}

		newInventory.addInventoryChangeListener(this);
		return newInventory;
	}

	public void openGUI(EntityPlayer player) {
		if (world.isRemote) {
			return;
		}
		IVillageLordInventory inventory = getInventory(player.getUniqueID());
		player.openGui(ToroQuest.INSTANCE, VillageLordGuiHandler.getGuiID(), world, getPosition().getX(), getPosition().getY(), getPosition().getZ());
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.isEntityAlive() && !this.isChild()) {
			if (!this.world.isRemote) {
				openGUI(player);
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
		if (!isDead) {
			setHasLord(true);
		}
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		setCanPickUpLoot(false);
		addArmor();
		if (!isDead) {
			setHasLord(true);
		}
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

	@Override
	public void onInventoryChanged(IInventory invBasic) {

	}

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		// TODO set no lord

		if (world.isRemote || inventories == null) {
			return;
		}

		for (IVillageLordInventory inventory : inventories.values()) {
			dropInventory(inventory);
		}

		achievement(cause);
		setHasLord(false);
	}

	protected void achievement(DamageSource cause) {
		if (world.isRemote) {
			return;
		}
		Entity entity = cause.getEntity();
		if (entity != null && entity instanceof EntityPlayer) {
			((EntityPlayer) entity).addStat(LORD_ACHIEVEMNT);
		}
	}

	protected void dropInventory(IVillageLordInventory inventory) {
		if (inventory == null) {
			return;
		}
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack itemstack = inventory.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				entityDropItem(itemstack, 0.0F);
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		NBTTagCompound c = new NBTTagCompound();
		for (Entry<UUID, VillageLordInventory> e : inventories.entrySet()) {
			c.setTag(e.getKey().toString(), e.getValue().saveAllItems());
		}
		compound.setTag("Items", c);
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		NBTTagCompound c = compound.getCompoundTag("Items");
		inventories = new HashMap<UUID, VillageLordInventory>();
		for (String sPlayerId : c.getKeySet()) {
			VillageLordInventory inv = new VillageLordInventory(this, "VillageLordInventory", getInventorySize());
			inv.loadAllItems(c.getTagList(sPlayerId, 10));
			inventories.put(UUID.fromString(sPlayerId), inv);
		}
	}

	public static void registerFixesVillageLord(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityVillageLord.class);
		fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityVillageLord.class, new String[] { "Items" }));
	}

	private void setHasLord(boolean hasLord) {
		Province province = CivilizationUtil.getProvinceAt(world, chunkCoordX, chunkCoordY);
		if (province == null) {
			return;
		}
		CivilizationDataAccessor worldData = CivilizationsWorldSaveData.get(world);
		if (worldData.provinceHasLord(province.id) == hasLord) {
			return;
		}

		if (isDead && hasLord) {
			hasLord = false;
		}

		worldData.setProvinceHasLord(province.id, hasLord);
	}
}
