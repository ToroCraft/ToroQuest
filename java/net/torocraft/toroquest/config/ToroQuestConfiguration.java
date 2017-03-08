package net.torocraft.toroquest.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;

public class ToroQuestConfiguration {

	private static final String CATEGORY = "ToroQuest Settings";
	private static Configuration config;

	public static int structureSpawnChance = 1500;
	public static int structureMinDistance = 500;
	public static int structureMinDistanceBetweenSame = 4000;
	public static float toroHealthMultiplier = 1;
	public static float toroAttackDamageMultiplier = 1;
	public static boolean cropsAffectRep = true;
	public static boolean animalsAffectRep = true;
	public static boolean specificEntityNames = false;

	public static void init(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration() {
		try {
			structureSpawnChance = config.getInt("structureSpawnChance", CATEGORY, 1500, 100, 100000,
					"One out of X chance to spawn a special structure");

			structureMinDistance = config.getInt("structureMinDistance", CATEGORY, 500, 100, 100000,
					"The minimum distance allowed between to special structures");

			structureMinDistanceBetweenSame = config.getInt("structureMinDistanceBetweenSame", CATEGORY, 4000, 100, 100000,
					"The minimum distance allowed between to of the same special structures");

			toroHealthMultiplier = config.getFloat("toroHealthMultiplier", CATEGORY, 1, 0.01f, 100f, "Toro health multipler");

			toroAttackDamageMultiplier = config.getFloat("toroAttackDamageMultiplier", CATEGORY, 1, 0.01f, 100f, "Toro damage multipler");

			cropsAffectRep = config.getBoolean("cropsAffectRep", CATEGORY, true, "Gain rep when planting crops and lose it when harvesting");

			animalsAffectRep = config.getBoolean("animalsAffectRep", CATEGORY, true, "Gain rep when breeding animals and lose it when killing them");

			specificEntityNames = config.getBoolean("specificEntityNames", CATEGORY, false,
					"Use specific entity names to improve mod compatabilty, SETTING THIS WILL REMOVE CURRENT TQ ENTITIES IN YOUR WORLD!");

			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(ToroQuest.MODID)) {
			loadConfiguration();
		}
	}
}
