package net.torocraft.toroquest.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.torocraft.toroquest.inventory.VillageLordInventory;

public class VillageLordGuiContainer extends GuiContainer {

	ResourceLocation texture = new ResourceLocation("toroquest", "textures/gui/villagelordgui.png");
	
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
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}