package net.torocraft.toroquest.generation.village.util;

import java.io.IOException;
import java.util.Map;

import net.minecraft.block.state.IBlockState;

public abstract class BlockMapBuilder extends BlockMapBase {

	private Map<String, IBlockState> palette;

	protected abstract void setBlockState(IBlockState block, int x, int y, int z);

	protected abstract void replaceAirAndLiquidDownwards(IBlockState block, int x, int y, int z);

	public BlockMapBuilder(String name) {
		super(name);
	}

	public void build(Map<String, IBlockState> palette) {
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

		char[] a = line.toCharArray();

		for (int i = 0; i < a.length - 1; i += 2) {
			placeBlock(String.valueOf(a[i]) + String.valueOf(a[i + 1]));
			x++;
		}


		z++;
	}

	private void placeBlock(String c) {
		if (c == "  ") {
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