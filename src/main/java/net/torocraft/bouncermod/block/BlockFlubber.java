package net.torocraft.bouncermod.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlubber extends BlockRubber {
	
	public static final String NAME = "flubberBlock";

	public BlockFlubber() {
		super();
		setUnlocalizedName(NAME);
		this.springiness = 1.25;
        this.slipperiness = 0.8F;
		this.translucent = true;
		this.isOpaqueCube(getDefaultState());
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
