package net.torocraft.toroquest.generation.village.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
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
		DEFAULT_PALLETTE.put("Wa", Blocks.WATER.getDefaultState());
		DEFAULT_PALLETTE.put("Dr", Blocks.DIRT.getDefaultState());
		DEFAULT_PALLETTE.put("Sb", Blocks.STONEBRICK.getDefaultState());
		DEFAULT_PALLETTE.put("So", Blocks.STONE.getDefaultState());
		DEFAULT_PALLETTE.put("Pw", Blocks.PLANKS.getDefaultState());
		DEFAULT_PALLETTE.put("Fw", Blocks.OAK_FENCE.getDefaultState());
		DEFAULT_PALLETTE.put("Lg", Blocks.LOG.getDefaultState());
		DEFAULT_PALLETTE.put("l^", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("lv", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("l<", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("l>", Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
		DEFAULT_PALLETTE.put("Ss", Blocks.STONE_SLAB.getDefaultState());
		DEFAULT_PALLETTE.put("Gp", Blocks.GLASS_PANE.getDefaultState());
		DEFAULT_PALLETTE.put("Gs", Blocks.GLOWSTONE.getDefaultState());
		DEFAULT_PALLETTE.put("Sv", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("S^", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("S>", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("S<", Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));

		DEFAULT_PALLETTE.put("bv", Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("b^", Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("b>", Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("b<", Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.WEST));

		DEFAULT_PALLETTE.put("dv", Blocks.OAK_DOOR.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("d^", Blocks.OAK_DOOR.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("d>", Blocks.OAK_DOOR.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("d<", Blocks.OAK_DOOR.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.WEST));

		DEFAULT_PALLETTE.put("cv", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("c^", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("c>", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("c<", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST));

		DEFAULT_PALLETTE.put("tv", Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
		DEFAULT_PALLETTE.put("t^", Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
		DEFAULT_PALLETTE.put("t>", Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
		DEFAULT_PALLETTE.put("t<", Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
		DEFAULT_PALLETTE.put("t.", Blocks.TORCH.getDefaultState());

	}

	protected final String name;

	public VillagePieceBlockMap(String name, StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bounds, EnumFacing facing) {
		super(start, type);
		this.name = name;
		this.setCoordBaseMode(facing);
		this.boundingBox = bounds;
	}

	protected boolean specialBlockHandling(World world, String c, int x, int y, int z) {
		return false;
	}

	public boolean addComponentParts(final World world, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		if (averageGroundLvl < 0) {
			averageGroundLvl = this.getAverageGroundLevel(world, structureBoundingBoxIn);

			if (averageGroundLvl < 0) {
				return true;
			}
			boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + boundingBox.getYSize() - 1, 0);
		}

		Map<String, IBlockState> palette = getBiomeSpecificPalette();

		new BlockMapBuilder(name) {
			@Override
			protected void setBlockState(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.setBlockState(world, block, x, y, z, boundingBox);
			}

			@Override
			protected void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z) {
				VillagePieceBlockMap.this.replaceAirAndLiquidDownwards(world, block, x, y, z, boundingBox);
			}

			@Override
			protected boolean specialBlockHandling(String c, int x, int y, int z) {
				return VillagePieceBlockMap.this.specialBlockHandling(world, c, x, y, z);
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

	protected IBlockState getBiomeSpecificBlockState(IBlockState in) {
		in = super.getBiomeSpecificBlockState(in);
		if (in.getBlock() instanceof BlockDoor) {
			in = biomeSpecificDoor(in);
		}
		return in;
	}

	protected IBlockState biomeSpecificDoor(IBlockState in) {
		BlockDoor newBlock;
		switch (this.field_189928_h) {
		case 2:
			newBlock = Blocks.ACACIA_DOOR;
			break;
		case 3:
			newBlock = Blocks.SPRUCE_DOOR;
			break;
		default:
			newBlock = Blocks.OAK_DOOR;
			break;
		}
		return newBlock.getDefaultState().withProperty(BlockBed.FACING, in.getValue(BlockDoor.FACING));
	}

	protected abstract void alterPalette(Map<String, IBlockState> palette);

	protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
		return 1;
	}

}
