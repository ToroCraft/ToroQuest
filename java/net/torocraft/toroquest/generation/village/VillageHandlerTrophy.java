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

public class VillageHandlerTrophy implements IVillageCreationHandler {

	public static String NAME = "trophy";

	public static void init() {
		MapGenStructureIO.registerStructureComponent(VillagePieceTrophy.class, NAME);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerTrophy());
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return new PieceWeight(VillagePieceTrophy.class, 30, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return VillagePieceTrophy.class;
	}

	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
		return VillagePieceTrophy.createPiece(startPiece, pieces, random, p1, p2, p3, facing, p5);

	}

	public static class VillagePieceTrophy extends VillagePieceBlockMap {

		public static VillagePieceTrophy createPiece(StructureVillagePieces.Start start, List<StructureComponent> structures, Random rand, int x, int y, int z, EnumFacing facing, int p_175850_7_) {
			BlockPos size = new BlockMapMeasurer(NAME).measure();
			StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size.getX(), size.getY(), size.getZ(), facing);
			return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(structures, bounds) == null ? new VillagePieceTrophy(start, p_175850_7_, rand, bounds, facing) : null;
		}

		public VillagePieceTrophy(Start start, int type, Random rand, StructureBoundingBox bounds, EnumFacing facing) {
			super(NAME, start, type, rand, bounds, facing);
		}

		public VillagePieceTrophy() {
		}

		@Override
		protected void alterPalette(Map<String, IBlockState> palette) {

		}

	}

}