package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenBastionsLairPlacer implements IWorldGenerator {

	public static void init() {
		GameRegistry.registerWorldGenerator(new WorldGenBastionsLairPlacer(), 2);
	}

	public static final double LOW_CHANCE = 0.9999;
	public static final double HIGH_CHANCE = 0.7;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0 && chanceToSpawnStructure(chunkX, chunkZ, world, random)) {
			BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getActualHeight(), chunkZ * 16 + random.nextInt(16));
			new BastionsLairGenerator().generate(world, random, pos);
		}
	}

	private boolean chanceToSpawnStructure(int chunkX, int chunkZ, World world, Random random) {
		int i = chunkX >> 4;
        int j = chunkZ >> 4;
        random.setSeed((long)(i ^ j << 4) ^ world.getSeed());
        random.nextInt();
        return random.nextInt(3) != 0 ? false : (chunkX != (i << 4) + 4 + random.nextInt(8) ? false : chunkZ == (j << 4) + 4 + random.nextInt(8));
	}
	
	private boolean chanceToSpawnStructureOld(Random random) {
		return random.nextDouble() > LOW_CHANCE;
	}

}
