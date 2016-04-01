package net.torocraft.bouncermod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class KnockbackEffect {

	public static void getEntityKnockbackSpeed(Entity target, Entity source, Double knockback) {
		Double xDistance = getDistance(target.posX,source.posX);
		Double zDistance = getDistance(target.posZ,source.posZ);
		Double hypotenuse = getHypotenuse(xDistance,zDistance);
		
		target.motionX = knockback * xDistance/hypotenuse;
		target.motionZ = knockback * zDistance/hypotenuse;
		
		target.motionY = Math.signum(target.posY - source.posY) * (knockback/2);
		if (target.motionY == 0) {
			target.motionY = knockback;
		}
	}
	
	public static void getEntityKnockbackSpeed(EntityLivingBase target, EntityLivingBase source, Double knockback) {
		Double xDistance = getDistance(target.posX,source.posX);
		Double zDistance = getDistance(target.posZ,source.posZ);
		Double hypotenuse = getHypotenuse(xDistance,zDistance);
		
		target.motionX = knockback * xDistance/hypotenuse;
		target.motionZ = knockback * zDistance/hypotenuse;
		
		target.motionY = Math.signum(target.posY - source.posY) * (knockback/2);
		if (target.motionY == 0) {
			target.motionY = .5;
		}
	}
	
	private static Double getDistance(Double targetVal, Double attackerVal) {
		return targetVal - attackerVal;
	}
	
	private static Double getHypotenuse(Double xLength, Double zLength) {
		return Math.abs(Math.sqrt(xLength * xLength + zLength * zLength));		
	}
}
