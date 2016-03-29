package net.torocraft.bouncermod.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLaunch extends BlockRubber {
	
	public static final String NAME = "launchBlock";

	public BlockLaunch() {
		super();
		setUnlocalizedName(NAME);
		this.minBounceSpeed = 1.2D;
		this.maxBounceSpeed = 1.2D;

	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
