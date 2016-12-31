package net.torocraft.toroquest.gui;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.QuestDelegator;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageQuestUpdate;

public class VillageLordGuiContainer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("toroquest:textures/gui/lord_gui.png");
	
	private static final int buttonWidth = 59;
	private static final int buttonHeight = 19;
	
	private static final int MOUSE_COOLDOWN = 200;
	private static long mousePressed = 0;
	
	private static int availableReputation = 100;
	
	private static String civName = "";
	private static String questTitle = "";
	private static String questDescription = "";
	private static boolean questAccepted = false;
	
	private QuestDelegator quest = new QuestDelegator(new QuestData());
	
	public VillageLordGuiContainer() {
		this(null, null, null);
	}
	
	public VillageLordGuiContainer(EntityPlayer player, IInventory inventory, World world) {
		super(new VillageLordContainer(player, inventory, world));
		xSize = 176;
		ySize = 239;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(guiTexture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		//if(hasReputation()) {
			drawDonateButton(mouseX, mouseY);
		//}
			
		if(questAccepted) {
			drawAbandonButton(mouseX, mouseY);
		} else {
			drawAcceptButton(mouseX, mouseY);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		drawGuiTitle(LABEL_XPOS, LABEL_YPOS);
		drawReputationDisplay(LABEL_XPOS, LABEL_YPOS);
		drawQuestTitle(LABEL_XPOS, LABEL_YPOS);
	}
	
	private void drawGuiTitle(int xPos, int yPos) {
		fontRendererObj.drawString("Lord of " + this.civName, xPos, yPos, Color.darkGray.getRGB());
	}
	
	private void drawDonateButton(int mouseX, int mouseY) {
		GuiButton submitButton = new GuiButton(0, guiLeft + 105, guiTop + 15, buttonWidth, buttonHeight, "Donate");
		submitButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (submitButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				availableReputation = 10;
			}
		}
	}
	
	private void drawAcceptButton(int mouseX, int mouseY) {
		GuiButton acceptButton = new GuiButton(0, guiLeft + 105, guiTop + 130, buttonWidth, buttonHeight, "Accept");
		acceptButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (acceptButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				ToroQuestPacketHandler.INSTANCE.sendToServer(new MessageQuestUpdate());
			}
		}
	}
	
	private void drawAbandonButton(int mouseX, int mouseY) {
		GuiButton abandonButton = new GuiButton(0, guiLeft + 105, guiTop + 130, buttonWidth, buttonHeight, "Abandon");
		abandonButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (abandonButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				ToroQuestPacketHandler.INSTANCE.sendToServer(new MessageQuestUpdate());
			}
		}
	}
	
	private void drawReputationDisplay(int xPos, int yPos) {
		fontRendererObj.drawString(String.valueOf(availableReputation) + " Rep. for", xPos + 13, yPos + 15, Color.darkGray.getRGB());
	}
	
	private void drawQuestTitle(int xPos, int yPos) {		
		fontRendererObj.drawString(questTitle, xPos + 2, yPos + 35, Color.darkGray.getRGB());
		fontRendererObj.drawSplitString(questDescription, xPos + 25, yPos + 50, 115, Color.darkGray.getRGB());
	}
	
	public static void setProvinceName(String name) {
		civName = name;
	}
	
	public static void setAvailableReputation(int rep) {
		availableReputation = rep;
	}

	public static void setQuestData(String title, String description, boolean accepted) {
		questTitle = title;
		questDescription = description;
		questAccepted = accepted;
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