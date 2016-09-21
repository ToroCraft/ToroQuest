package net.torocraft.toroquest.civilization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CivilizationEvent extends PlayerEvent {

	private final CivilizationType civilization;

	public CivilizationEvent(EntityPlayer player, CivilizationType civilization) {
		super(player);
		this.civilization = civilization;
	}

	public CivilizationType getCivilization() {
		return civilization;
	}

	public static class Enter extends CivilizationEvent {
		public Enter(EntityPlayer player, CivilizationType civilization) {
			super(player, civilization);
		}
	}

	public static class Leave extends CivilizationEvent {
		public Leave(EntityPlayer player, CivilizationType civilization) {
			super(player, civilization);
		}
	}

	public static class ReputationChange extends CivilizationEvent {
		private final int reputation;

		public ReputationChange(EntityPlayer player, CivilizationType civilization, int reputation) {
			super(player, civilization);
			this.reputation = reputation;
		}

		public int getReputation() {
			return reputation;
		}
	}
}