package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntityRainbowGuard;
import net.torocraft.toroquest.entities.model.ModelRainbowGuard;

public class RenderRainbowGuard extends RenderBiped<EntityRainbowGuard> {
	private static final ResourceLocation TEXTURES = new ResourceLocation(ToroQuest.MODID + ":textures/entity/guard/guard.png");

	private final ModelRainbowGuard rainbowGuardModel;

	public RenderRainbowGuard(RenderManager renderManagerIn) {
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

	public void doRender(EntityRainbowGuard entity, double x, double y, double z, float entityYaw, float partialTicks) {
		rainbowGuardModel.atAttention = entity.isAtAttention();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityRainbowGuard entity) {
		return TEXTURES;
	}
}
