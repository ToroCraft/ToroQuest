package net.torocraft.toroquest.civilization;

import java.util.Collection;
import java.util.TreeMap;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.toroquest.ToroQuest;

public class CivilizationsWorldSaveData extends WorldSavedData {

	private static final String DATA_NAME = ToroQuest.MODID + "_civilizations";
	private static final int RADIUS = 12;

	private TreeMap<Integer, TreeMap<Integer, Border>> borders = new TreeMap<Integer, TreeMap<Integer, Border>>();

	public CivilizationsWorldSaveData() {
		super(DATA_NAME);
	}

	public CivilizationsWorldSaveData(String name) {
		super(name);
	}

	public Border getCivilizationBorderAt(Integer chunkX, Integer chunkZ) {
		Collection<Border> inBorders = null;

		try {
			for (TreeMap<Integer, Border> zValues : borders.subMap(chunkX - RADIUS, chunkX + RADIUS).values()) {
				inBorders = zValues.subMap(chunkZ - RADIUS, chunkZ + RADIUS).values();
			}
		} catch (Exception e) {
			return null;
		}

		if (inBorders == null || inBorders.size() < 1) {
			return null;
		}

		if (inBorders.size() == 1) {
			for (Border border : inBorders) {
				return border;
			}
		}

		TreeMap<Double, Border> bordersByDistance = new TreeMap<Double, CivilizationsWorldSaveData.Border>();

		for (Border border : inBorders) {
			bordersByDistance.put(border.chunkDistanceSq(chunkX, chunkZ), border);
		}

		Border border = bordersByDistance.firstEntry().getValue();

		if (border != null) {
			return border;
		}

		return null;
	}

	public Civilization getCivilizationAt(Integer chunkX, Integer chunkZ) {
		Border border = getCivilizationBorderAt(chunkX, chunkZ);
		if (border == null) {
			return null;
		}
		return border.civilization;
	}

	public void registerBorder(int chunkX, int chunkZ, Civilization civ) {
		Border border = new Border();
		border.chunkX = chunkX;
		border.chunkZ = chunkZ;
		border.civilization = civ;
		addBorder(border);
		markDirty();
	}

	protected void addBorder(Border border) {
		System.out.println("Loading Civ Border X[" + border.chunkX + "] Z[" + border.chunkZ + "] CIV[" + border.civilization + "]");
		if (borders.get(border.chunkX) == null) {
			borders.put(border.chunkX, new TreeMap<Integer, Border>());
		}
		borders.get(border.chunkX).put(border.chunkZ, border);
	}

	@Override
	public void readFromNBT(NBTTagCompound t) {
		NBTTagList list;
		try {
			list = (NBTTagList) t.getTag("borders");
		} catch (Exception e) {
			list = new NBTTagList();
		}
		for (int i = 0; i < list.tagCount(); i++) {
			Border border = new Border();
			border.readNBT(list.getCompoundTagAt(i));
			addBorder(border);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound t) {
		NBTTagList list = new NBTTagList();
		for (TreeMap<Integer, Border> sub : borders.values()) {
			for (Border border : sub.values()) {
				list.appendTag(border.writeNBT());
			}
		}
		t.setTag("borders", list);
		return t;
	}

	public static CivilizationsWorldSaveData get(World world) {
		MapStorage storage = world.getMapStorage();
		CivilizationsWorldSaveData instance = (CivilizationsWorldSaveData) storage.getOrLoadData(CivilizationsWorldSaveData.class, DATA_NAME);
		if (instance == null) {
			instance = new CivilizationsWorldSaveData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}

	public static class Border {
		public int chunkX;
		public int chunkZ;
		public Civilization civilization;

		public void readNBT(NBTTagCompound c) {
			chunkX = c.getInteger("chunkX");
			chunkZ = c.getInteger("chunkZ");
			civilization = Civilization.valueOf(c.getString("civilization"));
		}

		public NBTBase writeNBT() {
			NBTTagCompound c = new NBTTagCompound();
			c.setString("civilization", civilization.toString());
			c.setInteger("chunkX", chunkX);
			c.setInteger("chunkZ", chunkZ);
			return c;
		}

		public double chunkDistanceSq(int toChunkX, int toChunkZ) {
			double dx = (double) chunkX - toChunkX;
			double dz = (double) chunkZ - toChunkZ;
			return dx * dx + dz * dz;
		}
	}

	public static enum Civilization {
		EARTH, WIND, FIRE, MOON, SUN;
		public String getUnlocalizedName() {
			return "civilization." + this.toString().toLowerCase() + ".name";
		}

		public String getLocalizedName() {
			return I18n.format(getUnlocalizedName(), new Object[0]);
		}

		public String getFriendlyEnteringMessage() {
			return I18n.format("civilization.entering.friendly", getLocalizedName());
		}

		public String getNeutralEnteringMessage() {
			return I18n.format("civilization.entering.neutral", getLocalizedName());
		}

		public String getHostileEnteringMessage() {
			return I18n.format("civilization.entering.hostile", getLocalizedName());
		}

		public String getFriendlyLeavingMessage() {
			return I18n.format("civilization.leaving.friendly", getLocalizedName());
		}

		public String getNeutralLeavingMessage() {
			return I18n.format("civilization.leaving.neutral", getLocalizedName());
		}

		public String getHostileLeavingMessage() {
			return I18n.format("civilization.leaving.hostile", getLocalizedName());
		}
	};

}
