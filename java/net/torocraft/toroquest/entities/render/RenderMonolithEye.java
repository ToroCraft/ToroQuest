package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityMonolithEye;
import net.torocraft.toroquest.entities.model.ModelMonolithEye;

@SideOnly(Side.CLIENT)
public class RenderMonolithEye extends RenderLiving<EntityMonolithEye> {
	private static final ResourceLocation guardianTextures = new ResourceLocation("toroquest:textures/entity/monolitheye.png");

	public RenderMonolithEye(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelMonolithEye(), 0.5F);
	}

	public ModelMonolithEye getMainModel() {
		return (ModelMonolithEye) super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(EntityMonolithEye entity) {
		return guardianTextures;
	}

	protected void preRenderCallback(EntityMonolithEye entitylivingbaseIn, float partialTickTime) {
		float size = 1.0F; // 0.9375F;
		this.shadowSize = 0.5F;

		GlStateManager.scale(size, size, size);
	}
}