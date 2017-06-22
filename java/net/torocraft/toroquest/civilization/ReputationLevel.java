package net.torocraft.toroquest.civilization;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

public enum ReputationLevel {
	HERO(0.7), ALLY(0.9), FRIEND(1.0), DRIFTER(1.7), OUTCAST(10.0), ENEMY(50.0), VILLAIN(100.0);
	
	private ReputationLevel(double priceMultiplier) {
		this.priceMultiplier = priceMultiplier;
	}
	
	private double priceMultiplier;
	
	public double getPriceMultiplier() {
		return priceMultiplier;
	}

	public int adjustPrice(int price) {
		return MathHelper.clamp((int) (price * priceMultiplier), 1, 64);
	}
	
	public String getLocalname() {
		return I18n.format("civilization.reputation_level." + this.toString().toLowerCase());
	}

	public static ReputationLevel fromReputation(int rep) {

		if (rep <= -1000) {
			return ReputationLevel.VILLAIN;
		}

		if (rep <= -100) {
			return ReputationLevel.ENEMY;
		}

		if (rep <= -10) {
			return ReputationLevel.OUTCAST;
		}

		if (rep < 10) {
			return ReputationLevel.DRIFTER;
		}

		if (rep < 100) {
			return ReputationLevel.FRIEND;
		}

		if (rep < 1000) {
			return ReputationLevel.ALLY;
		}

		return HERO;
	}
}