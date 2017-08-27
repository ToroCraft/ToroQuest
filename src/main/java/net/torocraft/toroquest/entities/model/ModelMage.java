package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMage extends ModelBiped {

	protected ModelRenderer staff;
	protected ModelRenderer staffHead;

	public boolean isStaffAttacking;

	public ModelMage() {
		this(0.0F, false);
	}

	public ModelMage(float modelSize, boolean p_i1168_2_) {
		super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
		staff = new ModelRenderer(this, 0, 32);
		staffHead = new ModelRenderer(this, 0, 32);
		staff();
	}

	protected void guideGrid(ModelRenderer space) {
		space.addBox(0F, 0F, 0F, 20, 1, 1, 0F);
		space.addBox(0F, 0F, 0F, 1, 15, 1, 0F);
		space.addBox(0F, 0F, 0F, 1, 1, 10, 0F);
	}

	protected void staff() {
		staff.setRotationPoint(-1F, 8F, 0F);
		staff.addBox(0F, 0F, -13F, 1, 1, 30, 0F);
		staffHead();
		bipedRightArm.addChild(staff);
	}

	protected void staffHead() {
		staffHead.setRotationPoint(0F, 0F, -13F);
		staffHead.addBox(1.3F, 0F, -3F, 1, 1, 5, 0.0F);
		staffHead.addBox(-1.3F, 0F, -3F, 1, 1, 5, 0.0F);
		staffHead.addBox(0F, 1.3F, -3F, 1, 1, 5, 0.0F);
		staffHead.addBox(0F, -1.3F, -3F, 1, 1, 5, 0.0F);

		staffHead.setTextureOffset(7, 32);
		staffHead.addBox(0F, 0F, 0F, 1, 1, 2, 0.3F);
		staff.addChild(staffHead);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		arms(ageInTicks, entityIn);
	}

	protected void arms(float ageInTicks, Entity entityIn) {
		boolean armIsRaised = entityIn instanceof EntityZombie && ((EntityZombie) entityIn).isArmsRaised();
		float swingAngle = MathHelper.sin(swingProgress * (float) Math.PI);
		float f1 = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float) Math.PI);

		/*
		 * z rotates arm inward and outward
		 * 
		 * y rotates the axis down the arm
		 */

		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		staff.rotateAngleX = 0F;

		if (isStaffAttacking) {
			bipedRightArm.rotateAngleX += -1.2F;
			bipedLeftArm.rotateAngleX += -1.4F;
			staff.rotateAngleX = 1.1F;
		} else {
			bipedRightArm.rotateAngleX += -1.1F;
		}

	}

}