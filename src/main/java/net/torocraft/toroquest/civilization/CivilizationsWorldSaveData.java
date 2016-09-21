package net.torocraft.toroquest.civilization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.toroquest.ToroQuest;

public class CivilizationsWorldSaveData extends WorldSavedData implements CivilizationDataAccessor {

	private static final String DATA_NAME = ToroQuest.MODID + "_civilizations";
	private static final int RADIUS = 12;
	public World world;

	private TreeMap<Integer, TreeMap<Integer, Province>> provincesTreeMap = new TreeMap<Integer, TreeMap<Integer, Province>>();
	private List<Province> provinces = new ArrayList<Province>();

	public CivilizationsWorldSaveData() {
		super(DATA_NAME);
	}

	public CivilizationsWorldSaveData(String name) {
		super(name);
	}

	@Override
	public Province atLocation(final int chunkX, final int chunkZ) {

		Collection<Province> provinces = scan(chunkX, chunkZ);

		if (provinces == null || provinces.size() < 1) {
			return null;
		}

		if (provinces.size() == 1) {
			for (Province border : provinces) {
				return border;
			}
		}

		List<Province> list = new ArrayList<Province>(provinces);

		Collections.sort(list, new Comparator<Province>() {
			@Override
			public int compare(Province a, Province b) {
				double d0 = chunkDistanceSq(chunkX, chunkZ, a.chunkX, a.chunkZ);
				double d1 = chunkDistanceSq(chunkX, chunkZ, b.chunkX, b.chunkZ);
				return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
			}
		});

		return list.get(0);
	}

	public int chunkDistanceSq(int aX, int aZ, int bX, int bZ) {
		int x = aX - bX;
		int z = aZ - bZ;
		return x * x + z * z;
	}

	private Collection<Province> scan(int chunkX, int chunkZ) {
		// return rangeQueryOnProvinces(chunkX, chunkZ);
		return sequentialScan(chunkX, chunkZ);
	}

	private Collection<Province> sequentialScan(int chunkX, int chunkZ) {

		int lowerX = chunkX - RADIUS;
		int upperX = chunkX + RADIUS;
		int lowerZ = chunkZ - RADIUS;
		int upperZ = chunkZ + RADIUS;

		List<Province> subset = new ArrayList<Province>();

		for (Province p : provinces) {
			if (p.chunkX >= lowerX && p.chunkX <= upperX && p.chunkZ >= lowerZ && p.chunkZ <= upperZ) {
				subset.add(p);
			}
		}

		return subset;
	}

	protected Collection<Province> rangeQueryOnProvinces(int chunkX, int chunkZ) {
		try {
			for (TreeMap<Integer, Province> zValues : provincesTreeMap.subMap(chunkX - RADIUS, chunkX + RADIUS).values()) {
				return zValues.subMap(chunkZ - RADIUS, chunkZ + RADIUS).values();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public synchronized Province register(int chunkX, int chunkZ) {
		// System.out.println("register [" + chunkX + "][" + chunkZ + "]");
		Province province = atLocation(chunkX, chunkZ);

		if (province != null) {
			updateExistingProvince(province, chunkX, chunkZ);
		} else {
			province = buildNewProvince(chunkX, chunkZ);
		}
		markDirty();
		return province;
	}

	protected Province buildNewProvince(int chunkX, int chunkZ) {
		Province province;
		province = new Province();
		province.chunkX = chunkX;
		province.chunkZ = chunkZ;
		province.civilization = randomCivilizationType();

		province.lowerVillageBoundX = chunkX;
		province.upperVillageBoundX = chunkX;
		province.lowerVillageBoundZ = chunkZ;
		province.upperVillageBoundZ = chunkZ;
		province.computeSize();

		addProvinceToSaveData(province);

		return province;
	}

	protected CivilizationType randomCivilizationType() {
		Random rand = new Random();
		return CivilizationType.values()[rand.nextInt(CivilizationType.values().length)];
	}

	private synchronized void updateExistingProvince(Province province, int chunkX, int chunkZ) {
		province.addToBoundsAndRecenter(chunkX, chunkZ);
		// System.out.println("UPDATED newX[" + chunkX + "] newZ[" + chunkZ + "]
		// Province X[" + province.chunkX + "] Z[" + province.chunkZ + "] CIV["
		// + province.civilization + "] AREA[" + province.area + "] x[" +
		// province.xLength + "] z["
		// + province.zLength + "]");
	}

	private synchronized void addProvinceToSaveData(Province province) {
		// System.out.println("NEW Province X[" + province.chunkX + "] Z[" +
		// province.chunkZ + "] CIV[" + province.civilization + "] AREA[" +
		// province.area + "] x[" + province.xLength + "] z[" + province.zLength
		// + "]");
		provinces.add(province);
		addProvinceToTreeMap(province);
	}

	protected void addProvinceToTreeMap(Province border) {
		if (provincesTreeMap.get(border.chunkX) == null) {
			provincesTreeMap.put(border.chunkX, new TreeMap<Integer, Province>());
		}
		provincesTreeMap.get(border.chunkX).put(border.chunkZ, border);
	}

	@Override
	public void readFromNBT(NBTTagCompound t) {
		NBTTagList list;
		try {
			list = (NBTTagList) t.getTag("provinces");
		} catch (Exception e) {
			list = new NBTTagList();
		}
		for (int i = 0; i < list.tagCount(); i++) {
			Province province = new Province();
			province.readNBT(list.getCompoundTagAt(i));
			addProvinceToSaveData(province);
		}
		System.out.println("*** Loaded [" + provinces.size() + "] Provinces");
		for (Province p : provinces) {
			System.out.println(p.toString());
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound t) {

		System.out.println("*** Saving [" + provinces.size() + "] Provinces");
		for (Province p : provinces) {
			System.out.println(p.toString());
		}

		NBTTagList list = new NBTTagList();
		for (Province p : provinces) {
			list.appendTag(p.writeNBT());
		}
		t.setTag("provinces", list);
		return t;
	}

	public static CivilizationDataAccessor get(World world) {
		MapStorage storage = world.getMapStorage();
		CivilizationsWorldSaveData instance = (CivilizationsWorldSaveData) storage.getOrLoadData(CivilizationsWorldSaveData.class, DATA_NAME);
		if (instance == null) {
			instance = new CivilizationsWorldSaveData();
			storage.setData(DATA_NAME, instance);
		}
		instance.world = world;
		return instance;
	}

}
