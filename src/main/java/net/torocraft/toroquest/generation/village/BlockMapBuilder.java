package net.torocraft.toroquest.generation.village;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.torocraft.toroquest.ToroQuest;

public abstract class BlockMapBuilder {

	private Map<Character, IBlockState> palette;
	private BufferedReader reader;

	private final String name;

	private int y = -1;
	private int x;
	private int z;
	private String line;

	protected abstract void setBlockState(IBlockState block, int x, int y, int z);

	protected abstract void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z);

	public BlockMapBuilder(String name) {
		this.name = name;
	}

	public BlockPos measure() {
		load();

		x = 0;
		y = 0;
		z = 0;

		int xMax = 0;
		int yMax = 0;
		int zMax = 0;

		if (reader == null) {
			return new BlockPos(0, 0, 0);
		}

		try {
			while ((line = reader.readLine()) != null) {
				xMax = Math.max(xMax, line.toCharArray().length);
				if (line.matches("-{4,}")) {
					y++;
					zMax = Math.max(zMax, z);
					z = 0;
					continue;
				}
				z++;
			}
			yMax = Math.max(yMax, y);
		} catch (IOException e) {
			System.out.println("Failed to measure village piece NAME[" + name + "]: " + e.getMessage());
		}

		return new BlockPos(xMax - 1, yMax - 1, zMax - 1);
	}

	public void build(Map<Character, IBlockState> palette) {
		this.palette = palette;
		load();

		x = 0;
		y = 0;
		z = 0;

		if (reader == null) {
			return;
		}

		try {
			while ((line = reader.readLine()) != null) {
				handleLine();
			}
		} catch (IOException e) {
			System.out.println("Failed to build village piece NAME[" + name + "]: " + e.getMessage());
		}
	}

	private void handleLine() {

		if (line == null) {
			return;
		}

		if (line.matches("-{4,}")) {
			y++;
			z = 0;
			return;
		}

		x = 0;

		for (char c : line.toCharArray()) {
			placeBlock(c);
			x++;
		}

		z++;
	}

	private void placeBlock(char c) {
		if (c == ' ') {
			return;
		}
		IBlockState block = palette.get(c);
		if (block == null) {
			return;
		}

		if (y == 0) {
			replaceAirAndLiquidDownwards(block, x, y, z);
		} else {
			setBlockState(block, x, y, z);
		}
	}

	private void load() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("assets/" + ToroQuest.MODID + "/structures/" + name + ".txt"), "UTF-8"));
		} catch (IOException e) {
			reader = null;
			System.out.println("Unable to load village piece File NAME[" + name + "]: " + e.getMessage());
		}
	}

}