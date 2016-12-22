package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData;

public class WorldGenPlacer implements IWorldGenerator {

	public static void init() {
		GameRegistry.registerWorldGenerator(new WorldGenPlacer(), 2);
	}

	public static final int LOW_CHANCE = 2000;
	public static final int MID_CHANCE = 800;
	public static final int HIGH_CHANCE = 100;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (random.nextInt(LOW_CHANCE) != 0 || world.provider.getDimension() != 0) {
			return;
		}

		int roll = random.nextInt(4);

		switch (roll) {
		case 0:
			genMonolith(world, random, chunkX, chunkZ);
			break;
		case 1:
			genBastionsLair(world, random, chunkX, chunkZ);
			break;
		case 2:
			genMageTower(world, random, chunkX, chunkZ);
			break;
		case 3:
			genThroneRoom(world, random, chunkX, chunkZ);
			break;
		}
	}

	private void genMageTower(World world, Random random, int chunkX, int chunkZ) {
		if (!CivilizationsWorldSaveData.get(world).canGenStructure("mage_tower", chunkX, chunkZ)) {
			return;
		}

		BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getActualHeight(), chunkZ * 16 + random.nextInt(16));
		if (new MageTowerGenerator().generate(world, random, pos)) {
			System.out.println("ToroQuest Gen Placer: Mage Tower " + pos);
		}
	}

	private void genBastionsLair(World world, Random random, int chunkX, int chunkZ) {
		if (!CivilizationsWorldSaveData.get(world).canGenStructure("bastions_lair", chunkX, chunkZ)) {
			return;
		}
		BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getActualHeight(), chunkZ * 16 + random.nextInt(16));
		if (new BastionsLairGenerator().generate(world, random, pos)) {
			System.out.println("ToroQuest Gen Placer: Bastion's Lair " + pos);
		}
	}

	private void genMonolith(World world, Random random, int chunkX, int chunkZ) {
		if (!CivilizationsWorldSaveData.get(world).canGenStructure("monolith", chunkX, chunkZ)) {
			return;
		}
		BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getActualHeight(), chunkZ * 16 + random.nextInt(16));
		if (new MonolithGenerator().generate(world, random, pos)) {
			System.out.println("ToroQuest Gen Placer: Monolith " + pos);
		}
	}
	
	private void genThroneRoom(World world, Random random, int chunkX, int chunkZ) {
		if (!CivilizationsWorldSaveData.get(world).canGenStructure("throne_room", chunkX, chunkZ)) {
			return;
		}
		int y = random.nextInt(20) + 10;
		BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), y, chunkZ * 16 + random.nextInt(16));
		if (new ThroneRoomGenerator().generate(world, random, pos)) {
			System.out.println("ToroQuest Gen Placer: Throne Room " + pos);
		}
	}

}
