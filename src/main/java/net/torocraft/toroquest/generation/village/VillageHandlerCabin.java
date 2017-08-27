package net.torocraft.toroquest.generation.village;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.torocraft.toroquest.generation.village.util.BlockMapMeasurer;
import net.torocraft.toroquest.generation.village.util.VillagePieceBlockMap;

public class VillageHandlerCabin implements IVillageCreationHandler {

	public static String NAME = "cabin";

	public static void init() {
		MapGenStructureIO.registerStructureComponent(VillagePieceCabin.class, NAME);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerCabin());
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return new PieceWeight(VillagePieceCabin.class, 30, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return VillagePieceCabin.class;
	}

	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
		return VillagePieceCabin.createPiece(startPiece, pieces, random, p1, p2, p3, facing, p5);

	}

	public static class VillagePieceCabin extends VillagePieceBlockMap {

		public static VillagePieceCabin createPiece(StructureVillagePieces.Start start, List<StructureComponent> structures, Random rand, int x, int y, int z, EnumFacing facing, int p_175850_7_) {

			BlockPos size = new BlockMapMeasurer(NAME).measure();

			StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size.getX(), size.getY(), size.getZ(), facing);

			return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(structures, bounds) == null ? new VillagePieceCabin(start, p_175850_7_, rand, bounds, facing) : null;
		}

		public VillagePieceCabin(Start start, int type, Random rand, StructureBoundingBox bounds, EnumFacing facing) {
			super(NAME, start, type, rand, bounds, facing);
		}

		public VillagePieceCabin() {
		}

		@Override
		protected void alterPalette(Map<String, IBlockState> palette) {
			palette.clear();

			/*
			 * palette.put('-', Blocks.AIR.getDefaultState()); palette.put('W',
			 * Blocks.PLANKS.getDefaultState()); palette.put('p',
			 * Blocks.WOODEN_PRESSURE_PLATE.getDefaultState()); palette.put('G',
			 * Blocks.GLASS_PANE.getDefaultState()); // palette.put('b',
			 * Blocks.BED.getDefaultState()); palette.put('B',
			 * Blocks.STONEBRICK.getDefaultState()); palette.put('c',
			 * Blocks.CAULDRON.getDefaultState()); palette.put('f',
			 * Blocks.FURNACE.getDefaultState()); palette.put('C',
			 * Blocks.CHEST.getDefaultState()); palette.put('F',
			 * Blocks.OAK_FENCE.getDefaultState()); palette.put('N',
			 * Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.
			 * FACING, EnumFacing.NORTH)); palette.put('S',
			 * Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.
			 * FACING, EnumFacing.SOUTH)); palette.put('L',
			 * Blocks.LOG.getDefaultState()); // palette.put('T',
			 * Blocks.TORCH.getDefaultState()); palette.put('s',
			 * Blocks.WOODEN_SLAB.getDefaultState()); palette.put('D',
			 * Blocks.OAK_DOOR.getDefaultState().withProperty(BlockStairs.
			 * FACING, EnumFacing.NORTH));
			 */

		}

	}

}