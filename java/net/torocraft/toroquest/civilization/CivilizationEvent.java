package net.torocraft.toroquest.civilization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CivilizationEvent extends PlayerEvent {

	public final CivilizationType civilization;

	public CivilizationEvent(EntityPlayer player, CivilizationType civilization) {
		super(player);
		this.civilization = civilization;
	}

	public static class Enter extends CivilizationEvent {

		public final Province province;

		public Enter(EntityPlayer player, Province province) {
			super(player, province.civilization);
			this.province = province;
		}
	}

	public static class Leave extends CivilizationEvent {

		public final Province province;

		public Leave(EntityPlayer player, Province province) {
			super(player, province.civilization);
			this.province = province;
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