package net.torocraft.toroquest.potion;

import net.minecraft.potion.Potion;
import net.torocraft.toroquest.ToroQuest;

public class PotionRoyal extends Potion {

	public static String NAME = "royalty";
	
	public static PotionRoyal INSTANCE = new PotionRoyal();

	//private final ResourceLocation iconTexture;
	
	public PotionRoyal() {
		super(false, 0x800080);
		//setPotionName(this, NAME);
		
		
		setRegistryName(ToroQuest.MODID, NAME);
		setPotionName("effect." + NAME);
		
		
		
		//iconTexture = new ResourceLocation(ToroQuest.MODID, "textures/potions/" + NAME + ".png");
	}

	

	@Override
	public boolean hasStatusIcon() {
		return true;
	}

	/*
	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		if (mc.currentScreen != null) {
			mc.getTextureManager().bindTexture(iconTexture);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		mc.getTextureManager().bindTexture(iconTexture);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
*/
}
