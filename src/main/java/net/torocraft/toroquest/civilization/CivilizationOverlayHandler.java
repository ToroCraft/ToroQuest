package net.torocraft.toroquest.civilization;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;
import net.torocraft.toroquest.util.ToroUtils;

public class CivilizationOverlayHandler {
	public static final ResourceLocation OVERLAY = new ResourceLocation("toroquest:textures/icons/civilization.png");

	private final Random random = new Random();
	private final Minecraft minecraft = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onPostRenderOverlay(RenderGameOverlayEvent.Pre event) {
		ScaledResolution resolution = event.getResolution();
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		if (event.getType() == ElementType.HEALTH) {

			if (minecraft.playerController.gameIsSurvivalOrAdventure()) {
				//drawCivilizationOverlay(width, height, null);
			}
		}
	}

	private void drawCivilizationOverlay(int width, int height, Civilization civ) {
		minecraft.getTextureManager().bindTexture(OVERLAY);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		int left = width / 2 - 5;
		int top = height / 2 + 40;
		int textureIndex = 1;

		ToroUtils.drawTexturedModalRect(left, top, textureIndex * 16, 0, 16, 16);

		minecraft.getTextureManager().bindTexture(Gui.ICONS);
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}

}
