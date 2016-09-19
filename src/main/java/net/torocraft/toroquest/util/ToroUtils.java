package net.torocraft.toroquest.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ToroUtils {

	private static final float TEXTURE_HEIGHT_SCALER = 1F / 256F;
	private static final float TEXTURE_WIDTH_SCALER = 1F / 256F;

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + 0, y + height, 0.0D).tex((textureX + 0) * TEXTURE_HEIGHT_SCALER, (textureY + height) * TEXTURE_WIDTH_SCALER)
				.endVertex();
		;
		worldrenderer.pos(x + width, y + height, 0.0D).tex((textureX + width) * TEXTURE_HEIGHT_SCALER, (textureY + height) * TEXTURE_WIDTH_SCALER)
				.endVertex();
		worldrenderer.pos(x + width, y + 0, 0.0D).tex((textureX + width) * TEXTURE_HEIGHT_SCALER, (textureY + 0) * TEXTURE_WIDTH_SCALER).endVertex();
		worldrenderer.pos(x + 0, y + 0, 0.0D).tex((textureX + 0) * TEXTURE_HEIGHT_SCALER, (textureY + 0) * TEXTURE_WIDTH_SCALER).endVertex();
		tessellator.draw();
	}
}
