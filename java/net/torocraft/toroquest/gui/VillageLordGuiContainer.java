package net.torocraft.toroquest.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.torocraft.toroquest.inventory.VillageLordInventory;

public class VillageLordGuiContainer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("toroquest:textures/gui/lord_gui.png");
	
	public VillageLordGuiContainer() {
		this(null, null, null);
	}
	
	public VillageLordGuiContainer(EntityPlayer player, VillageLordInventory inventory, World world) {
		super(new VillageLordContainer(player, inventory, world));
		
		xSize = 175;
		ySize = 130;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(guiTexture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString("Village Lord", LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}
}