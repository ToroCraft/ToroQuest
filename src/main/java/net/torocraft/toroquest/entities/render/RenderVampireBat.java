package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityVampireBat;
import net.torocraft.toroquest.entities.model.ModelVampireBat;

@SideOnly(Side.CLIENT)
public class RenderVampireBat extends RenderLiving<EntityVampireBat> {
	private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

	public RenderVampireBat(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelVampireBat(), 0.25F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityVampireBat entity) {
		return BAT_TEXTURES;
	}

	/**
	 * Allows the render to do state modifications necessary before the model is
	 * rendered.
	 */
	@Override
	protected void preRenderCallback(EntityVampireBat entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
	}

	@Override
	protected void applyRotations(EntityVampireBat entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
		bounceModel(p_77043_2_);
		super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
	}

	protected void bounceModel(float p_77043_2_) {
		GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
	}
}