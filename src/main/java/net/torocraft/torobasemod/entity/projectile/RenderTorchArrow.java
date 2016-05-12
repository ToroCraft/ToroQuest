package net.torocraft.torobasemod.entity.projectile;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.torobasemod.ToroBaseMod;

@SideOnly(Side.CLIENT)
public class RenderTorchArrow extends RenderArrow<EntityTorchArrow> {
	public static final ResourceLocation RES_TORCH_ARROW = new ResourceLocation(ToroBaseMod.MODID + ":textures/entity/projectiles/torch_arrow.png");

	public RenderTorchArrow(RenderManager p_i46549_1_) {
		super(p_i46549_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityTorchArrow entity) {
		return RES_TORCH_ARROW;
	}
}