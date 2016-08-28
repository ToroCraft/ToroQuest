package net.torocraft.torobasemod.entities;

public class ToroEntities {
	public static void init() {
		int id = 50;
		EntityMage.init(id++);
		EntityFriendlyMage.init(id++);
		EntityMonolithEye.init(id++);
	}
}
