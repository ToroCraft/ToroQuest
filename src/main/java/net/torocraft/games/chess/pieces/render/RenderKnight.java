package net.torocraft.games.chess.pieces.render;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.games.chess.pieces.enities.EntityBishop;
import net.torocraft.games.chess.pieces.enities.EntityKnight;
import net.torocraft.games.chess.pieces.models.ModelKnight;
import net.torocraft.games.chess.pieces.models.ModelRook;

@SideOnly(Side.CLIENT)
public class RenderKnight extends RenderLiving<EntityKnight> {
	
	private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
	
	public RenderKnight(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelKnight(2), 0.5F);
		
	}
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be
	 * called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityKnight entity) {
		return slimeTextures;
	}

}