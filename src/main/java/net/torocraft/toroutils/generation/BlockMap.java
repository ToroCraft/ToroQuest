package net.torocraft.toroutils.generation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMap {

	private final BlockPos origin;
	private final World world;
	private String blockMap;
	private String[] lines;
	private String delimiter;
	private int lineIndex;
	private String line;
	private int y = -1;
	private int x;
	private int z;
	private Map<Character, IBlockState> palette;

	public BlockMap(World world, BlockPos origin) {
		this.world = world;
		this.origin = origin;
	}

	public void loadLocalFile(String path) {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			throw new RuntimeException("file not found: " + path);
		}
		try {
			blockMap = IOUtils.toString(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generate() {
		palette = new HashMap<Character, IBlockState>();
		splitIntoLines();
		parseDelimiter();
		for (lineIndex = 1; lineIndex < lines.length; lineIndex++) {
			line = lines[lineIndex];
			handleLine();
		}
	}

	private void handleLine() {

		if (line == null) {
			return;
		}

		if (delimiter.equals(line)) {
			y++;
			z = 0;
			return;
		}

		palette.put(' ', null);

		if (y < 0) {
			parsePalette();
		} else {
			generateLine();
			z++;
		}
	}

	private void parsePalette() {
		if (line.length() < 3) {
			System.out.println("Invalid palette line [" + line + "]");
			return;
		}

		System.out.println("parsing palette line [" + line + "]");
		char c = line.substring(0, 1).toCharArray()[0];
		String blockType = line.substring(2);

		// N=minecraft:dark_oak_stairs|facing:north

		System.out.println("palette: [" + c + "][" + blockType + "]");

		palette.put(c, parsePaleteBlock(blockType));

	}

	private IBlockState parsePaleteBlock(String blockString) {

		String[] a = blockString.split("\\|");

		IBlockState block = Block.blockRegistry.getObject(new ResourceLocation(a[0])).getDefaultState();

		if (a.length > 0) {
			for (int i = 1; i < a.length; i++) {
				String propertyString = a[i];
				if (propertyString == null) {
					continue;
				}

				String[] aProp = propertyString.split(":");

				if (aProp.length != 2) {
					continue;
				}

				IProperty prop = null;
				Object value = null;

				if ("facing".equals(aProp[0])) {
					prop = BlockHorizontal.FACING;
				}

				if ("north".equals(aProp[1])) {
					value = EnumFacing.NORTH;
				} else if ("south".equals(aProp[1])) {
					value = EnumFacing.SOUTH;
				}

				if (value == null || prop == null) {
					continue;
				}

				System.out.println("adding property [" + aProp[0] + "] [" + aProp[1] + "]");

				block = block.withProperty(prop, value);

			}
		}


		return block;
	}

	private void parseDelimiter() {
		delimiter = lines[0];
		System.out.println("delim: [" + delimiter + "]");
	}

	private void splitIntoLines() {
		lines = blockMap.split("\n\r?");
	}

	private void generateLine() {
		System.out.println("placing line [" + line + "] [" + x + "][" + y + "][" + z + "]");
		x = 0;
		char[] a = line.toCharArray();
		for (char c : a) {
			placeBlock(c);
			x++;
		}
	}

	private void placeBlock(char c) {

		IBlockState block = palette.get(c);

		if (block == null) {
			block = Blocks.air.getDefaultState();
			System.out.println("palette entry not found for [" + c + "]");
		}

		world.setBlockState(cursorCoords(), block);
	}

	private boolean onAirBlock() {
		IBlockState currentBlock = world.getBlockState(cursorCoords());
		return !currentBlock.isOpaqueCube();
	}

	private BlockPos cursorCoords() {
		return origin.add(x, y, z);
	}

}
