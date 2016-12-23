package net.torocraft.toroquest.gui;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.torocraft.toroquest.inventory.VillageLordInventory;

public class VillageLordGuiContainer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("toroquest:textures/gui/lord_gui.png");
	
	private static final int buttonWidth = 59;
	private static final int buttonHeight = 19;
	
	private static final int MOUSE_COOLDOWN = 200;
	private static long mousePressed = 0;
	
	private static int availableReputation = 0;
	
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
		
		if(hasReputation()) {
			drawSubmitButton(mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString("Village Lord", LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
		drawReputationDisplay(LABEL_XPOS, LABEL_YPOS);
	}
	
	private void drawSubmitButton(int mouseX, int mouseY) {
		GuiButton submitButton = new GuiButton(0, guiLeft + 73, guiTop + 15, buttonWidth, buttonHeight, "Donate");
		submitButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (submitButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				availableReputation = 10;
			}
		}
	}
	
	private void drawReputationDisplay(int xPos, int yPos) {
		fontRendererObj.drawString(String.valueOf(availableReputation) + " Reputation", xPos + 50, yPos + 32, Color.darkGray.getRGB());
	}
	
	public static void setAvailableReputation(int rep) {
		availableReputation = rep;
	}
	
	private boolean hasReputation() {
		if(availableReputation > 0) {
			return true;
		}
		return false;
	}
	
	private boolean mouseCooldownOver() {
		return Minecraft.getSystemTime() - mousePressed > MOUSE_COOLDOWN;
	}
}