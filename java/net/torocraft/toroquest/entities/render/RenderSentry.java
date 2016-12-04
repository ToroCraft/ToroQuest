package net.torocraft.toroquest.entities.render;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.torocraft.toroquest.entities.EntitySentry;

public class RenderSentry extends RenderLiving<EntitySentry> {
	private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");

	public RenderSentry(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelIronGolem(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySentry entity) {
		return ironGolemTextures;
	}
}
