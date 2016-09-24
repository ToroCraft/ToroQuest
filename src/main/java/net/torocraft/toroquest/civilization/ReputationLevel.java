package net.torocraft.toroquest.civilization;

public enum ReputationLevel {
	HERO, ALLY, FRIEND, DRIFTER, OUTCAST, ENEMY, VILLIAN;

	public static ReputationLevel fromReputation(int rep) {

		if (rep <= -1000) {
			return ReputationLevel.VILLIAN;
		}

		if (rep <= -100) {
			return ReputationLevel.ENEMY;
		}

		if (rep <= -10) {
			return ReputationLevel.ENEMY;
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