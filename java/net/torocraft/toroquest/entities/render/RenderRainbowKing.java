package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntityRainbowKing;
import net.torocraft.toroquest.entities.model.ModelRainbowGuard;

public class RenderRainbowKing extends RenderBiped<EntityRainbowKing> {
	private static final ResourceLocation TEXTURES = new ResourceLocation(ToroQuest.MODID + ":textures/entity/guard/guard.png");

	private final ModelRainbowGuard rainbowGuardModel;

	public RenderRainbowKing(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelRainbowGuard(), 0.5F);
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelRainbowGuard(0.5F, true);
				this.modelArmor = new ModelRainbowGuard(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
		this.rainbowGuardModel = (ModelRainbowGuard) super.mainModel;
	}

	public void doRender(EntityRainbowKing entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected void preRenderCallback(EntityRainbowKing entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityRainbowKing entity) {
		return TEXTURES;
	}
}
