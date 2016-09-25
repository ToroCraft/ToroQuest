package net.torocraft.toroquest.entities.model;

import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.entities.EntityGuard;

@SideOnly(Side.CLIENT)
public class ModelGuard extends ModelBiped {

	private ModelRenderer[] capes = new ModelRenderer[CivilizationType.values().length];

	private final float modelSize;
	private final float yOffset = 0f;

	private CivilizationType civ;

	private static final float DEFAULT_CAPE_ANGLE = 0.08f;

	public ModelGuard() {
		this(0.0F);
	}

	public ModelGuard(float modelSize) {
		super(modelSize, 0, 64, 64);
		this.modelSize = modelSize;
		for (CivilizationType civ : CivilizationType.values()) {
			buildCape(civ);
		}
	}

	public void setCivilization(CivilizationType civ) {
		this.civ = civ;
	}

	protected CivilizationType randomCivilizationType() {
		Random rand = new Random();

		return CivilizationType.values()[rand.nextInt(CivilizationType.values().length)];
	}

	protected void buildCape(CivilizationType civ) {
		ModelRenderer cape = new ModelRenderer(this, 0, 32);
		cape.setTextureSize(64, 64);
		switch (civ) {
		case SUN:
			cape.setTextureOffset(0, 32);
			break;
		case EARTH:
			cape.setTextureOffset(20, 32);
			break;
		case WATER:
			cape.setTextureOffset(40, 32);
			break;
		case WIND:
			cape.setTextureOffset(0, 47);
			break;
		case MOON:
			cape.setTextureOffset(20, 47);
			break;
		case FIRE:
			cape.setTextureOffset(40, 47);
			break;
		default:
			cape.setTextureOffset(0, 32);
			break;
		}
		cape.addBox(-4.5F, 0.0F, 0F, 9, 14, 1, modelSize);
		cape.setRotationPoint(0, 0, 0);
		cape.offsetZ = 0.17f;
		cape.offsetY = +0.05f;
		cape.rotateAngleX = DEFAULT_CAPE_ANGLE;
		bipedBody.addChild(cape);
		cape.isHidden = true;
		capes[civ.ordinal()] = cape;
	}


	private static final float MAX_CAPE_ANI = 0.07f;


	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		capeAnimation((EntityGuard) entityIn);
	}

	protected void capeAnimation(EntityGuard entityIn) {
		if (civ == null) {
			return;
		}

		showCurrentCape();

		float speed = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionZ * entityIn.motionZ);

		float rot = speed * 40;

		if (rot > 0.6f) {
			rot = 0.6f;
		}

		if (entityIn.capeAniUp && entityIn.capeAni > MAX_CAPE_ANI) {
			entityIn.capeAniUp = false;

		} else if (!entityIn.capeAniUp && entityIn.capeAni < 0) {
			entityIn.capeAniUp = true;
		}

		if (entityIn.capeAniUp) {
			entityIn.capeAni += 0.00025;
		} else {
			entityIn.capeAni -= 0.00025;
		}

		rot += entityIn.capeAni;

		capes[civ.ordinal()].rotateAngleX = rot;
	}

	private void showCurrentCape() {
		for (ModelRenderer cape : capes) {
			cape.isHidden = true;
		}
		capes[civ.ordinal()].isHidden = false;
	}
}