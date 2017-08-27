package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityMage;
import net.torocraft.toroquest.entities.model.ModelMage;

@SideOnly(Side.CLIENT)
public class RenderMage extends RenderLiving<EntityMage> {
	private static final ResourceLocation TEXTURES = new ResourceLocation("toroquest:textures/entity/mage/mage.png");

	private final ModelMage mageModel;

	public RenderMage(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelMage(), 0.5F);
		this.mageModel = getMainModel();
		addLayer(new LayerHeldItem(this));
	}

	public ModelMage getMainModel() {
		return (ModelMage) super.getMainModel();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityMage entity) {
		return TEXTURES;
	}

	public void doRender(EntityMage entity, double x, double y, double z, float entityYaw, float partialTicks) {
		mageModel.isStaffAttacking = entity.isStaffAttacking();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityMage entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;

		this.shadowSize = 0.5F;

		GlStateManager.scale(f, f, f);
	}
}