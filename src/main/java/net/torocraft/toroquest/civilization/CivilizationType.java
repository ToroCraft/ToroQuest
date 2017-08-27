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

	public String getFriendlyEnteringMessage(Province province) {
		return I18n.format("civilization.entering.friendly", province.name, getLocalizedName());
	}

	public String getNeutralEnteringMessage(Province province) {
		return I18n.format("civilization.entering.neutral", province.name, getLocalizedName());
	}

	public String getHostileEnteringMessage(Province province) {
		return I18n.format("civilization.entering.hostile", province.name, getLocalizedName());
	}

	public String getFriendlyLeavingMessage(Province province) {
		return I18n.format("civilization.leaving.friendly", province.name, getLocalizedName());
	}

	public String getNeutralLeavingMessage(Province province) {
		return I18n.format("civilization.leaving.neutral", province.name, getLocalizedName());
	}

	public String getHostileLeavingMessage(Province province) {
		return I18n.format("civilization.leaving.hostile", province.name, getLocalizedName());
	}
}