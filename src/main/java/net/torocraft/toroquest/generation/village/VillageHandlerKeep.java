package net.torocraft.toroquest.generation.village;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageHandlerKeep implements IVillageCreationHandler {

	public static String NAME = "villageKeep";

	public static void init() {
		MapGenStructureIO.registerStructureComponent(VilleagePieceKeep.class, "villageKeep");
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerKeep());
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return new PieceWeight(VilleagePieceKeep.class, 30, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return VilleagePieceKeep.class;
	}

	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
		return VilleagePieceKeep.createPiece(startPiece, pieces, random, p1, p2, p3, facing, p5);

	}

	public static class VilleagePieceKeep extends StructureVillagePieces.Village {
		public VilleagePieceKeep() {
		}

		public VilleagePieceKeep(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing) {
			super(start, type);
			this.setCoordBaseMode(facing);
			this.boundingBox = p_i45571_4_;
		}

		public static VilleagePieceKeep createPiece(StructureVillagePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int x, int y, int z, EnumFacing facing, int p_175850_7_) {
			System.out.println("*** creating village keep");
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 9, 6, facing);
			return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null ? new VilleagePieceKeep(start, p_175850_7_, rand, structureboundingbox, facing) : null;
		}

		/**
		 * second Part of Structure generating, this for example places
		 * Spiderwebs, Mob Spawners, it closes Mineshafts at the end, it adds
		 * Fences...
		 */

		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
			if (this.averageGroundLvl < 0) {
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
			}

			IBlockState cobble = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
			IBlockState carpet = this.getBiomeSpecificBlockState(Blocks.CARPET.getDefaultState());
			IBlockState planks = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
			IBlockState logs = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
			IBlockState glowstone = this.getBiomeSpecificBlockState(Blocks.GLOWSTONE.getDefaultState());
			IBlockState glassPane = this.getBiomeSpecificBlockState(Blocks.GLASS_PANE.getDefaultState());
			IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

			IBlockState stairsNorth = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
			IBlockState stairsSouth = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
			IBlockState stairsEast = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
			IBlockState stairsWest = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));

			// -5599655086621954440

			// clear inside
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

			// floor
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, cobble, cobble, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 8, 1, 5, carpet, carpet, false);

			// front and back wall
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 4, 5, cobble, cobble, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 4, 5, cobble, cobble, false);

			// front and back top wall area
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 0, 4, 4, planks, logs, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 1, 8, 4, 4, planks, logs, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 0, 5, 3, logs, logs, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 2, 8, 5, 3, logs, logs, false);

			// lights
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 1, 5, 3, glowstone, glowstone, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 2, 3, 5, 3, glowstone, glowstone, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 5, 3, glowstone, glowstone, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 2, 7, 5, 3, glowstone, glowstone, false);

			// side walls
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 8, 4, 0, cobble, cobble, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 5, 8, 4, 5, cobble, cobble, false);

			// side windows
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 0, 6, 2, 0, glassPane, glassPane, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 5, 6, 2, 5, glassPane, glassPane, false);

			// back window
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 2, 8, 3, 3, glassPane, glassPane, false);

			// corners
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 0, logs, logs, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 8, 4, 0, logs, logs, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 0, 4, 5, logs, logs, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 5, 8, 4, 5, logs, logs, false);

			// front doors

			// this.func_189927_a(worldIn, structureBoundingBoxIn, randomIn, 0,
			// 1,
			// 1, EnumFacing.EAST);
			// this.func_189927_a(worldIn, structureBoundingBoxIn, randomIn, 0,
			// 1, 4, EnumFacing.EAST);

			// side doors
			this.func_189927_a(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.NORTH);
			// this.func_189927_a(worldIn, structureBoundingBoxIn, randomIn, 1,
			// 1,
			// 5, EnumFacing.SOUTH);

			// counter
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 1, 4, planks, planks, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 4, fence, fence, false);

			// roof
			for (int i = -1; i <= 2; ++i) {
				for (int j = 0; j <= 8; ++j) {
					this.setBlockState(worldIn, stairsNorth, j, 4 + i, i, structureBoundingBoxIn);
					this.setBlockState(worldIn, stairsSouth, j, 4 + i, 5 - i, structureBoundingBoxIn);
				}
			}

			// foundation
			if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
				this.setBlockState(worldIn, stairsNorth, 1, 0, -1, structureBoundingBoxIn);
			}

			for (int l = 0; l < 6; ++l) {
				for (int k = 0; k < 9; ++k) {
					this.clearCurrentPositionBlocksUpwards(worldIn, k, 9, l, structureBoundingBoxIn);
					this.replaceAirAndLiquidDownwards(worldIn, cobble, k, -1, l, structureBoundingBoxIn);
				}
			}

			return true;
		}

		protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
			return 1;
		}

	}
}