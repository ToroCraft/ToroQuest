package net.torocraft.toroquest.civilization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.util.ToroGuiUtils;

public class CivilizationOverlayHandler {

	private final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onPostRenderOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != ElementType.HOTBAR) {
			return;
		}
		ScaledResolution resolution = event.getResolution();
		drawCivilizationOverlay(resolution.getScaledWidth(), resolution.getScaledHeight());
	}

	private void drawCivilizationOverlay(int width, int height) {
		int left = width / 2 - 8;
		int top = height - 48;

		EntityPlayerSP player = mc.thePlayer;
		Province civ = PlayerCivilizationCapabilityImpl.get(player).getPlayerInCivilization();

		if (civ == null || civ.civilization == null) {
			return;
		}

		drawCurrentCivilizationIcon(left, top, civ);
	}

	private void drawCurrentCivilizationIcon(int left, int top, Province civ) {
		ToroGuiUtils.drawOverlayIcon(mc, left, top + 4, 0, 1);
		ToroGuiUtils.drawOverlayIcon(mc, left, top, iconIndex(civ.civilization), 0);
	}

	private int iconIndex(CivilizationType civ) {
		switch (civ) {
		case EARTH:
			return 0;
		case FIRE:
			return 1;
		case MOON:
			return 2;
		case SUN:
			return 3;
		case WATER:
			return 4;
		case WIND:
			return 5;
		default:
			return 0;
		}
	}

}
