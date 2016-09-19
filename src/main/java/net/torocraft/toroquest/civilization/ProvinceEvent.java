package net.torocraft.toroquest.civilization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ProvinceEvent extends PlayerEvent {

	private final Province province;

	public ProvinceEvent(EntityPlayer player, Province province) {
		super(player);
		this.province = province;
	}

	public Province getProvince() {
		return province;
	}

	public static class Enter extends ProvinceEvent {
		public Enter(EntityPlayer player, Province province) {
			super(player, province);
		}
	}

	public static class Leave extends ProvinceEvent {
		public Leave(EntityPlayer player, Province province) {
			super(player, province);
		}
	}

	public static class ReputationChange extends ProvinceEvent {
		private final int reputation;

		public ReputationChange(EntityPlayer player, Province province, int reputation) {
			super(player, province);
			this.reputation = reputation;
		}

		public int getReputation() {
			return reputation;
		}
	}
}