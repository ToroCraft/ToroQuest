package net.torocraft.torobasemod.entities;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torobasemod.ToroBaseMod;

public class EntityMonolithEye extends EntityFlying implements IRangedAttackMob {

	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityWitch.class, DataSerializers.BOOLEAN);
    
	public static String NAME = "monolitheye";
	
	public Vec3d originPos;

	private EntityLivingBase targetedEntity;
    private float field_175485_bl;
    private float field_175486_bm;
    private boolean isStunned;
        
	public EntityMonolithEye(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		originPos = this.getPositionVector();
	}
	
	public static void init(int entityId) {
		EntityRegistry.registerModEntity(EntityMonolithEye.class, NAME, entityId, ToroBaseMod.instance, 60, 2, true);
	}

    @SideOnly(Side.CLIENT)
    public float func_175469_o(float p_175469_1_)
    {
        return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
    }
    
	@Override
	protected boolean canDespawn() {
		return false;
	}

	protected void initEntityAI() {
//		this.tasks.addTask(1, new EntityAIMonolithStun(this));				
//	    this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 30, 10.0F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));
//		this.tasks.addTask(3, new EntityAILookIdle(this));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITCH_AMBIENT;
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_WITCH_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITCH_DEATH;
	}

	public void setAggressive(boolean aggressive) {
		this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	}

	public void onLivingUpdate() {
		updateLogic();
		spawnAuraParticle();
		super.onLivingUpdate();
	}

	protected void updateLogic() {
		if (this.worldObj.isRemote) {
			return;
		}

		handleAttachLogicUpdate();

		if (this.rand.nextFloat() < 7.5E-4F) {
			this.worldObj.setEntityState(this, (byte) 15);
		}

	}
	
	protected void spawnAuraParticle() {
		if(this.rand.nextDouble() <= 0.66D) {
			this.worldObj.spawnParticle(EnumParticleTypes.DRAGON_BREATH, 
					this.posX + this.rand.nextGaussian() * 0.06999999523162842D, 
					this.posY + 1.5D + this.rand.nextGaussian() * 0.20999999523162842D,
					this.posZ + this.rand.nextGaussian() * 0.06999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);			
		}

	}
	
	protected void handleAttachLogicUpdate() {

	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		
		super.handleStatusUpdate(id);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_GUARDIAN;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
	}

    private void setTargetedEntity(int entityId)
    {
        //this.dataWatcher.set(TARGET_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasTargetedEntity()
    {
        return false; //((Integer)this.dataWatcher.get(TARGET_ENTITY)).intValue() != 0;
    }

    public EntityLivingBase getTargetedEntity()
    {
        if (!this.hasTargetedEntity())
        {
            return null;
        }
        else if (this.worldObj.isRemote)
        {
            if (this.targetedEntity != null)
            {
                return this.targetedEntity;
            }
            else
            {
                Entity entity = null; //this.worldObj.getEntityByID(((Integer)this.dataWatcher.get(TARGET_ENTITY)).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    this.targetedEntity = (EntityLivingBase)entity;
                    return this.targetedEntity;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return this.getAttackTarget();
        }
    }

	private void spawnParticles(double xSpeed, double ySpeed, double zSpeed) {
		if (this.worldObj.isRemote) {
			for (int i = 0; i < 32; ++i) {
				worldObj.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, xSpeed, ySpeed, zSpeed, new int[0]);
			}
		} else {
			this.worldObj.setEntityState(this, (byte) 42);
		}

	}

	public float getEyeHeight() {
		return 1.62F;
	}
		
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}

		if (hitByRangedAttack(source)) {
			this.isStunned = true;
		}
		
		return false;
//		return super.attackEntityFrom(source, amount);
	}
	
	private boolean hitByRangedAttack(DamageSource source) {
		return source instanceof EntityDamageSourceIndirect;
	}
	
	public boolean getIsStunned() {
		return this.isStunned;
	}

	public void setIsStunned(Boolean bool) {
		this.isStunned = bool;
	}
}
