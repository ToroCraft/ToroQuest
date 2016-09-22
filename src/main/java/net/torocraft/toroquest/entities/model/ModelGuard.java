package net.torocraft.toroquest.entities.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGuard extends ModelBiped {

	private final ModelRenderer bipedCape;
	private final float modelSize;
	private final float yOffset = 0f;

	public ModelGuard() {
		this(0.0F);
	}

	public ModelGuard(float modelSize) {
		super(modelSize, 0, 64, 64);

		this.modelSize = modelSize;

		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
		this.bipedCape.setRotationPoint(0.0F, 0.0F, 0.0F);


	}

	private static final ResourceLocation CAPETEXTURE = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		// TODO Auto-generated method stub
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		GlStateManager.pushMatrix();

		Minecraft minecraft = Minecraft.getMinecraft();
		// TextureUtil.bindTexture(CAPETEXTURE);
		minecraft.getTextureManager().bindTexture(CAPETEXTURE);

		// this.bipedCape = new ModelRenderer(this, 16, 16);

		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.translate(2.0F, 24.0F * scale, 0.0F);
		this.bipedBody.render(scale);

		GlStateManager.popMatrix();

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