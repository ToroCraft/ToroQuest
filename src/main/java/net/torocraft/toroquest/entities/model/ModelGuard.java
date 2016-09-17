package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGuard extends ModelBiped {
	public ModelGuard() {
		this(0.0F);
	}

	public ModelGuard(float modelSize) {
		super(modelSize, 0.0F, 64, 64);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		// zombieArmLogic(ageInTicks, entityIn);

	}

	protected void zombieArmLogic(float ageInTicks, Entity entityIn) {
		boolean flag = entityIn instanceof EntityZombie && ((EntityZombie) entityIn).isArmsRaised();
		

		float f = MathHelper.sin(swingProgress * (float) Math.PI);
		float f1 = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float) Math.PI);
		
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
		bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
		float f2 = -(float) Math.PI / (flag ? 1.5F : 2.25F);
		bipedRightArm.rotateAngleX = f2;
		bipedLeftArm.rotateAngleX = f2;
		bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}
}