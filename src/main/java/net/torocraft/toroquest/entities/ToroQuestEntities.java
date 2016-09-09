package net.torocraft.toroquest.entities;

public class ToroQuestEntities {
	public static void init() {
		int id = 1;
		EntityMage.init(id++);
		EntityFriendlyMage.init(id++);
		EntityMonolithEye.init(id++);
		EntityToro.init(id++);
	}
}
