package net.torocraft.toroquest.generation.village;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntityGuard;
import net.torocraft.toroquest.generation.village.util.BlockMapMeasurer;
import net.torocraft.toroquest.generation.village.util.VillagePieceBlockMap;

public class VillageHandlerBarracks implements IVillageCreationHandler {

	public static String NAME = "barracks";

	public static void init() {
		MapGenStructureIO.registerStructureComponent(VillagePieceBarracks.class, NAME);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerBarracks());
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return new PieceWeight(VillagePieceBarracks.class, 30, 1);
	}

	@Override
	public Class<?> getComponentClass() {
		return VillagePieceBarracks.class;
	}

	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
		return VillagePieceBarracks.createPiece(startPiece, pieces, random, p1, p2, p3, facing, p5);

	}

	public static class VillagePieceBarracks extends VillagePieceBlockMap {

		@Override
		protected int getYOffset() {
			return -1;
		}
		
		public static VillagePieceBarracks createPiece(StructureVillagePieces.Start start, List<StructureComponent> structures, Random rand, int x, int y, int z, EnumFacing facing, int p_175850_7_) {
			BlockPos size = new BlockMapMeasurer(NAME).measure();
			StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size.getX(), size.getY(), size.getZ(), facing);
			return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(structures, bounds) == null ? new VillagePieceBarracks(start, p_175850_7_, rand, bounds, facing) : null;
		}

		public VillagePieceBarracks() {
			super();
		}

		public VillagePieceBarracks(Start start, int type, Random rand, StructureBoundingBox bounds, EnumFacing facing) {
			super(NAME, start, type, rand, bounds, facing);
		}

		@Override
		protected boolean specialBlockHandling(World world, String c, int x, int y, int z) {
			if (!c.equals("xx")) {
				return false;
			}
			
			setBlockState(world, Blocks.AIR.getDefaultState(), x, y, z, boundingBox);

			int j = this.getXWithOffset(x, z);
			int k = this.getYWithOffset(y);
			int l = this.getZWithOffset(x, z);

			/*
			 * if (!structurebb.isVecInside(new BlockPos(j, k, l))) { return; }
			 */

			EntityArmorStand stand = new EntityArmorStand(world);
			stand.setLocationAndAngles((double) j + 0.5D, (double) k, (double) l + 0.5D, 90F, 0.0F);
			world.spawnEntity(stand);

			List<String> entities = new ArrayList<String>();
			entities.add(ToroQuest.MODID + ":" + EntityGuard.NAME);
			specialHandlingForSpawner(world, "xx", c, x, y, z, entities);

			return true;

		}


		@Override
		protected void alterPalette(Map<String, IBlockState> palette) {

		}

	}

}