package net.torocraft.toroquest.generation.village.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public abstract class VillagePieceBlockMap extends StructureVillagePieces.Village {

	public static final Map<String, IBlockState> DEFAULT_PALLETTE;
	static {
		DEFAULT_PALLETTE = new HashMap<String, IBlockState>();
		DEFAULT_PALLETTE.put("--", Blocks.AIR.getDefaultState());
		DEFAULT_PALLETTE.put("Cs", Blocks.COBBLESTONE.getDefaultState());
		DEFAULT_PALLETTE.put("Dr", Blocks.DIRT.getDefaultState());
		DEFAULT_PALLETTE.put("Sb", Blocks.STONEBRICK.getDefaultState());
		DEFAULT_PALLETTE.put("So", Blocks.STONE.getDefaultState());
		DEFAULT_PALLETTE.put("Pw", Blocks.PLANKS.getDefaultState());
		DEFAULT_PALLETTE.put("Fw", Blocks.OAK_FENCE.getDefaultState());
		DEFAULT_PALLETTE.put("Lg", Blocks.LOG.getDefaultState());
		DEFAULT_PALLETTE.put("l<", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("l^", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("l<", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("l>", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
		DEFAULT_PALLETTE.put("Ss", Blocks.STONE_SLAB.getDefaultState());
		DEFAULT_PALLETTE.put("Gp", Blocks.GLASS_PANE.getDefaultState());
		DEFAULT_PALLETTE.put("Gs", Blocks.GLOWSTONE.getDefaultState());
		DEFAULT_PALLETTE.put("Sv", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("S^", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("S>", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("S<", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
	}

	protected final String name;

	public VillagePieceBlockMap(String name, StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bounds, EnumFacing facing) {
		super(start, type);
		this.name = name;
		this.setCoordBaseMode(facing);
		this.boundingBox = bounds;
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs,
	 * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
	 */

	public boolean addComponentParts(final World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		if (averageGroundLvl < 0) {
			averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

			if (averageGroundLvl < 0) {
				return true;
			}
			boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + boundingBox.getYSize() - 1, 0);
		}

		Map<String, IBlockState> palette = getBiomeSpecificPalette();

		new BlockMapBuilder(name) {
			@Override
			protected void setBlockState(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.setBlockState(worldIn, block, x, y, z, boundingBox);
			}

			@Override
			protected void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.replaceAirAndLiquidDownwards(worldIn, block, x, y, z, boundingBox);
			}
		}.build(palette);

		return true;
	}

	private Map<String, IBlockState> getBiomeSpecificPalette() {

		Map<String, IBlockState> palette = new HashMap<String, IBlockState>();
		palette.putAll(DEFAULT_PALLETTE);

		alterPalette(palette);

		for (Entry<String, IBlockState> e : palette.entrySet()) {
			e.setValue(getBiomeSpecificBlockState(e.getValue()));
		}

		return palette;
	}

	protected abstract void alterPalette(Map<String, IBlockState> palette);

	protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
		return 1;
	}

}
