package com.frodare.fishing.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Worm extends Item {

	public Worm() {
		this(64, CreativeTabs.tabMisc, "fishingWorm");
	}
	
	public Worm(int maxStackSize, CreativeTabs tab, String name) {
		setMaxStackSize(maxStackSize);
		setCreativeTab(tab);
		setUnlocalizedName(name);
	}
}
