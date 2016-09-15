package net.torocraft.toroquest.generation.village.util;

import java.io.IOException;

import net.minecraft.util.math.BlockPos;

public class BlockMapMeasurer extends BlockMapBase {

	public BlockMapMeasurer(String name) {
		super(name);
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
				if (line.matches("#{4,}")) {
					y++;
					zMax = Math.max(zMax, z);
					z = 0;
					continue;
				}
				xMax = Math.max(xMax, line.toCharArray().length);
				z++;
			}
			yMax = Math.max(yMax, y);
		} catch (IOException e) {
			System.out.println("Failed to measure village piece NAME[" + name + "]: " + e.getMessage());
		}

		return new BlockPos(xMax, yMax + 1, zMax);
	}


}