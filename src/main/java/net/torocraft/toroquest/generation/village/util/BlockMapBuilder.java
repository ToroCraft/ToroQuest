package net.torocraft.toroquest.generation.village.util;

import java.io.IOException;
import java.util.Map;

import net.minecraft.block.state.IBlockState;

public abstract class BlockMapBuilder extends BlockMapBase {

	private Map<Character, IBlockState> palette;

	protected abstract void setBlockState(IBlockState block, int x, int y, int z);

	protected abstract void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z);

	public BlockMapBuilder(String name) {
		super(name);
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

		if (line.matches("#{4,}")) {
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

}