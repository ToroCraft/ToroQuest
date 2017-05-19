package net.torocraft.toroquest.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiVillageLordSettle extends GuiScreen {

	private GuiButton buttonSettle;
	private GuiButton buttonCivilization;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void initGui() {

		int wCenter = width / 2;
		int hCenter = height / 2;

		int x = wCenter -100;
		int y = hCenter -24;

		buttonCivilization = new GuiButton(0, x, y, "Fire");
		buttonSettle = new GuiButton(1, x, y + 20, "Settle");
		
		buttonList.add(buttonSettle);
		buttonList.add(buttonCivilization);

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button ==  buttonSettle) {
			System.out.println("Settle Button");
			return;
		}
		
		if (button == buttonCivilization) {
			System.out.println("Civ Button");
			return;
		}
	}

	private void closeGui() {
		this.mc.displayGuiScreen(null);
		if (this.mc.currentScreen == null)
			this.mc.setIngameFocus();
	}

}
