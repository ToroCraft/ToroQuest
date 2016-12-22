package net.torocraft.toroquest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.torocraft.toroquest.inventory.VillageLordInventory;

public class VillageLordGuiHandler implements IGuiHandler {

	private static final int VILLAGE_LORD_GUI_ID = 70;
	public static int getGuiID() {return VILLAGE_LORD_GUI_ID;}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == VILLAGE_LORD_GUI_ID) {
			return new VillageLordContainer(player, new VillageLordInventory(), world);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == VILLAGE_LORD_GUI_ID) {
			return new VillageLordGuiContainer(player, new VillageLordInventory(), world);
		}
		return null;
	}
}
