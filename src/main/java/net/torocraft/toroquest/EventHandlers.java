package net.torocraft.toroquest;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.entities.EntityToro;

public class EventHandlers {

	@SubscribeEvent
	public void toroDontLikeYouHurtingCows(LivingHurtEvent event) {
		System.out.println("You hurt a cow?");
		EntityLivingBase victum = event.getEntityLiving();
		EntityLivingBase attacker = getAttacker(event);

		if (victum == null || attacker == null || !(victum instanceof EntityCow)) {
			return;
		}

		List<EntityToro> nearbyToros = victum.getEntityWorld().getEntitiesWithinAABB(EntityToro.class, victum.getEntityBoundingBox().expand(40.0D, 10.0D, 40.0D));
		for (EntityToro toro : nearbyToros) {
			toro.setAttackTarget(attacker);
		}
	}

	private EntityLivingBase getAttacker(LivingHurtEvent event) {
		try {
			return (EntityLivingBase) event.getSource().getEntity();
		} catch (Exception e) {
			return null;
		}
	}

}
