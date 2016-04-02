package net.torocraft.games.chess.pieces.enities;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.torocraft.games.chess.BlockChessControl;
import net.torocraft.games.chess.ai.EntityAILookDownBoard;
import net.torocraft.games.chess.ai.EntityAIMoveToPosition;

public abstract class EntityChessPiece extends EntityCreature implements IChessPiece {

	private Side side = Side.WHITE;
	private String chessPosition;
	private BlockPos gameBlockPosition;
	private static final String NBT_SIDE_KEY = "chessside";
	private static final String NBT_POSITION_KEY = "chessposition";
	private static final String NBT_XGAME_POS_KEY = "xgamepos";
	private static final String NBT_YGAME_POS_KEY = "ygamepos";
	private static final String NBT_ZGAME_POS_KEY = "zgamepos";

	private boolean moved = true;

	public EntityChessPiece(World worldIn) {
		super(worldIn);
		experienceValue = 0;
		//setHealth(0.1f);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIMoveToPosition(this));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAILookDownBoard(this));
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	public void resetMovedFlag() {
		moved = false;
	}

	public boolean hasMoved() {
		return moved;
	}

	double x = 0;
	double z = 0;

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (x != posX || z != posZ) {
			moved = true;
		}

		x = posX;
		z = posZ;
	}

	public void onLivingUpdate() {
		this.updateArmSwingProgress();
		super.onLivingUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
	}

	public void setChessPosition(String position) {
		moved = true;
		this.chessPosition = position;
	}

	public String getChessPosition() {
		return this.chessPosition;
	}

	private boolean isRunTick() {
		return worldObj.getTotalWorldTime() % 80L == 0L;
	}

	/*
	 * public ChessGame getGame() { if (game == null && gameBlockPosition !=
	 * null) { try{ game = ((BlockChessControl)
	 * worldObj.getBlockState(gameBlockPosition).getBlock()).getGame();
	 * }catch(ClassCastException e){ game = null; } }
	 * 
	 * if(game == null){ kill(); }
	 * 
	 * return game; }
	 */

	@Deprecated
	private BlockPos searchForChessControlBlock(int xRange, int yRange, int zRange) {

		BlockPos blockpos = new BlockPos(this);
		BlockPos blockpos1;

		for (int y = -yRange; y < yRange; y++) {
			for (int x = -xRange; x < xRange; x++) {
				for (int z = -zRange; z < zRange; z++) {
					blockpos1 = blockpos.add(x, y, z);
					if (blockIsChessControl(blockpos1)) {
						return blockpos1;
					}
				}
			}
		}

		return null;
	}

	private boolean blockIsChessControl(BlockPos pos) {

		return worldObj.getBlockState(pos).getBlock() instanceof BlockChessControl;
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.entity_hostile_swim;
	}

	protected SoundEvent getSplashSound() {
		return SoundEvents.entity_hostile_splash;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.entity_hostile_hurt;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.entity_hostile_death;
	}

	protected SoundEvent getFallSound(int heightIn) {
		return heightIn > 4 ? SoundEvents.entity_hostile_big_fall : SoundEvents.entity_hostile_small_fall;
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
					((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F,
						(double) MathHelper.sin(this.rotationYaw * 0.017453292F),
						(double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;

				if (itemstack != null && itemstack1 != null && itemstack.getItem() instanceof ItemAxe
						&& itemstack1.getItem() == Items.shield) {
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.shield, 100);
						this.worldObj.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	public float getBlockPathWeight(BlockPos pos) {
		return 0.5F - this.worldObj.getLightBrightness(pos);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere() {
		return false;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}

	/**
	 * Entity won't drop items or experience points if this returns false
	 */
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public void setSide(Side side) {
		this.side = side;
	}

	public void setGamePosition(BlockPos pos) {
		this.gameBlockPosition = pos;
	}

	public BlockPos getGamePosition() {
		return this.gameBlockPosition;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound t) {
		super.writeEntityToNBT(t);

		t.setInteger(NBT_XGAME_POS_KEY, gameBlockPosition.getX());
		t.setInteger(NBT_YGAME_POS_KEY, gameBlockPosition.getY());
		t.setInteger(NBT_ZGAME_POS_KEY, gameBlockPosition.getZ());

		t.setBoolean(NBT_SIDE_KEY, castSide(side));
		t.setString(NBT_POSITION_KEY, chessPosition);
	}

	private boolean castSide(Side side) {
		if (Side.BLACK.equals(side)) {
			return true;
		} else {
			return false;
		}
	}

	private Side castSide(Boolean side) {
		if (side) {
			return Side.BLACK;
		} else {
			return Side.WHITE;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound t) {
		super.readEntityFromNBT(t);
		side = castSide(t.getBoolean(NBT_SIDE_KEY));
		chessPosition = t.getString(NBT_POSITION_KEY);
		gameBlockPosition = new BlockPos(t.getInteger(NBT_XGAME_POS_KEY), t.getInteger(NBT_YGAME_POS_KEY),
				t.getInteger(NBT_ZGAME_POS_KEY));
	}
}
