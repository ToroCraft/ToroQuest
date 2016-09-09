package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;

public class ModelToro extends ModelQuadruped {

	private static final int HEIGHT = 5;

	public ModelToro() {
		super(HEIGHT, 0f);
		createHead();
		createBody();

		createLegs();

		childZOffset += 2.0F;
	}

	protected void createHead() {
		head = new ModelRenderer(this, 0, 0);
		// head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		head.addBox(-4.0F, -400.0F, -6.0F, 8, 8, 6, 0.0F);
		head.setRotationPoint(0.0F, 4.0F, -8.0F);
		head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
	}

	protected void createBody() {
		float zoom = 2;

		body = new ModelRenderer(this, 18, 4);

		// front
		// body.addBox(-7.0F, -16.0F, -12.0F, 16, 10, 12, zoom);
		body.addBox(-8.0F, -16.0F, -12.0F, 16, 16, 15, zoom);

		// back
		// body.addBox(-6.0F, -6.0F, -12.0F, 14, 18, 11, zoom);
		body.addBox(-7.0F, 0.0F, -12.0F, 14, 24, 14, zoom);

		body.setRotationPoint(0.0F, 5.0F, 2);
		body.setTextureOffset(52, 0);

		drawAxes();

		/**
		 * x: to the right
		 * 
		 * y: point back
		 * 
		 * z: pointing up
		 * 
		 */

	}

	private void drawAxes() {
		body.addBox(0F, 0F, 0F, 10, 1, 1, 0F);
		body.addBox(0F, 0F, 0F, 1, 20, 1, 0F);
		body.addBox(0F, 0F, 0F, 1, 1, 30, 0F);
	}

	private void createLegs() {

		int height = 5;
		int scale = 0;

		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		leg1.setRotationPoint(-3.0F, (float) (24 - height), 7.0F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		leg2.setRotationPoint(3.0F, (float) (24 - height), 7.0F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		leg3.setRotationPoint(-3.0F, (float) (24 - height), -5.0F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
		leg4.setRotationPoint(3.0F, (float) (24 - height), -5.0F);

		--leg1.rotationPointX;
		++leg2.rotationPointX;
		leg1.rotationPointZ += 0.0F;
		leg2.rotationPointZ += 0.0F;
		--leg3.rotationPointX;
		++leg4.rotationPointX;
		--leg3.rotationPointZ;
		--leg4.rotationPointZ;
	}

}
