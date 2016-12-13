package net.torocraft.toroquest.civilization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.util.Hud;
import net.torocraft.toroquest.util.ToroGuiUtils;

public class CivilizationOverlayHandler extends Hud {

	public CivilizationOverlayHandler(Minecraft mc) {
		super(mc, 20, 10);
	}


	@Override
	public void render(int screenWidth, int screenHeight) {

		int left = screenWidth - 75;

		int top = screenHeight - 25;


		EntityPlayerSP player = mc.thePlayer;
		
		if (player.dimension != 0) {
			return;
		}
		
		Province civ = PlayerCivilizationCapabilityImpl.get(player).getPlayerInCivilization();

		if (civ == null || civ.civilization == null) {
			return;
		}

		drawCurrentCivilizationIcon(left, top, civ, player);
	}

	private void drawCurrentCivilizationIcon(int left, int top, Province civ, EntityPlayerSP player) {

		drawString(Integer.toString(PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ.civilization), 10), left + 20, top + 5, 0xffffff);
		drawString(s(PlayerCivilizationCapabilityImpl.get(player).getReputationLevel(civ.civilization)), left + 18, top + 15, 0xffffff);

		ToroGuiUtils.drawOverlayIcon(mc, left - 2, top - 3, 0, 96, 20, 27);
		ToroGuiUtils.drawOverlayIcon(mc, left, top, iconIndex(civ.civilization), 0);
	}

	private String s(ReputationLevel reputationLevel) {
		try {
			return reputationLevel.toString();
		} catch (Exception e) {
			return "";
		}
	}

	private int iconIndex(CivilizationType civ) {
		switch (civ) {
		case EARTH:
			return 0;
		case FIRE:
			return 5;
		case MOON:
			return 4;
		case SUN:
			return 1;
		case WATER:
			return 2;
		case WIND:
			return 3;
		default:
			return 0;
		}
	}


}
