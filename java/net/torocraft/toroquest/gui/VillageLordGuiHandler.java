package net.torocraft.toroquest.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.torocraft.toroquest.entities.EntityVillageLord;
import net.torocraft.toroquest.inventory.IVillageLordInventory;

public class VillageLordGuiHandler implements IGuiHandler {

	private static final int VILLAGE_LORD_GUI_ID = 70;

	public static int getGuiID() {
		return VILLAGE_LORD_GUI_ID;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == VILLAGE_LORD_GUI_ID) {
			return new VillageLordContainer(player, getVillageLordInventory(world, player, x, y, z), world);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == VILLAGE_LORD_GUI_ID) {
			return new VillageLordGuiContainer(player, getVillageLordInventory(world, player, x, y, z), world);
		}
		return null;
	}
	
	
	private IVillageLordInventory getVillageLordInventory(World world, EntityPlayer player, int x, int y, int z) {
		EntityVillageLord lord = getVillageLord(world, x, y, z);
		if(lord == null){
			return null;
		}
		return lord.getInventory(player.getUniqueID());
	}
	

	public static EntityVillageLord getVillageLord(World world, int x, int y, int z) {
		// TODO is this really the best way to get the village lord reference,
		// what if there is another close by?
		//
		// maybe start with a search box of 1x1x1 and expand if none found

		// maybe take the last clicked on lord in the player cap

		List<EntityVillageLord> lords;
		for (int i = 1; i < 5; i++) {
			lords = world.getEntitiesWithinAABB(EntityVillageLord.class, new AxisAlignedBB(new BlockPos(x, y, z)).expand(i, i, i));
			if (lords != null && lords.size() > 0) {
				return lords.get(0);
			}
		}

		throw new NullPointerException("village lord not found");
	}
}
