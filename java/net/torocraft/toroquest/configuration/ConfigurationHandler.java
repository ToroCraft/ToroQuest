package net.torocraft.toroquest.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;

public class ConfigurationHandler {

	public static String repDisplayPosition;
	public static Integer repDisplayX;
	public static Integer repDisplayY;
	
	public static Configuration config;
	
	public static void init(File configFile) {
		config = new Configuration(configFile);
		loadConfiguration();
	}
	
	public static void loadConfiguration() {
		try {
			repDisplayPosition = config.getString("Rep Badge Position", Configuration.CATEGORY_CLIENT, "BOTTOM RIGHT", "Location of Rep Badge", new String[] { "TOP LEFT", "TOP CENTER", "TOP RIGHT", "BOTTOM LEFT", "BOTTOM RIGHT", "CUSTOM" });
			repDisplayX = config.getInt("Rep Badge X", Configuration.CATEGORY_CLIENT, 0, -20000, 20000, "With CUSTOM position, sets X position of Rep Badge");
			repDisplayY = config.getInt("Rep Badge Y", Configuration.CATEGORY_CLIENT, 0, -20000, 20000, "With CUSTOM position, sets Y position of Rep Badge");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
	
	@SubscribeEvent
	public void onConfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(ToroQuest.MODID)) {
			loadConfiguration();
		}
	}
	
}
