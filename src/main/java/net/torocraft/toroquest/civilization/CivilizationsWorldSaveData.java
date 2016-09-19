package net.torocraft.toroquest.civilization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

	private TreeMap<Integer, TreeMap<Integer, Province>> borders = new TreeMap<Integer, TreeMap<Integer, Province>>();

	public CivilizationsWorldSaveData() {
		super(DATA_NAME);
	}

	@Override
	public Province atLocation(final int chunkX, final int chunkZ) {


		Collection<Province> provinces = rangeQueryOnProvinces(chunkX, chunkZ);

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

	protected Collection<Province> rangeQueryOnProvinces(int chunkX, int chunkZ) {
		try {
			for (TreeMap<Integer, Province> zValues : borders.subMap(chunkX - RADIUS, chunkX + RADIUS).values()) {
				return zValues.subMap(chunkZ - RADIUS, chunkZ + RADIUS).values();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public Province register(int chunkX, int chunkZ) {
		
		Province nearbyProvice = atLocation(chunkX, chunkZ);
		
		Province province = new Province();
		province.chunkX = chunkX;
		province.chunkZ = chunkZ;
		
		if(nearbyProvice != null && nearbyProvice.civilization != null){
			province.civilization = nearbyProvice.civilization;
		}else {
			province.civilization = CivilizationType.values()[world.rand.nextInt(CivilizationType.values().length)];
		}
		
		addBorder(province);
		markDirty();
		return province;
	}

	private void addBorder(Province border) {
		System.out.println("Loading Civ Border X[" + border.chunkX + "] Z[" + border.chunkZ + "] CIV[" + border.civilization + "]");
		if (borders.get(border.chunkX) == null) {
			borders.put(border.chunkX, new TreeMap<Integer, Province>());
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
			Province border = new Province();
			border.readNBT(list.getCompoundTagAt(i));
			addBorder(border);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound t) {
		NBTTagList list = new NBTTagList();
		for (TreeMap<Integer, Province> sub : borders.values()) {
			for (Province border : sub.values()) {
				list.appendTag(border.writeNBT());
			}
		}
		t.setTag("borders", list);
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
