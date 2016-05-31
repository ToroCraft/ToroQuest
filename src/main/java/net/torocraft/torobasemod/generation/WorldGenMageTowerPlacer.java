package net.torocraft.torobasemod.generation;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenMageTowerPlacer implements IWorldGenerator {

	public static void init() {
		GameRegistry.registerWorldGenerator(new WorldGenMageTowerPlacer(), 2);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0 && random.nextDouble() > 0.99) {
			BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getActualHeight(), chunkZ * 16 + random.nextInt(16));
			new MageTowerGenerator().generate(world, random, pos);
		}
	}

}
