package net.torocraft.toroquest.generation.village.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.torocraft.toroquest.ToroQuest;

public abstract class BlockMapBase {

	protected BufferedReader reader;
	protected final String name;

	protected int y;
	protected int x;
	protected int z;

	protected String line;

	public BlockMapBase(String name) {
		this.name = name;
	}

	protected void load() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("assets/" + ToroQuest.MODID + "/structures/" + name + ".txt"), "UTF-8"));
		} catch (Exception e) {
			reader = null;
			System.out.println("Unable to load village piece File NAME[" + name + "]: " + e.getMessage());
		}
	}

}