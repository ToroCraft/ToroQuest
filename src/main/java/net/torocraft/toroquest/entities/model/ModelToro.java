package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;

public class ModelToro extends ModelQuadruped {

	private static final int HEIGHT = 5;

	public ModelToro() {
		super(HEIGHT, 0.9F);
		createHead();
		createBody();
		legAdjustments();
		childZOffset += 2.0F;
	}

	protected void createHead() {
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		head.setRotationPoint(0.0F, 4.0F, -8.0F);
		head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
	}

	protected void createBody() {
		body = new ModelRenderer(this, 18, 4);

		body.addBox(-6.0F, -10.0F, -12.0F, 12, 18, 10, 0.0F);

		body.setRotationPoint(0.0F, 5.0F, 2.0F);
		body.setTextureOffset(52, 0);


		body.addBox(0F, 0F, 0F, 5, 1, 1, 0F);
		body.addBox(0F, 0F, 0F, 1, 10, 1, 0F);
		body.addBox(0F, 0F, 0F, 1, 1, 15, 0F);

		/**
		 * x: to the right
		 * 
		 * y: point back
		 * 
		 * z: pointing up
		 * 
		 */

	}

	protected void legAdjustments() {
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
