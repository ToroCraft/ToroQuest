package net.torocraft.toroquest.entities.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.entities.EntityToro;
import net.torocraft.toroquest.entities.model.ModelToro;

@SideOnly(Side.CLIENT)
public class RenderToro extends RenderLiving<EntityToro> {
	private static final ResourceLocation TORO_TEXTURES = new ResourceLocation("toroquest:textures/entity/toro/toro.png");

	private final ModelToro toroModel;

	public RenderToro(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
		this.toroModel = (ModelToro) super.mainModel;
	}

	public void doRender(EntityToro entity, double x, double y, double z, float entityYaw, float partialTicks) {
		toroModel.isCharging = entity.isCharging();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityToro entity) {
		return TORO_TEXTURES;
	}
}