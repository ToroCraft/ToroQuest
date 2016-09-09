package net.torocraft.toroquest.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityCreature;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class BaseToroEntity extends EntityCreature implements IEntityAdditionalSpawnData, ICommandSender {

	private static final DataParameter<String> RoleData = EntityDataManager.createKey(BaseToroEntity.class, DataSerializers.STRING);

	public String getRoleData() {
		return (String) this.dataManager.get(RoleData);
	}

	public void setRoleData(String s) {
		this.dataManager.set(RoleData, s);
	}

	private static FakePlayer chateventPlayer;

	public BaseToroEntity(World world) {
		super(world);

	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO Auto-generated method stub

	}

}
