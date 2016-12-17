package net.torocraft.toroquest.entities.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntitySentry;
import net.torocraft.toroquest.entities.model.ModelSentry;

public class RenderSentry extends RenderBiped<EntitySentry> {
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation TEXTURES = new ResourceLocation(ToroQuest.MODID + ":textures/entity/guard/guard.png");

	public RenderSentry(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelSentry(), 0.5F);
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelSentry(0.5F, true);
				this.modelArmor = new ModelSentry(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntitySentry entity) {
		return TEXTURES;
	}
}
