package net.torocraft.toroquest.entities.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGuard extends ModelBiped {

	private final ModelRenderer bipedCape;

	public ModelGuard() {
		this(0.0F);
	}

	public ModelGuard(float modelSize) {
		super(modelSize, 0.0F, 64, 64);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);

	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if (entityIn.isSneaking()) {
			this.bipedCape.rotationPointY = 2.0F;
		} else {
			this.bipedCape.rotationPointY = 0.0F;
		}

	}

	public void renderCape(float scale) {
		this.bipedCape.render(scale);
	}

}