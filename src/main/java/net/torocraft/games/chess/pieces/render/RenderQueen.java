package net.torocraft.games.chess.pieces.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.games.chess.pieces.enities.EntityBishop;
import net.torocraft.games.chess.pieces.enities.EntityQueen;
import net.torocraft.games.chess.pieces.models.ModelQueen;
import net.torocraft.games.chess.pieces.models.ModelRook;

@SideOnly(Side.CLIENT)
public class RenderQueen extends RenderLiving<EntityQueen> {
	
	 private static final ResourceLocation skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	
	public RenderQueen(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelQueen(), 0.5F);
		
	}
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be
	 * called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityQueen entity) {
		return skeletonTextures;
	}

}