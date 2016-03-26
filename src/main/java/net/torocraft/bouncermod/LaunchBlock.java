package net.torocraft.bouncermod;

import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LaunchBlock extends BounceBlock {
	
	public static final String NAME = "launchBlock";

	public LaunchBlock() {
		super();
		setUnlocalizedName(NAME);
		this.minBounceSpeed = 1.2D;
		this.maxBounceSpeed = 1.2D;

	}
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
