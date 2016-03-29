package net.torocraft.bouncermod.item;

public class ItemFlubberSword extends ItemRubberSword {
		
	public ItemFlubberSword(String unlocalizedName, ToolMaterial material) {
        super(unlocalizedName, material);
        this.setKnockback(2D);
	}

}
