package net.torocraft.toroquest.entities.render;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.torocraft.toroquest.entities.EntityBas;
import net.torocraft.toroquest.entities.model.ModelBas;

public class RenderBas extends RenderBiped<EntityBas> {
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");

	public RenderBas(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelSkeleton(), 0.5F);

		// this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelZombie(0.5F, true);
				this.modelArmor =  new ModelZombie(0.5F, true);
			}
		});

	}

	@Override
	protected void preRenderCallback(EntityBas entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(2.5F, 3F, 2.5F);
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityBas entity) {
		return SKELETON_TEXTURES;
	}

}
