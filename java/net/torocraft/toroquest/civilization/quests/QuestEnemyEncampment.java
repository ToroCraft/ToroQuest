package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.block.BlockToroSpawner;
import net.torocraft.toroquest.block.TileEntityToroSpawner;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestEnemyEncampment extends QuestBase implements Quest {
	public static int ID;
	public static QuestEnemyEncampment INSTANCE;

	private final static int hutHalfWidth = 6;

	// TODO make dynamic and save in quest data
	private final static int mobCount = 30;

	public static void init(int id) {
		INSTANCE = new QuestEnemyEncampment();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> in) {

		if (getKills(data) < 1) {
			data.getPlayer().sendMessage(new TextComponentString("You didn't personally kill any from the encampment!"));
			return null;
		}

		int count = countEntities(data);

		if (count > 0) {
			if (count == 1) {
				data.getPlayer().sendMessage(new TextComponentString("You didn't kill all of them, there is one left!"));
			} else {
				data.getPlayer().sendMessage(new TextComponentString("You didn't kill all of them, there are " + count + " left"));
			}
			return null;
		}

		data.setCompleted(true);

		in.addAll(getRewardItems(data));

		if (getKills(data) == mobCount) {
			addGoldenSword(data, in);
		}

		return in;
	}

	protected void addGoldenSword(QuestData data, List<ItemStack> in) {
		ItemStack sword = new ItemStack(Items.GOLDEN_SWORD);
		sword.addEnchantment(Enchantment.getEnchantmentByID(16), 5);
		sword.addEnchantment(Enchantment.getEnchantmentByID(21), 3);
		Province inProvince = PlayerCivilizationCapabilityImpl.get(data.getPlayer()).getInCivilization();
		if (inProvince.id.equals(data.getProvinceId())) {
			sword.setStackDisplayName("Golden Sword of " + inProvince.name);
		}
		in.add(sword);
	}

	private int countEntities(final QuestData data) {
		Predicate<Entity> filter = new Predicate<Entity>() {
			public boolean apply(@Nullable Entity entity) {
				return entity.getTags().contains("encampment_quest") && entity.getTags().contains(data.getQuestId().toString());
			}
		};
		return data.getPlayer().world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(getSpawnPosition(data)).expand(80, 40, 80), filter).size();
	}

	@Override
	public void reject(QuestData data) {

	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		spawn(data);
		return in;
	}

	private void spawn(QuestData data) {
		BlockPos pos = searchForSuitableLocation(data);
		setSpawnPosition(data, pos);
		buildHut(data, pos);
		addToroSpawner(data, data.getPlayer().getEntityWorld(), getSpawnPosition(data), getEnemyType(data));
	}

	private BlockPos searchForSuitableLocation(QuestData data) {
		// TODO support underground and under water positions
		Random rand = data.getPlayer().getEntityWorld().rand;
		BlockPos pos = null;
		for (int i = 0; i < 100; i++) {
			pos = randomLocation(data, rand, false);
			if (pos != null) {
				break;
			}
		}
		if (pos == null) {
			return randomLocation(data, rand, true);
		}
		return pos;
	}

	private BlockPos randomLocation(QuestData data, Random rand, boolean force) {
		int distance = rand.nextInt(300) + 500;
		int directionDegrees = rand.nextInt(360);
		int z = distance * (int) Math.round(Math.sin(Math.toRadians(directionDegrees)));
		int x = distance * (int) Math.round(Math.cos(Math.toRadians(directionDegrees)));
		BlockPos pos = findSurface(data, x + (int) data.getPlayer().posX, z + (int) data.getPlayer().posZ, force);

		if (pos == null) {
			return null;
		}

		if (data.getPlayer().world.isAnyPlayerWithinRangeAt((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 60d)) {
			return null;
		}

		try {
			pos = new BlockPos(pos.getX(), getLocalMinimum(data, pos), pos.getZ());
		} catch (NotLevelEnoughException e) {
			if (force) {
				return pos;
			}
			return null;
		}

		return pos;
	}

	private int getLocalMinimum(QuestData data, BlockPos pos) throws NotLevelEnoughException {
		int w = hutHalfWidth;
		int max = 0, min = 1000;
		int y;
		for (int x = -w; x <= w; x++) {
			for (int z = -w; z <= w; z++) {
				y = findSurface(data, pos.getX() + x, pos.getZ() + z, true).getY();
				max = Math.max(y, max);
				min = Math.min(y, min);
			}
		}
		if (max - min > 5) {
			throw new NotLevelEnoughException("Offest " + (max - min));
		}

		return min;
	}

	private static class NotLevelEnoughException extends Exception {

		public NotLevelEnoughException(String message) {
			super(message);
		}

	}

	private BlockPos findSurface(QuestData data, int x, int z, boolean force) {
		World world = data.getPlayer().getEntityWorld();
		BlockPos pos = new BlockPos(x, world.getActualHeight(), z);
		IBlockState blockState;
		while (pos.getY() > 0) {
			blockState = world.getBlockState(pos);
			if (!force && isLiquid(blockState)) {
				return null;
			}
			if (isGroundBlock(blockState)) {
				break;
			}
			pos = pos.down();
		}
		return pos.up();
	}



	private void buildHut(QuestData data, BlockPos pos) {
		World world = data.getPlayer().getEntityWorld();
		if (pos == null) {
			return;
		}
		int w = hutHalfWidth;

		BlockPos pointer;
		IBlockState block;

		for (int x = -w; x <= w; x++) {
			for (int y = 0; y <= w; y++) {
				for (int z = -w; z <= w; z++) {
					pointer = pos.add(x, y, z);

					block = world.getBlockState(pointer);

					if (cantBuildOver(block)) {
						continue;
					}

					if (y + Math.abs(z) == w) {
						if (x % 2 == 0) {
							world.setBlockState(pointer, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED));
						} else {
							world.setBlockState(pointer, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK));
						}
					} else if (z == 0 && (x == w || x == -w)) {
						world.setBlockState(pointer, Blocks.DARK_OAK_FENCE.getDefaultState());
					}

				}
			}
		}
	}

	private void addToroSpawner(QuestData data, World world, BlockPos blockpos, List<String> entities) {
		world.setBlockState(blockpos, BlockToroSpawner.INSTANCE.getDefaultState());
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityToroSpawner) {
			TileEntityToroSpawner spawner = (TileEntityToroSpawner) tileentity;
			spawner.setTriggerDistance(80);
			spawner.setEntityIds(entities);
			spawner.setSpawnRadius(4);
			spawner.setHelmet(new ItemStack(Items.DIAMOND_HELMET));
			spawner.setBoots(new ItemStack(Items.DIAMOND_BOOTS));
			spawner.setLeggings(new ItemStack(Items.DIAMOND_LEGGINGS));
			spawner.setChestplate(new ItemStack(Items.DIAMOND_CHESTPLATE));
			spawner.addEntityTag(data.getQuestId().toString());
			spawner.addEntityTag("encampment_quest");
		} else {
			System.out.println("tile entity is missing");
		}
	}

	@Override
	public String getTitle(QuestData data) {
		if (data == null) {
			return "";
		}
		return "Clear Encampment";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		s.append("- Clear " + getEnemyNames(data) + " encampment\n");
		if (getSpawnPosition(data) != null) {
			s.append("- " + getDirections(getProvincePosition(getQuestProvince(data)), getSpawnPosition(data)) + "\n");
		}
		s.append("- Reward ").append(listItems(getRewardItems(data))).append("\n");
		s.append("- Recieve ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	private String getEnemyNames(QuestData data) {
		String name = getEnemyType(data).get(0);
		try {
			Entity entity = TileEntityToroSpawner.getEntityForId(data.getPlayer().world, name);
			return entity.getName();
		} catch (Exception e) {
			System.out.println("failed to get name of entity [" + name + "] : " + e.getMessage());
			return "unknown enemy";
		}
	}


	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		Random rand = player.getEntityWorld().rand;
		QuestData data = new QuestData();
		data.setCiv(province.civilization);
		data.setPlayer(player);
		data.setProvinceId(province.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);
		chooseEnemyType(data);
		setRewardRep(data, 50);

		List<ItemStack> reward = new ArrayList<ItemStack>(1);
		reward.add(new ItemStack(Items.EMERALD, 10));
		setRewardItems(data, reward);

		return data;
	}

	private void chooseEnemyType(QuestData data) {
		// TODO add variety

		// TODO set amount
		List<String> enemies = new ArrayList<String>();
		for (int i = 0; i < mobCount; i++) {
			enemies.add("minecraft:stray");
		}
		setEnemyType(data, enemies);
	}

	private BlockPos getSpawnPosition(QuestData data) {
		NBTTagCompound c = getCustomNbtTag(data);
		if (c.getInteger("locationFound") != 1) {
			return null;
		}
		return new BlockPos(c.getInteger("pos_x"), c.getInteger("pos_y"), c.getInteger("pos_z"));
	}

	private void setSpawnPosition(QuestData data, BlockPos pos) {
		NBTTagCompound c = getCustomNbtTag(data);
		c.setInteger("pos_x", pos.getX());
		c.setInteger("pos_y", pos.getY());
		c.setInteger("pos_z", pos.getZ());
		c.setInteger("locationFound", 1);
	}

	private List<String> getEnemyType(QuestData data) {
		List<String> enemies = new ArrayList<String>();
		NBTTagCompound c = getCustomNbtTag(data);
		try {
			NBTTagList list = (NBTTagList) c.getTag("enemies");
			for (int i = 0; i < list.tagCount(); i++) {
				enemies.add(list.getStringTagAt(i));
			}
			return enemies;
		} catch (Exception e) {
			System.out.println("Failed to load enemy types: " + e.getMessage());
			return getDefaultEnemies(data);
		}
	}

	private List<String> getDefaultEnemies(QuestData data) {
		List<String> zombies = new ArrayList<String>();
		for (int i = 0; i < 69; i++) {
			zombies.add("zombie");
		}
		return zombies;
	}

	private void setEnemyType(QuestData data, List<String> enemies) {
		NBTTagCompound c = getCustomNbtTag(data);
		NBTTagList list = new NBTTagList();
		for (String enemy : enemies) {
			list.appendTag(new NBTTagString(enemy));
		}
		c.setTag("enemies", list);
	}

	public static Integer getKills(QuestData data) {
		return i(data.getiData().get("kills"));
	}

	public static void incrementKills(QuestData data) {
		data.getiData().put("kills", getKills(data) + 1);
	}

	@SubscribeEvent
	public void checkkills(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victum = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (!victum.getTags().contains("encampment_quest")) {
			return;
		}

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		} else {
			return;
		}

		Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
		for (QuestData data : quests) {
			if (ID == data.getQuestType() && victum.getTags().contains(data.getQuestId().toString())) {
				incrementKills(data);
				return;
			}
		}
	}
}
