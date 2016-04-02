package net.torocraft.bouncermod.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraft.util.math.BlockPos;

public class WorldGenRubberTreePlacer implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);

		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16);

		}

	}

	private void generateSurface(World world, Random random, int BlockX, int BlockZ) {

		for (int i = 0; i < 20; i++) {
			int Xcoord1 = BlockX + random.nextInt(16);
			int Ycoord1 = random.nextInt(90);
			int Zcoord1 = BlockZ + random.nextInt(16);

			(new WorldGenRubberTree(false, false)).generate(world, random, new BlockPos(Xcoord1, Ycoord1, Zcoord1));
		}
	}

	private void generateNether(World world, Random random, int i, int j) {

	}

}