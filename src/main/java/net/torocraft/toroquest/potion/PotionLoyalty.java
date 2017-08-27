package net.torocraft.toroquest.potion;

import net.minecraft.potion.Potion;
import net.torocraft.toroquest.ToroQuest;

public class PotionLoyalty extends Potion {

	public static String NAME = "loyalty";
	
	public static final PotionLoyalty INSTANCE = new PotionLoyalty();

	//private final ResourceLocation iconTexture;
	
	public PotionLoyalty() {
		super(false, 0x661aff);
		setRegistryName(ToroQuest.MODID, NAME);
		setPotionName("effect." + getRegistryName().toString());
		//iconTexture = new ResourceLocation(ToroQuest.MODID, "textures/potions/" + NAME + ".png");
	}



	@Override
	public boolean hasStatusIcon() {
		return false;
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
