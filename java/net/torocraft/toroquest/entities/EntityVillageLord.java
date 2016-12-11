package net.torocraft.toroquest.entities;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.torocraft.toroquest.ToroQuest;

public class EntityVillageLord extends EntityVillager implements IEntityAdditionalSpawnData {

	public static String NAME = "villageLord";

	public EntityVillageLord(World world) {
		super(world);
		this.setSize(0.6F, 1.95F);
	}

	public static void init(int entityId) {
		EntityRegistry.registerModEntity(new ResourceLocation(ToroQuest.MODID, NAME), EntityVillageLord.class, NAME, entityId, ToroQuest.INSTANCE, 60, 2, true, 0xeca58c, 0xba12c8);
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		Team team = this.getTeam();
		String name = this.getCustomNameTag();

		if (name == null || name.length() == 0) {
			name = NAME;
		}

		TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, name));
		textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponentstring;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		return data;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		
	}
}
