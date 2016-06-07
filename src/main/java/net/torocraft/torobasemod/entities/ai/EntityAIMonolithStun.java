package net.torocraft.torobasemod.entities.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.torocraft.torobasemod.entities.EntityMonolithEye;

public class EntityAIMonolithStun extends EntityAIBase {

	private static float twitchDistance = 1F;
	private static float twitchSpeed = 1F;
	private static int stunLength = 200;	
	
	private EntityMonolithEye entity;
	private int stunClock;
	private double oX;
	private double oY;
	private double oZ;
	
	
	public EntityAIMonolithStun(EntityMonolithEye entity) {
		super();
		this.entity = entity;
		this.setMutexBits(1);
		
		if(entity.originPos == null) {
			entity.originPos = entity.getPositionVector();
		}
		
		oX = entity.originPos.xCoord;
		oY = entity.originPos.yCoord;
		oZ = entity.originPos.zCoord;
	}

	@Override
	public boolean shouldExecute() {
		if(entity.getIsStunned() && stunClock <= this.stunLength) {
			return true;			
		}
		return false;
	}

	public void startExecuting() {
		super.startExecuting();
	}

	public void updateTask() {
		Double x = entity.getPositionVector().xCoord;
		Double y = entity.getPositionVector().yCoord;
		Double z = entity.getPositionVector().zCoord;
	}
	
    public void resetTask()
    {
        stunClock = 0;
        entity.setIsStunned(false);
    }
}
