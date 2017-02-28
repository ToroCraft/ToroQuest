package net.torocraft.toroquest.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.potion.PotionLoyalty;
import net.torocraft.toroquest.potion.PotionRoyal;

public class EntitySpawning {

	private static class PossibleConversionData {
		public long time;
		public EntityLiving from;
		public EntityLiving to;
	}

	private Map<Long, PossibleConversionData> possibleConversionMap = new ConcurrentHashMap<Long, PossibleConversionData>();

	@SubscribeEvent
	public void onCreateVillager(EntityJoinWorldEvent event) {
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityVillager) {

			cleanMap();

			if (handlePossibleConversion((EntityLiving) event.getEntity(), null)) {
				event.getEntity().setDead();
			}
		}

	}

	@SubscribeEvent
	public void onZombieVillagerDeath(LivingUpdateEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote && event.getEntity().isDead && event.getEntity() instanceof EntityZombie) {

			if (!((EntityZombie) event.getEntity()).isVillager()) {
				return;
			}

			EntityLiving convertTo = null;

			if (hasRoyalEffect(event)) {
				convertTo = new EntityVillageLord(event.getEntity().worldObj);
			} else if (hasLoyalEffect(event)) {
				convertTo = new EntityGuard(event.getEntity().worldObj);
			}

			if (convertTo == null) {
				return;
			}

			if (handlePossibleConversion((EntityLiving) event.getEntity(), convertTo)) {
				removeVillager(event);
			}
		}
	}

	private boolean hasRoyalEffect(LivingUpdateEvent event) {
		return ((EntityZombie) event.getEntity()).getActivePotionEffect(PotionRoyal.INSTANCE) != null;
	}

	private boolean hasLoyalEffect(LivingUpdateEvent event) {
		return ((EntityZombie) event.getEntity()).getActivePotionEffect(PotionLoyalty.INSTANCE) != null;
	}

	private void removeVillager(LivingUpdateEvent event) {
		List<EntityVillager> list = event.getEntity().worldObj.getEntitiesWithinAABB(EntityVillager.class,
				new AxisAlignedBB(event.getEntity().getPosition()));
		if (list.size() < 1) {
			return;
		}
		list.get(0).setDead();
	}

	private boolean handlePossibleConversion(EntityLiving entity, EntityLiving toEntity) {
		PossibleConversionData data = hasConversionEntry(entity);
		if (data == null) {
			data = new PossibleConversionData();
			data.from = entity;
			data.to = toEntity;
			data.time = System.currentTimeMillis();
			possibleConversionMap.put(entity.getPosition().toLong(), data);
			return false;
		} else {
			replaceEntityWithVillageLord(data, toEntity);
			return true;
		}
	}

	private PossibleConversionData hasConversionEntry(Entity entity) {
		PossibleConversionData data = possibleConversionMap.get(entity.getPosition().toLong());

		if (data == null) {
			return null;
		}

		long delay = System.currentTimeMillis() - data.time;

		if (delay < 0 || delay > 500) {
			return null;
		} else {
			return possibleConversionMap.remove(entity.getPosition().toLong());
		}
	}

	private void replaceEntityWithVillageLord(PossibleConversionData data, EntityLiving toEntity) {
		World world = data.from.worldObj;
		BlockPos pos = data.from.getPosition();

		if (toEntity == null) {
			toEntity = data.to;
		}

		if (toEntity == null) {
			return;
		}

		if (!data.from.isDead) {
			data.from.setDead();
		}

		toEntity.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData) null);
		toEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		world.spawnEntityInWorld(toEntity);
		toEntity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
	}

	private void cleanMap() {
		if (possibleConversionMap.size() == 0) {
			return;
		}
		for (Iterator<Entry<Long, PossibleConversionData>> it = possibleConversionMap.entrySet().iterator(); it.hasNext();) {
			Entry<Long, PossibleConversionData> entry = it.next();
			if (System.currentTimeMillis() - entry.getValue().time > 10000) {
				it.remove();
			}
		}
	}

}
