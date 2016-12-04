package net.torocraft.toroquest.civilization;

import net.minecraft.client.resources.I18n;

public enum CivilizationType {
	EARTH, WIND, FIRE, WATER, MOON, SUN;
	public String getUnlocalizedName() {
		return "civilization." + this.toString().toLowerCase() + ".name";
	}

	public String getLocalizedName() {
		return I18n.format(getUnlocalizedName(), new Object[0]);
	}

	public String getFriendlyEnteringMessage() {
		return I18n.format("civilization.entering.friendly", getLocalizedName());
	}

	public String getNeutralEnteringMessage() {
		return I18n.format("civilization.entering.neutral", getLocalizedName());
	}

	public String getHostileEnteringMessage() {
		return I18n.format("civilization.entering.hostile", getLocalizedName());
	}

	public String getFriendlyLeavingMessage() {
		return I18n.format("civilization.leaving.friendly", getLocalizedName());
	}

	public String getNeutralLeavingMessage() {
		return I18n.format("civilization.leaving.neutral", getLocalizedName());
	}

	public String getHostileLeavingMessage() {
		return I18n.format("civilization.leaving.hostile", getLocalizedName());
	}
}