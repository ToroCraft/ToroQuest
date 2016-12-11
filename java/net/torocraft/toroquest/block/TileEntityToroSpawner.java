package net.torocraft.toroquest.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.torocraft.toroquest.entities.EntityGuard;

public class TileEntityToroSpawner extends TileEntity implements ITickable {

	private int triggerDistance = 60;
	private List<String> entityIds = new ArrayList<String>();
	private int spawnRadius = 0;

	private int spawnTryCount = 0;

	public TileEntityToroSpawner() {

	}

	public int getSpawnRadius() {
		return spawnRadius;
	}

	public void setSpawnRadius(int spawnRadius) {
		this.spawnRadius = spawnRadius;
	}

	public void setTriggerDistance(int triggerDistance) {
		this.triggerDistance = triggerDistance;
	}

	public void setEntityIds(List<String> entityIds) {
		this.entityIds = entityIds;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		triggerDistance = compound.getInteger("trigger_distance");
		spawnRadius = compound.getInteger("spawn_radius");

		entityIds.clear();

		NBTTagList list;
		try {
			list = (NBTTagList) compound.getTag("entity_ids");
		} catch (Exception e) {
			list = new NBTTagList();
		}
		for (int i = 0; i < list.tagCount(); i++) {
			entityIds.add(list.getCompoundTagAt(i).getString("id"));
		}

	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("trigger_distance", triggerDistance);
		compound.setInteger("spawn_radius", spawnRadius);

		NBTTagList list = new NBTTagList();
		for (String id : entityIds) {
			NBTTagCompound c = new NBTTagCompound();
			c.setString("id", id);
			list.appendTag(c);
		}
		compound.setTag("entity_ids", list);

		return compound;
	}

	public void update() {
		if (!worldObj.isRemote && isRunTick() && withinRange()) {
			triggerSpawner();
		}
	}

	protected void triggerSpawner() {
		for (String entityId : entityIds) {
			spawnCreature(entityId);
		}
		worldObj.setBlockToAir(pos);
	}

	public void spawnCreature(String entityID) {
		Random rand = worldObj.rand;

		int r = 0;
		double angle = 0;
		spawnTryCount = 0;
		double x = 0;
		double z = 0;

		while (spawnTryCount < 10) {
			spawnTryCount++;

			if (spawnRadius > 0) {
				r = rand.nextInt(spawnRadius);
				angle = (double) rand.nextInt(62) / 10;
				x = r * Math.cos(angle);
				z = r * Math.sin(angle);
				if (badSpawnLocation(x, 0, z)) {
					continue;
				}
			}

			spawnCreature(entityID, x, 0, z);
			return;
		}

		spawnCreature(entityID, 0, 0, 0);
	}

	public void spawnCreature(String entityID, double xOffset, double yOffset, double zOffset) {
		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation("toroquest", entityID), worldObj);

		if (!(entity instanceof EntityLivingBase)) {
			System.out.println("entity not EntityLivingBase");
			return;
		}

		double x = pos.getX() + 0.5D + xOffset;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D + zOffset;

		spawnEntityLiving((EntityLiving) entity, x, y, z);
	}

	protected boolean spawnEntityLiving(EntityLiving entity, double x, double y, double z) {
		entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldObj.rand.nextFloat() * 360.0F), 0.0F);
		entity.rotationYawHead = entity.rotationYaw;
		entity.renderYawOffset = entity.rotationYaw;
		entity.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData) null);
		worldObj.spawnEntityInWorld(entity);
		entity.playLivingSound();
		return true;
	}

	private boolean badSpawnLocation(double xOffset, double yOffset, double zOffset) {
		double x = pos.getX() + 0.5D + xOffset;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D + zOffset;

		BlockPos pos = new BlockPos(x + 0.5D + xOffset, y, z);
		if (worldObj.getBlockState(pos).getMaterial() != Material.AIR) {
			return true;
		}
		if (worldObj.getBlockState(pos.up()).getMaterial() != Material.AIR) {
			return true;
		}
		return false;
	}

	private void spawnCreature() {
		EntityGuard entity = new EntityGuard(worldObj);
		EntityLiving entityliving = (EntityLiving) entity;
		entity.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldObj.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
		worldObj.spawnEntityInWorld(entity);
	}

	private boolean withinRange() {
		return worldObj.isAnyPlayerWithinRangeAt((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (double) this.triggerDistance);
	}

	private boolean isRunTick() {
		return worldObj.getTotalWorldTime() % 20L == 0L;
	}

	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
		nbttagcompound.removeTag("SpawnPotentials");
		return nbttagcompound;
	}

	public boolean onlyOpsCanSetNbt() {
		return true;
	}

}
