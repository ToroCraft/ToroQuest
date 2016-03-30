package net.torocraft.bouncermod.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.bouncermod.block.BounceModBlocks;
import net.torocraft.bouncermod.item.BounceModItems;

public class BounceModRecipes {
	
	public static final void init() {
		rubberManufacturing();
	}
	
	private static void rubberManufacturing() {
		GameRegistry.addSmelting(BounceModItems.rubberSapItem, new ItemStack(BounceModItems.rawRubberItem), .7F);
		GameRegistry.addRecipe(new ItemStack(BounceModBlocks.rubberBlock), "##", "##", '#', BounceModItems.rawRubberItem);
		GameRegistry.addRecipe(new ItemStack(BounceModItems.rubberSwordItem), " # ", " # ", " S ", '#', BounceModItems.rawRubberItem, 'S', Items.stick);
	}
}
