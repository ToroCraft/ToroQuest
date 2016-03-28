package net.torocraft.games.chess.pieces.models;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ModelRook extends ModelEnderman {

	public ModelRook(float scale) {
		super(scale);
	}

}