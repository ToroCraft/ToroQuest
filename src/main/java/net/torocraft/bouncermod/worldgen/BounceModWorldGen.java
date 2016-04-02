package net.torocraft.bouncermod.worldgen;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.bouncermod.BouncerMod;

public class BounceModWorldGen {

	private final static String MODID = BouncerMod.MODID;
	
	public static void init() {
		initRubberTreeWorldGen();
	}
	
	private static void initRubberTreeWorldGen() {
		GameRegistry.registerWorldGenerator(new WorldGenRubberTreePlacer(),2);
	}
	
}
