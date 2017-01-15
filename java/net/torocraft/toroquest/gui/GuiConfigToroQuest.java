package net.torocraft.toroquest.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.configuration.ConfigurationHandler;

public class GuiConfigToroQuest extends GuiConfig {

	public GuiConfigToroQuest(GuiScreen parent) {
		super (parent, new ConfigElement(ConfigurationHandler.config.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
				ToroQuest.MODID,
				false,
				false,
				"ToroQuest");
		titleLine2 = ConfigurationHandler.config.getConfigFile().getAbsolutePath();
	}
}
