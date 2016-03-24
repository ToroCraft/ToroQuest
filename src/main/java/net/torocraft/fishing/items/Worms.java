package net.torocraft.fishing.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Worms extends Item {
	
	public static String NAME = "worms";

	public Worms() {
		this(64, CreativeTabs.tabMisc, NAME);
	}
	
	public Worms(int maxStackSize, CreativeTabs tab, String name) {
		setMaxStackSize(maxStackSize);
		setCreativeTab(tab);
		setUnlocalizedName(name);
	}
}
