package net.torocraft.toroquest.generation.village;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.torocraft.toroquest.generation.village.VillageHandlerKeep.VilleagePieceKeep;

public abstract class VillagePieceBlockMap extends StructureVillagePieces.Village {

	public static final Map<Character, IBlockState> DEFAULT_PALLETTE;
	static {
		DEFAULT_PALLETTE = new HashMap<Character, IBlockState>();
		DEFAULT_PALLETTE.put('-', Blocks.AIR.getDefaultState());
		DEFAULT_PALLETTE.put('C', Blocks.COBBLESTONE.getDefaultState());
		DEFAULT_PALLETTE.put('P', Blocks.PLANKS.getDefaultState());
		DEFAULT_PALLETTE.put('F', Blocks.OAK_FENCE.getDefaultState());
		DEFAULT_PALLETTE.put('L', Blocks.LOG.getDefaultState());
		DEFAULT_PALLETTE.put('G', Blocks.GLASS_PANE.getDefaultState());
		DEFAULT_PALLETTE.put('g', Blocks.GLOWSTONE.getDefaultState());
		DEFAULT_PALLETTE.put('N', Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put('S', Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put('E', Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put('W', Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
	}

	protected final String name;

	public VillagePieceBlockMap(String name, StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox p_i45571_4_,
			EnumFacing facing) {
		super(start, type);
		this.name = name;
		this.setCoordBaseMode(facing);
		this.boundingBox = p_i45571_4_;
	}

	public static VilleagePieceKeep createPiece(StructureVillagePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int x, int y,
			int z, EnumFacing facing, int p_175850_7_) {

		StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 9, 6, facing);

		return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(p_175850_1_, bounds) == null
				? new VilleagePieceKeep(start, p_175850_7_, rand, bounds, facing) : null;
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
			boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + 9 - 1, 0);
		}

		Map<Character, IBlockState> palette = getBiomeSpecificPalette();

		alterPalette(palette);

		BlockMapBuilder builder = new BlockMapBuilder(name) {
			@Override
			protected void setBlockState(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.setBlockState(worldIn, block, x, y, z, boundingBox);
			}

			@Override
			protected void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.replaceAirAndLiquidDownwards(worldIn, block, x, y, z, boundingBox);
			}
		};

		return true;
	}

	private Map<Character, IBlockState> getBiomeSpecificPalette() {

		Map<Character, IBlockState> palette = new HashMap<Character, IBlockState>();
		palette.putAll(DEFAULT_PALLETTE);

		for (Entry<Character, IBlockState> e : palette.entrySet()) {
			e.setValue(getBiomeSpecificBlockState(e.getValue()));
		}

		return palette;
	}

	protected abstract void alterPalette(Map<Character, IBlockState> palette);

	protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
		return 1;
	}

}
