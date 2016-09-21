package net.torocraft.toroquest.civilization.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;

public interface PlayerCivilizationCapability {


	void setPlayerReputation(CivilizationType civ, int amount);

	void adjustPlayerReputation(CivilizationType civ, int amount);

	int getPlayerReputation(CivilizationType civ);

	void setPlayerInCivilization(Province civ);

	Province getPlayerInCivilization();

	NBTTagCompound writeNBT();

	void readNBT(NBTBase c);

	void updatePlayerLocation();

}
