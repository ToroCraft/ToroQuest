package net.torocraft.toroquest.entities;

public class ToroQuestEntities {
	public static void init() {
		int id = 1;
		EntityMage.init(id++);
		EntityMonolithEye.init(id++);
		EntityToro.init(id++);
		EntityVillageLord.init(id++);
		EntityGuard.init(id++);
		EntityShopkeeper.init(id++);
		EntityBas.init(id++);
		EntityVampireBat.init(id++);
		EntitySentry.init(id++);
		EntityRainbowGuard.init(id++);
		EntityRainbowKing.init(id++);
		EntityFugitive.init(id++);
	}
}
