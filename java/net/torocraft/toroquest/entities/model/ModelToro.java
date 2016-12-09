package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelToro extends ModelQuadruped {

	public boolean isCharging;

	protected ModelRenderer horns;

	private static final float HEAD_HEIGHT = 3;
	private static final float HEAD_DISTANCE = -15;

	private static final float BODY_HEIGHT = 1;

	private static final int TEX_WIDTH = 100;
	private static final int TEX_HEIGHT = 100;
	private static final int LEG_HEIGHT = 12;

	public ModelToro() {
		super(LEG_HEIGHT, 0f);
		createHead();
		createBody();
		createLegs();
		childZOffset += 2.0F;
	}

	protected void createHead() {
		head = new ModelRenderer(this, 0, 0);

		head.setTextureSize(TEX_WIDTH, TEX_HEIGHT);

		head.addBox(-5.0F, -5.0F, -8.0F, 10, 10, 8, 0.0F);

		head.setRotationPoint(0.0F, HEAD_HEIGHT, HEAD_DISTANCE);

		// head.setRotationPoint(0.0F, 30.0F, -15.0F); moved it down

		horns = new ModelRenderer(this, 0, 0);
		horns.setTextureSize(TEX_WIDTH, TEX_HEIGHT);
		head.addChild(horns);
		horns.setTextureOffset(36, 0).addBox(-6.0F, -16F, -3.0F, 1, 10, 1, 0.0F);
		horns.setTextureOffset(36, 0).addBox(5.0F, -16F, -3.0F, 1, 10, 1, 0.0F);
		horns.rotateAngleX = 0.5f;
	}

	protected void createBody() {
		body = new ModelRenderer(this, 0, 0);
		body.setTextureSize(TEX_WIDTH, TEX_HEIGHT);
		body.setTextureOffset(0, 18).addBox(-7.0F, -15.0F, -12.0F, 14, 15, 16, 0);
		body.setTextureOffset(0, 49).addBox(-6.0F, 0.0F, -12.0F, 12, 24, 15, 0);
		body.setRotationPoint(0, BODY_HEIGHT, 0);
		body.setTextureOffset(52, 0);
	}

	private void drawAxes(ModelRenderer m) {
		if (m == null) {
			m = new ModelRenderer(this, 0, 16);
		}
		m.addBox(0F, 0F, 0F, 10, 1, 1, 0F);
		m.addBox(0F, 0F, 0F, 1, 20, 1, 0F);
		m.addBox(0F, 0F, 0F, 1, 1, 30, 0F);
	}

	private void createLegs() {
		createBackRightLeg();
		createBackLeftLeg();
		createFronRightLeg();
		createFrontLeftLeg();
	}

	protected void createBackRightLeg() {
		leg1 = createLeg();
		leg1.setRotationPoint(-3.8F, (float) (24 - LEG_HEIGHT), 20F);
	}

	protected void createBackLeftLeg() {
		leg2 = createLeg();
		leg2.setRotationPoint(3.8F, (float) (24 - LEG_HEIGHT), 20F);
	}

	protected void createFronRightLeg() {
		leg3 = createLeg();
		leg3.setRotationPoint(-4.0F, (float) (24 - LEG_HEIGHT), -8.0F);
	}

	protected void createFrontLeftLeg() {
		leg4 = createLeg();
		leg4.setRotationPoint(4.0F, (float) (24 - LEG_HEIGHT), -8.0F);
	}

	protected ModelRenderer createLeg() {
		ModelRenderer leg = new ModelRenderer(this, 40, 0);
		leg.setTextureSize(TEX_WIDTH, TEX_HEIGHT);
		leg.addBox(-2.0F, 0.0F, -2.0F, 4, LEG_HEIGHT, 4, 0);
		return leg;
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		head.rotationPointY = HEAD_HEIGHT;
		head.rotationPointZ = HEAD_DISTANCE;

		body.rotationPointY = BODY_HEIGHT;

		if (this.isCharging) {
			head.rotateAngleX = 0.8f;
			body.rotateAngleX += 0.10f;


			head.rotationPointY += 6;
			head.rotationPointZ += 2;

			body.rotationPointY += 4;

		}
	}

}
