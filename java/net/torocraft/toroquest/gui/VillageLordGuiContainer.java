package net.torocraft.toroquest.gui;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.torocraft.toroquest.inventory.IVillageLordInventory;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageQuestUpdate;
import net.torocraft.toroquest.network.message.MessageQuestUpdate.Action;
import net.torocraft.toroquest.network.message.MessageSetItemReputationAmount;
import net.torocraft.toroquest.network.message.MessageSetItemReputationAmount.MessageCode;

public class VillageLordGuiContainer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("toroquest:textures/gui/lord_gui.png");

	private static final int buttonWidth = 59;
	private static final int buttonHeight = 19;

	private static final int MOUSE_COOLDOWN = 200;
	private static long mousePressed = 0;

	private static int donateRepForItem = 0;
	private static MessageCode donateMessageCode = MessageCode.EMPTY;

	private static String civName = "";
	private static String questTitle = "";
	private static String questDescription = "";
	private static boolean questAccepted = false;

	public VillageLordGuiContainer() {
		this(null, null, null);
	}

	public VillageLordGuiContainer(EntityPlayer player, IVillageLordInventory inventory, World world) {
		super(new VillageLordContainer(player, inventory, world));
		xSize = 176;
		ySize = 239;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(guiTexture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawDonateButton(mouseX, mouseY);

		if (questAccepted) {
			drawAbandonButton(mouseX, mouseY);
			drawCompleteButton(mouseX, mouseY);
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
		updateReputationDisplay(LABEL_XPOS, LABEL_YPOS);
		drawQuestTitle(LABEL_XPOS, LABEL_YPOS);
	}

	private void drawGuiTitle(int xPos, int yPos) {
		fontRendererObj.drawString(I18n.format("quest.gui.title", VillageLordGuiContainer.civName), xPos, yPos, Color.darkGray.getRGB());
	}

	private void drawDonateButton(int mouseX, int mouseY) {
		GuiButton submitButton = new GuiButton(0, guiLeft + 105, guiTop + 15, buttonWidth, buttonHeight, I18n.format("quest.gui.button.donate"));
		submitButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (submitButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				MessageQuestUpdate message = new MessageQuestUpdate();
				message.action = Action.DONATE;
				ToroQuestPacketHandler.INSTANCE.sendToServer(message);
			}
		}
	}

	private void drawAcceptButton(int mouseX, int mouseY) {
		drawActionButton("quest.gui.button.accept", Action.ACCEPT, mouseX, mouseY, 0);
	}

	private void drawCompleteButton(int mouseX, int mouseY) {
		drawActionButton("quest.gui.button.complete", Action.COMPLETE, mouseX, mouseY, 0);
	}

	private void drawAbandonButton(int mouseX, int mouseY) {
		drawActionButton("quest.gui.button.reject", Action.REJECT, mouseX, mouseY, -70);
	}

	protected void drawActionButton(String label, Action action, int mouseX, int mouseY, int xOffset) {
		GuiButton abandonButton = new GuiButton(0, guiLeft + 105 + xOffset, guiTop + 130, buttonWidth, buttonHeight, I18n.format(label));
		abandonButton.drawButton(mc, mouseX, mouseY);
		if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
			if (abandonButton.mousePressed(mc, mouseX, mouseY) && mouseCooldownOver()) {
				mousePressed = Minecraft.getSystemTime();
				MessageQuestUpdate message = new MessageQuestUpdate();
				message.action = action;
				ToroQuestPacketHandler.INSTANCE.sendToServer(message);
			}
		}
	}

	private void updateReputationDisplay(int xPos, int yPos) {
		if (MessageCode.DONATION.equals(donateMessageCode)) {
			fontRendererObj.drawString(I18n.format("quest.gui.rep_for", donateRepForItem), xPos + 13, yPos + 15, Color.darkGray.getRGB());

		} else if (MessageCode.NOTE.equals(donateMessageCode)) {
			fontRendererObj.drawString(I18n.format("quest.gui.reply"), xPos + 13, yPos + 15, Color.darkGray.getRGB());

		} else if (MessageCode.STOLEN_ITEM.equals(donateMessageCode)) {
			fontRendererObj.drawString(I18n.format("quest.gui.stolen_item"), xPos + 13, yPos + 15, Color.darkGray.getRGB());

		} else {
			fontRendererObj.drawString(I18n.format("quest.gui.empty"), xPos + 13, yPos + 15, Color.darkGray.getRGB());

		}
	}

	private void drawQuestTitle(int xPos, int yPos) {
		fontRendererObj.drawString(questTitle, xPos + 2, yPos + 35, Color.darkGray.getRGB());
		fontRendererObj.drawSplitString(questDescription, xPos + 25, yPos + 50, 115, Color.darkGray.getRGB());
	}

	private static String translate(String in) {
		if (in == null || in.trim().length() < 1) {
			return "";
		}
		String[] parts = in.split("\\|");
		if (parts.length == 1) {
			return I18n.format(parts[0]);
		}
		Object[] parameters = Arrays.copyOfRange(parts, 1, parts.length);

		processComplexParamters(parameters);

		return I18n.format(parts[0], parameters);
	}

	private static void processComplexParamters(Object[] parameters) {
		Object o;
		for (int i = 0; i < parameters.length; i++) {
			o = parameters[i];
			if (o != null && o instanceof String && o.toString().startsWith("L:")) {
				parameters[i] = processListParamter(o.toString());
			} else if (o != null && o instanceof String && o.toString().startsWith("D:")) {
				parameters[i] = processDirectionsParamter(o.toString());
			}
		}
	}

	private static Object processDirectionsParamter(String s) {
		String[] parts = s.substring(2).split(";");

		if (parts.length != 3) {
			return s;
		}

		return I18n.format("quest.directions", parts[0], parts[1], parts[2]);
	}

	private static String processListParamter(String s) {
		StringBuilder sb = new StringBuilder();
		String[] sStacks = s.substring(2).split(";");

		boolean isFirst = true;

		for (String sStack : sStacks) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
			}

			String[] sStackParts = sStack.split(",");

			if (sStackParts.length == 2) {
				sb.append(I18n.format(sStackParts[0] + ".name"));
				sb.append("[").append(sStackParts[1]).append("]");
			} else {
				sb.append(sStackParts);
			}
		}

		return sb.toString();
	}

	public static void setProvinceName(String name) {
		civName = name;
	}

	public static void setDonateInfo(MessageSetItemReputationAmount message) {
		donateRepForItem = message.reputation;
		donateMessageCode = message.messageCode;
	}

	public static void setQuestData(String title, String description, boolean accepted) {
		questTitle = translate(title);
		questDescription = translate(description);
		questAccepted = accepted;
	}

	private boolean mouseCooldownOver() {
		return Minecraft.getSystemTime() - mousePressed > MOUSE_COOLDOWN;
	}
}